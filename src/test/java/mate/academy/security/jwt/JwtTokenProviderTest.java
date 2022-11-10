package mate.academy.security.jwt;

import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.exception.InvalidJwtAuthenticationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {
    private static final String LOGIN = "bob@domain.com";
    private static final String ROLE = "USER";
    private static final String PASSWORD = "1234";
    @Mock
    private UserDetailsService userDetailsService;
    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private HttpServletRequest httpServletRequest;
    private String token;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000);
        token = jwtTokenProvider.createToken(LOGIN, List.of(ROLE));
    }

    @Test
    void createToken_Ok() {
        Assertions.assertNotNull(token);
    }

    @Test
    void getAuthentication_Ok() {
        UserDetails userDetails = User.withUsername(LOGIN).password(PASSWORD).roles(ROLE).build();
        Mockito.when(userDetailsService.loadUserByUsername(any())).thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        User user = (User) actual.getPrincipal();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(userDetails.getUsername(), actual.getName());
        Assertions.assertEquals(userDetails.getPassword(), user.getPassword());
        Assertions.assertEquals(userDetails.getAuthorities(), user.getAuthorities());
    }

    @Test
    void getUsername_Ok() {
        Assertions.assertEquals(LOGIN, jwtTokenProvider.getUsername(token));
    }

    @Test
    void resolveToken_ValidToken_Ok() {
        Mockito.when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(httpServletRequest);
        Assertions.assertEquals(token, actual);
    }

    @Test
    void resolveToken_NullToken_NotOk() {
        Mockito.when(httpServletRequest.getHeader("Authorization")).thenReturn(null);
        String actual = jwtTokenProvider.resolveToken(httpServletRequest);
        Assertions.assertNull(actual);
    }

    @Test
    void resolveToken_NotBearerToken_NotOk() {
        Mockito.when(httpServletRequest.getHeader("Authorization")).thenReturn(token);
        String actual = jwtTokenProvider.resolveToken(httpServletRequest);
        Assertions.assertNull(actual);
    }

    @Test
    void validateToken_ValidToken_Ok() {
        boolean actual = jwtTokenProvider.validateToken(token);
        Assertions.assertTrue(actual);
    }

    @Test
    void validateToken_NotValidToken_NotOk() {
        InvalidJwtAuthenticationException thrown =
                Assertions.assertThrows(InvalidJwtAuthenticationException.class,
                        () -> jwtTokenProvider.validateToken("diff" + token));
        Assertions.assertEquals("Expired or invalid JWT token", thrown.getMessage());
    }
}

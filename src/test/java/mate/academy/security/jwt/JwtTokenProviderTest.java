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
    private static final String EMAIL = "bob@e.mail";
    private static final String PASSWORD = "qwerty";
    private static final String ROLE = "USER";
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private HttpServletRequest httpServletRequest;
    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;
    private String token;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                3600000L, long.class);
        token = jwtTokenProvider.createToken(EMAIL, List.of(ROLE));
    }

    @Test
    void createToken_ok() {
        Assertions.assertNotNull(token);
    }

    @Test
    void getAuthentication_ok() {
        UserDetails userDetails = User.withUsername(EMAIL).password(PASSWORD).roles(ROLE).build();
        Mockito.when(userDetailsService.loadUserByUsername(any())).thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        User user = (User) actual.getPrincipal();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(userDetails.getUsername(), actual.getName());
        Assertions.assertEquals(userDetails.getPassword(), user.getPassword());
        Assertions.assertEquals(userDetails.getAuthorities(), user.getAuthorities());
    }

    @Test
    void getUsername_ok() {
        Assertions.assertEquals(EMAIL, jwtTokenProvider.getUsername(token));
    }

    @Test
    void resolveToken_ok() {
        Mockito.when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + token);
        Assertions.assertEquals(token, jwtTokenProvider.resolveToken(httpServletRequest));
    }

    @Test
    void resolveToken_nullToken_notOk() {
        Mockito.when(httpServletRequest.getHeader("Authorization")).thenReturn(null);
        Assertions.assertNull(jwtTokenProvider.resolveToken(httpServletRequest));
    }

    @Test
    void resolveToken_notBearerToken_notOk() {
        Mockito.when(httpServletRequest.getHeader("Authorization")).thenReturn(token);
        Assertions.assertNull(jwtTokenProvider.resolveToken(httpServletRequest));
    }

    @Test
    void validateToken_ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_invalidToken_notOk() {
        Assertions.assertEquals("Expired or invalid JWT token",
                Assertions.assertThrows(InvalidJwtAuthenticationException.class,
                        () -> jwtTokenProvider.validateToken("invalid_token")).getMessage());
    }
}

package mate.academy.security.jwt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "1234";
    private static final String ROLE_NAME = "USER";
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private HttpServletRequest request;
    private String token;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider,
                "secretKey","secret");
        ReflectionTestUtils.setField(jwtTokenProvider,
                "validityInMilliseconds", 3600000L);
        token = jwtTokenProvider.createToken(EMAIL, List.of(ROLE_NAME));
    }

    @Test
    void createToken_Ok() {
        Assertions.assertNotNull(token);
    }

    @Test
    void getAuthentication_Ok() {
        UserDetails userDetails = User.builder()
                .username(EMAIL)
                .password(PASSWORD)
                .roles(ROLE_NAME)
                .build();
        Mockito.when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        Assertions.assertNotNull(actual);
    }

    @Test
    void getUsername_Ok() {
        String actual = jwtTokenProvider.getUsername(token);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual);
    }

    @Test
    void resolveToken_Ok() {
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(token, actual);
    }

    @Test
    void validateToken_Ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_TokenIsNull_NotOk() {
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(null));
        Assertions.assertEquals("Expired or invalid JWT token", exception.getMessage());
    }

    @Test
    void validateToken_IncorrectToken_NotOk() {
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken("incorrectToken"));
        Assertions.assertEquals("Expired or invalid JWT token", exception.getMessage());
    }
}

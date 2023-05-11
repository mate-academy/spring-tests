package mate.academy.security.jwt;

import java.util.ArrayList;
import java.util.Base64;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String USER_NAME = "bob@i.ua";
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;
    private String secretKey = "secret";
    private Long validityInMilliseconds = 3600000L;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.spy(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", secretKey);
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                validityInMilliseconds);
    }

    @Test
    void init_ok() {
        jwtTokenProvider.init();
        Object actual = ReflectionTestUtils.getField(jwtTokenProvider, "secretKey");
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(secretKey, actual);
    }

    @Test
    void createToken_ok() {
        String login = jwtTokenProvider.createToken(USER_NAME, new ArrayList<>());
        Assertions.assertNotNull(login);
    }

    @Test
    void getUserName_ok() {
        String token = jwtTokenProvider.createToken(USER_NAME, new ArrayList<>());
        String actual = jwtTokenProvider.getUsername(token);
        Assertions.assertNotNull(token);
        Assertions.assertEquals(USER_NAME, actual);

    }

    @Test
    void resolveToken_ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn(null);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNull(actual);
    }

    @Test
    void validateToken_ok() {
        String token = jwtTokenProvider.createToken("login", new ArrayList<>());
        Assertions.assertNotNull(token);
    }

    @Test
    void resolveToken_tokenIsNull_notOk() {
        String token = jwtTokenProvider.createToken("login", new ArrayList<>());
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNotNull(actual);
    }

    @Test
    void validateToken_nullToken_NotOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(null));
    }
}

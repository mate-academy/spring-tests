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
    private static final String VALID_EMAIL = "bob@i.ua";
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;
    private String secretKey = "secretKey";
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
    void init_Ok() {
        jwtTokenProvider.init();
        Object actual = ReflectionTestUtils.getField(jwtTokenProvider, "secretKey");
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(secretKey, actual);
    }

    @Test
    void createToken_Ok() {
        String login = jwtTokenProvider.createToken(VALID_EMAIL, new ArrayList<>());
        Assertions.assertNotNull(login);
    }

    @Test
    void getUserByName_Ok() {
        String token = jwtTokenProvider.createToken(VALID_EMAIL, new ArrayList<>());
        String actual = jwtTokenProvider.getUsername(token);
        Assertions.assertNotNull(token);
        Assertions.assertEquals(VALID_EMAIL, actual);

    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn(null);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNull(actual);
    }

    @Test
    void validateToken_Ok() {
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

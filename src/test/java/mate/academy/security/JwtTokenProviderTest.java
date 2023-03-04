package mate.academy.security;

import static org.mockito.ArgumentMatchers.any;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;

public class JwtTokenProviderTest {
    private static final String INVALID_TOKEN = "invalidToken";
    private static final String SECRET_KEY = "secret";
    private static final Long MILLISECONDS = 3600000L;
    private static final List<String> ROLES = List.of("USER");
    private static final String LOGIN = "bob";
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib2IiLCJyb2xlcyI6Wy"
            + "JVU0VSIl0sImlhdCI6MTY3Nzk1MDQ5MywiZXhwIjoxNjc3OTU0MDkzfQ.P1iC6kMp4vIUz4nT4Vzp4OXqK"
            + "xOQKsY4OrXakH_zX8E";
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        String secretKey = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
        setField(jwtTokenProvider, "secretKey", secretKey, String.class);
        setField(jwtTokenProvider, "validityInMilliseconds", MILLISECONDS, Long.class);
    }

    @Test
    void createToken_Ok() {
        String token = jwtTokenProvider.createToken(LOGIN, ROLES);
        Assertions.assertNotNull(token);
        Assertions.assertEquals(151, token.length());
    }

    @Test
    public void getUserName_Ok() {
        String username = jwtTokenProvider.getUsername(TOKEN);
        Assertions.assertNotNull(username);
        Assertions.assertEquals(LOGIN, username);
    }

    @Test
    void getUserName_NotOk() {
        Assertions.assertThrows(Exception.class, () -> jwtTokenProvider.getUsername(INVALID_TOKEN));
    }

    @Test
    void validateToken_Ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(TOKEN));
    }

    @Test
    void validateToken_NotOk() {
        Assertions.assertThrows(RuntimeException.class, () -> jwtTokenProvider
                .validateToken(INVALID_TOKEN));
    }

    @Test
    public void resolveToken_InvalidToken_NotOk() {
        HttpServletRequest mock = Mockito.mock(HttpServletRequest.class);
        Mockito.when(mock.getHeader(any())).thenReturn(LOGIN);
        Assertions.assertNotEquals(LOGIN, jwtTokenProvider.resolveToken(mock));
    }

    @Test
    public void resolveToken_Ok() {
        HttpServletRequest mock = Mockito.mock(HttpServletRequest.class);
        Mockito.when(mock.getHeader(any())).thenReturn("Bearer " + LOGIN);
        Assertions.assertEquals(LOGIN, jwtTokenProvider.resolveToken(mock));
    }

    private void setField(Object object, String fieldName, Object value, Class<?> type) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, type.cast(value));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set field " + fieldName + " on " + object, e);
        }
    }
}

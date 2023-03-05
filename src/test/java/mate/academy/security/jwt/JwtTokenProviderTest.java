package mate.academy.security.jwt;

import static org.mockito.ArgumentMatchers.any;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;

public class JwtTokenProviderTest {
    private static final String INCORRECT_TOKEN = "incorrectTocken";
    private static final String SECRET_KEY = "secret";
    private static final String SECRET_KEY_FIELD_NAME = "secretKey";
    private static final String VALID_IN_MILLISECONDS_FIELD_NAME = "validityInMilliseconds";
    private static final Long VALIDITY_IN_MILLISECONDS = 3600000L;
    private static final List<String> ROLES = List.of("USER");
    private static final String LOGIN = "bob";
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib2IiLCJyb2xlcyI6Wy"
            + "JVU0VSIl0sImlhdCI6MTY3ODAxMzM2NCwiZXhwIjoxNjc4MDE2OTY0fQ.nyP53FMK6fB9F1HyrVRvIT"
            + "e_BALazE589KjbNR0peFQ";
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        String secretKey = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
        setField(jwtTokenProvider, SECRET_KEY_FIELD_NAME, secretKey, String.class);
        setField(jwtTokenProvider, VALID_IN_MILLISECONDS_FIELD_NAME,
                VALIDITY_IN_MILLISECONDS, Long.class);
    }

    @Test
    void createToken_Ok() {
        String token = jwtTokenProvider.createToken(LOGIN, ROLES);
        Assertions.assertNotNull(token);
        Assertions.assertEquals(151, token.length());
        System.out.println(token);
    }

    @Test
    public void getUserName_Ok() {
        String username = jwtTokenProvider.getUsername(TOKEN);
        Assertions.assertNotNull(username);
        Assertions.assertEquals(LOGIN, username);
    }

    @Test
    void getUserName_NotOk() {
        Assertions.assertThrows(Exception.class, () ->
                jwtTokenProvider.getUsername(INCORRECT_TOKEN));
    }

    @Test
    void validateToken_Ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(TOKEN));
    }

    @Test
    void validateToken_NotOk() {
        Assertions.assertThrows(RuntimeException.class, () -> jwtTokenProvider
                .validateToken(INCORRECT_TOKEN));
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

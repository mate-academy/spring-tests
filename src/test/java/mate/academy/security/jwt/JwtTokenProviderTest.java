package mate.academy.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;

class JwtTokenProviderTest {
    private static final List<String> ROLES = List.of("USER");
    private static final String EMAIL = "mail@gmail.com";
    private static final String EMPTY = "";
    private UserDetailsService userDetailsService;
    private String token;
    private HttpServletRequest req;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        req = Mockito.mock(HttpServletRequest.class);
        Field secretKey = JwtTokenProvider.class.getDeclaredField("secretKey");
        secretKey.setAccessible(true);
        secretKey.set(jwtTokenProvider, "secret");
        Field validity = JwtTokenProvider.class.getDeclaredField("validityInMilliseconds");
        validity.setAccessible(true);
        validity.set(jwtTokenProvider, 3600000);
        token = jwtTokenProvider.createToken(EMAIL, ROLES);
    }

    @Test
    void createToken_Ok() {
        assertNotNull(token);
        assertNotEquals(token, "");
    }

    @Test
    void getAuthentication_nullToken_notOk() {
        assertThrows(IllegalArgumentException.class,
                () -> jwtTokenProvider.getAuthentication(null));
    }

    @Test
    void getAuthentication_emptyToken_notOk() {
        assertThrows(IllegalArgumentException.class,
                () -> jwtTokenProvider.getAuthentication(""));
    }

    @Test
    void getUsername_Ok() {
        assertEquals(jwtTokenProvider.getUsername(token), EMAIL);
    }

    @Test
    void getUsername_nullToken_notOk() {
        assertThrows(IllegalArgumentException.class,
                () -> jwtTokenProvider.getUsername(null));
    }

    @Test
    void getUsername_emptyToken_notOk() {
        assertThrows(IllegalArgumentException.class,
                () -> jwtTokenProvider.getUsername(EMPTY));
    }

    @Test
    void resolveToken_Ok() {
        when(req.getHeader("Authorization")).thenReturn("Bearer " + token);
        assertEquals(jwtTokenProvider.resolveToken(req), token);
    }

    @Test
    void resolveToken_notBearerToken_notOk() {
        when(req.getHeader("Authorization")).thenReturn("Not bearer " + token);
        assertNull(jwtTokenProvider.resolveToken(req));
    }

    @Test
    void resolveToken_nullHeader_notOk() {
        when(req.getHeader("Authorization")).thenReturn(null);
        assertNull(jwtTokenProvider.resolveToken(req));
    }

    @Test
    void resolveToken_nullRequest_notOk() {
        assertThrows(NullPointerException.class,
                () -> jwtTokenProvider.resolveToken(null));
    }

    @Test
    void validateToken_Ok() {
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_nullToken_notOk() {
        assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(null));
    }

    @Test
    void validateToken_emptyToken_notOk() {
        assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(EMPTY));
    }

    @Test
    void validateToken_expiredToken_notOk() throws IllegalAccessException, NoSuchFieldException {
        Field validityField = JwtTokenProvider.class.getDeclaredField("validityInMilliseconds");
        validityField.setAccessible(true);
        validityField.set(jwtTokenProvider, 0);

        String newToken = jwtTokenProvider.createToken(EMAIL, ROLES);
        assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(newToken));
    }
}

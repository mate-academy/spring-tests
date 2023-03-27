package mate.academy.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.lang.reflect.Field;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

class JwtTokenProviderTest {
    private static final String EMAIL = "bob@mate.academy";
    private static final String PASSWORD = "12345678";
    private static final List<String> ROLES = List.of("USER");
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private HttpServletRequest req;
    private String token;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
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
    void getAuthentication_Ok() {
        User.UserBuilder builder = User.withUsername(EMAIL);
        builder.password(PASSWORD);
        builder.roles(ROLES.toArray(String[]::new));
        UserDetails userDetails = builder.build();
        Mockito.when(userDetailsService.loadUserByUsername(any())).thenReturn(userDetails);
        Authentication auth = jwtTokenProvider.getAuthentication(token);
        assertNotNull(auth);
        assertEquals(auth.getName(), EMAIL);
        assertEquals(((User) auth.getPrincipal()).getPassword(), PASSWORD);
    }

    @Test
    void getAuthentication_nullToken_notOk() {
        assertThrows(IllegalArgumentException.class, () -> {
            jwtTokenProvider.getAuthentication(null);
        });
    }

    @Test
    void getAuthentication_emptyToken_notOk() {
        assertThrows(IllegalArgumentException.class, () -> {
            jwtTokenProvider.getAuthentication("");
        });
    }

    @Test
    void getUsername_Ok() {
        assertEquals(jwtTokenProvider.getUsername(token), EMAIL);
    }

    @Test
    void getUsername_nullToken_notOk() {
        assertThrows(IllegalArgumentException.class, () -> jwtTokenProvider.getUsername(null));
    }

    @Test
    void getUsername_emptyToken_notOk() {
        assertThrows(IllegalArgumentException.class, () -> jwtTokenProvider.getUsername(""));
    }

    @Test
    void resolveToken_Ok() {
        Mockito.when(req.getHeader("Authorization")).thenReturn("Bearer " + token);
        assertEquals(jwtTokenProvider.resolveToken(req), token);
    }

    @Test
    void resolveToken_notBearerToken_notOk() {
        Mockito.when(req.getHeader("Authorization")).thenReturn("Not bearer " + token);
        assertNull(jwtTokenProvider.resolveToken(req));
    }

    @Test
    void resolveToken_nullHeader_notOk() {
        Mockito.when(req.getHeader("Authorization")).thenReturn(null);
        assertNull(jwtTokenProvider.resolveToken(req));
    }

    @Test
    void resolveToken_nullRequest_notOk() {
        assertThrows(NullPointerException.class, () -> jwtTokenProvider.resolveToken(null));
    }

    @Test
    void validateToken_Ok() {
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_nullToken_notOk() {
        assertThrows(RuntimeException.class, () -> jwtTokenProvider.validateToken(null));
    }

    @Test
    void validateToken_emptyToken_notOk() {
        assertThrows(RuntimeException.class, () -> jwtTokenProvider.validateToken(""));
    }

    @Test
    void validateToken_expiredToken_notOk() throws IllegalAccessException, NoSuchFieldException {
        Field validity = JwtTokenProvider.class.getDeclaredField("validityInMilliseconds");
        validity.setAccessible(true);
        validity.set(jwtTokenProvider, 0);
        String newToken = jwtTokenProvider.createToken(EMAIL, ROLES);
        assertThrows(RuntimeException.class, () -> jwtTokenProvider.validateToken(newToken));
    }
}

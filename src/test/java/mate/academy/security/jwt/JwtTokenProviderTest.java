package mate.academy.security.jwt;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtTokenProviderTest {
    private static final String secretKey = "secret";
    private static final long validityInMilliseconds = 3600000L;
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9"
            + ".eyJzdWIiOiJiY2h1cGlrYUBtYXRlLmFjYWRlbXkiLCJyb2xlcyI6WyJBRE1JTiIsIlVTRVIiXSwiaWF0IjoxNjY5O"
            + "TIzODQwLCJleHAiOjE2Njk5Mjc0NDB9.pqwCA7R47eZ8GZ2jPwFxBDVy9rew60mGoyQZ8wF6CF0";
    private static final String EMAIL = "bchupika@mate.academy";
    private static final List<String> ROLES = List.of("ADMIN", "USER");
    private static final UserDetailsService userDetailsService = mock(UserDetailsService.class);
    private String token;
    private static JwtTokenProvider jwtTokenProvider;

    @BeforeAll
    static void beforeAll() throws IllegalAccessException {
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        Class<?> clazz = jwtTokenProvider.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().equals("secretKey")) {
                field.setAccessible(true);
                field.set(jwtTokenProvider, Base64.getEncoder().encodeToString(secretKey.getBytes()));
            }
            if (field.getName().equals("validityInMilliseconds")) {
                field.setAccessible(true);
                field.set(jwtTokenProvider, validityInMilliseconds);
            }
        }
    }

    @BeforeEach
    void setUp() {
        token = jwtTokenProvider.createToken(EMAIL, ROLES);
    }


    @Test
    void createToken_userData_ok() {
        assertNotNull(token);
    }

    @Test
    void getUsername_userToken_ok() {
        assertEquals(EMAIL, jwtTokenProvider.getUsername(token));
    }

    @Test
    void getUsername_noToken_notOk() {
        assertThrows(Exception.class, () -> jwtTokenProvider.getUsername("noToken"));
    }

    @Test
    void getAuthentication_userToken_ok() {
        when(userDetailsService.loadUserByUsername(any()))
                .thenReturn(Mockito.mock(UserDetails.class));
        Authentication actualAuth = jwtTokenProvider.getAuthentication(token);
        assertNotNull(actualAuth.getPrincipal());
    }

    @Test
    void getAuthentication_noToken_notOk() {
        assertThrows(Exception.class, () -> jwtTokenProvider.getAuthentication("noToken"));
    }

    @Test
    void resolveToken_mockRequest_ok() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getHeader("Authorization")).thenReturn("Bearer " + token);
        assertEquals(token, jwtTokenProvider.resolveToken(req));
    }

    @Test
    void resolveToken_mockRequest_notOk() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getHeader("Authorization")).thenReturn("12345");
        assertNotEquals(token, jwtTokenProvider.resolveToken(req));
    }

    @Test
    void validateToken_validToken_ok() {
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_inValidToken_ok() {
        assertEquals("Expired or invalid JWT token",
                assertThrows(Exception.class, () -> jwtTokenProvider.validateToken(INVALID_TOKEN)).getMessage());
    }
}
package mate.academy.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

class JwtTokenProviderTest {
    private static final String USER_EMAIL = "test@com.ua";
    private static final String USER_PASSWORD = "password";
    private static final String SECRET_KEY = "secretKey";
    private static final String VALIDITY_MILLISECONDS = "validityInMilliseconds";
    private static final String ROLE_USER = "USER";
    private static final String ROLE_ADMIN = "ADMIN";
    private static final Long MILLISECONDS = 3600000L;
    private static UserDetailsService userDetailsService;
    private static JwtTokenProvider jwtTokenProvider;
    private static String token;

    @BeforeAll
    static void setUp() throws NoSuchFieldException, IllegalAccessException {
        userDetailsService = mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        Field secretField = JwtTokenProvider.class.getDeclaredField(SECRET_KEY);
        secretField.setAccessible(true);
        secretField.set(jwtTokenProvider, SECRET_KEY);
        Field secondField = JwtTokenProvider.class.getDeclaredField(VALIDITY_MILLISECONDS);
        secondField.setAccessible(true);
        secondField.setLong(jwtTokenProvider, MILLISECONDS);
        token = jwtTokenProvider.createToken(USER_EMAIL, List.of(ROLE_USER, ROLE_ADMIN));
    }

    @Test
    void createToken() {
        assertNotNull(token);
    }

    @Test
    void getAuthentication_Ok() {
        UserDetails userDetails =
                new org.springframework.security.core.userdetails.User(USER_EMAIL, USER_PASSWORD,
                        Set.of());
        when(userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(userDetails);
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        assertNotNull(authentication);
        assertEquals(USER_EMAIL, authentication.getName());
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        String resolvedToken = jwtTokenProvider.resolveToken(request);
        assertNotNull(resolvedToken);
    }

    @Test
    void validateToken_Ok() {
        assertTrue(jwtTokenProvider.validateToken(token));
    }
}

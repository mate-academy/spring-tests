package mate.academy.security.jwt;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.jsonwebtoken.MalformedJwtException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String LOGIN = "bchupika@mate.academy";
    private static final String PASSWORD = "12345678";
    private static final String ROLE_USER = "USER";
    private static final String NON_UNIQUE_PART_OF_TOKEN = "eyJhbGciOiJIUzI1NiJ9.";
    private static final String TOKEN =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiY2h1cGlrYUBtYXRlLmFjYWRlbXkiLCJyb2xlcyI6WyJVU0VSIl0sImlhdCI6MTY1Mjg3OTk5NSwiZXhwIjoxNjUyODgzNTk1fQ.zVN6kPLSppKm0RxxTZ6OqRydRLcUavGeiruHg4ET6aQ";
    private static final String INVALID_TOKEN = "invalid.token.invalid";
    private HttpServletRequest req;
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        req = Mockito.mock(HttpServletRequest.class);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000L);
        jwtTokenProvider.init();
    }

    @Test
    void createToken_validData_ok() {
        String actual = jwtTokenProvider.createToken(LOGIN, List.of(ROLE_USER));
        assertNotNull(actual);
        assertEquals(NON_UNIQUE_PART_OF_TOKEN, actual.substring(0, 21));
    }

    @Test
    void getAuthentication_validData_ok() {
        UserDetails expectedUser =
                User.withUsername(LOGIN).password(PASSWORD).roles(ROLE_USER).build();
        Mockito.when(userDetailsService.loadUserByUsername(Mockito.anyString()))
                .thenReturn(expectedUser);
        Authentication actual = jwtTokenProvider.getAuthentication(TOKEN);
        User actualUser = (User) actual.getPrincipal();
        assertNotNull(actual);
        assertNotNull(actualUser.getAuthorities());
        assertEquals(expectedUser.getUsername(), actualUser.getUsername());
        assertEquals(expectedUser.getPassword(), actualUser.getPassword());
        assertEquals(expectedUser.getAuthorities(), actualUser.getAuthorities());
    }

    @Test
    void getUsername_validData_ok() {
        String actualUsername = jwtTokenProvider.getUsername(TOKEN);
        assertNotNull(actualUsername);
        assertEquals(LOGIN, actualUsername);
    }

    @Test
    void getUsername_invalidToken_notOk() {
        assertThrows(MalformedJwtException.class,
                () -> jwtTokenProvider.getUsername(INVALID_TOKEN));
    }

    @Test
    void resolveToken_validData_ok() {
        Mockito.when(req.getHeader("Authorization")).thenReturn("Bearer " + TOKEN);
        String actual = jwtTokenProvider.resolveToken(req);
        assertNotNull(actual);
        assertEquals(TOKEN, actual);
    }

    @Test
    void resolveToken_invalidToken_notOk() {
        Mockito.when(req.getHeader("Authorization")).thenReturn("Bearer " + INVALID_TOKEN);
        String actual = jwtTokenProvider.resolveToken(req);
        assertNotNull(actual);
        assertNotEquals(TOKEN, actual);
    }

    @Test
    void validateToken_validData_ok() {
        boolean actual = jwtTokenProvider.validateToken(TOKEN);
        assertNotNull(actual);
        assertTrue(actual);
    }

    @Test
    void validateToken_invalidToken_notOk() {
        assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(INVALID_TOKEN));
    }
}

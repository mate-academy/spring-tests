package mate.academy.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import mate.academy.security.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String SECRET_KEY = "secret-key";
    private static final long VALIDITY_IN_MILLISECONDS = 3600000;
    private static final String LOGIN = "user@i.ua";
    private static final String PASSWORD = "2345";
    private static final List<String> ROLES = List.of("USER");
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(CustomUserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                VALIDITY_IN_MILLISECONDS);
        jwtTokenProvider.init();
    }

    @Test
    void createToken_Ok() {
        String actual = jwtTokenProvider.createToken(LOGIN, ROLES);
        assertNotNull(actual);
        assertTrue(actual.length() > 0);
        assertEquals(3, actual.split("\\.").length);
    }

    @Test
    void getAuthentication_Ok() {
        UserBuilder builder = User.withUsername(LOGIN);
        builder.password(PASSWORD);
        builder.roles(ROLES.get(0));
        String token = jwtTokenProvider.createToken(LOGIN, ROLES);
        Mockito.when(userDetailsService.loadUserByUsername(jwtTokenProvider.getUsername(token)))
                .thenReturn(builder.build());
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        List<SimpleGrantedAuthority> expectedAuthorities = ROLES.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        assertNotNull(actual);
        assertEquals(LOGIN, actual.getName());
        assertEquals(ROLES.size(), actual.getAuthorities().size());
        assertTrue(actual.getAuthorities().containsAll(expectedAuthorities));
    }

    @Test
    void getUsername_Ok() {
        String token = jwtTokenProvider.createToken(LOGIN, ROLES);
        String actual = jwtTokenProvider.getUsername(token);
        assertNotNull(actual);
        assertEquals(LOGIN, actual);
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        String token = jwtTokenProvider.createToken(LOGIN, ROLES);
        Mockito.when(req.getHeader("Authorization")).thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(req);
        assertNotNull(actual);
        assertEquals(token, actual);
    }

    @Test
    void validateToken_Ok() {
        String token = jwtTokenProvider.createToken(LOGIN, ROLES);
        boolean actual = jwtTokenProvider.validateToken(token);
        assertTrue(actual);
    }

    @Test
    void validateToken_InvalidToken_NotOk() {
        String token = "random string";
        assertFalse(jwtTokenProvider.validateToken(token));
    }
}

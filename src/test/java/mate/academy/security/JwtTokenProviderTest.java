package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import mate.academy.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String LOGIN = "harrington@.ua";
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider,"validityInMilliseconds",
                3600000L);
    }

    @Test
    void createToken_ok() {
        String actual = jwtTokenProvider.createToken(LOGIN, new ArrayList<>());
        assertNotNull(actual);
    }

    @Test
    void getAuthentication_ok() {
        String token = jwtTokenProvider.createToken(LOGIN, new ArrayList<>());
        Role role = new Role(Role.RoleName.USER);
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role
                .getRoleName().name()));
        UserDetails details = new User(LOGIN, "12345", authorities);
        Mockito.doReturn(details).when(userDetailsService).loadUserByUsername(LOGIN);
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        assertNotNull(actual);
        assertEquals(authorities, actual.getAuthorities());
        assertEquals(details.getUsername(), actual.getName());
    }

    @Test
    void getUsername_ok() {
        String token = jwtTokenProvider.createToken(LOGIN, new ArrayList<>());
        String actual = jwtTokenProvider.getUsername(token);
        assertNotNull(token);
        assertEquals(LOGIN, actual);
    }

    @Test
    void resolveToken_ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn(null);
        String actual = jwtTokenProvider.resolveToken(request);
        assertNull(actual);
    }

    @Test
    void resolveToken_tokenIsNull_notOk() {
        String token = jwtTokenProvider.createToken(LOGIN, new ArrayList<>());
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(request);
        assertNotNull(actual);
        assertEquals(token, actual);
    }

    @Test
    void resolveToken_invalidToken_notOk() {
        String token = jwtTokenProvider.createToken(LOGIN, new ArrayList<>());
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn(token);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNull(actual);
    }

    @Test
    void validateToken_ok() {
        String token = jwtTokenProvider.createToken(LOGIN, new ArrayList<>());
        boolean actual = jwtTokenProvider.validateToken(token);
        assertTrue(actual);
    }

    @Test
    void validateToken_expiredToken_notOk() {
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                0);
        String token = jwtTokenProvider.createToken(LOGIN, new ArrayList<>());
        assertThrows(RuntimeException.class, () -> jwtTokenProvider.validateToken(token),
                "Should receive RuntimeException");
    }

    @Test
    void validateToken_invalidToken_notOk() {
        assertThrows(RuntimeException.class, () -> jwtTokenProvider.validateToken("nonsense"),
                "Should receive RuntimeException");
    }
}

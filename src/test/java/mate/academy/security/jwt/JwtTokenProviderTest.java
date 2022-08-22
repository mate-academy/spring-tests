package mate.academy.security.jwt;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
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
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;
    private String secretKey = "secret";
    private long validityInMilliseconds = 3600000L;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.spy(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtTokenProvider,"validityInMilliseconds",
                validityInMilliseconds);
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    @Test
    void init_ok() {
        jwtTokenProvider.init();
        Object actual = ReflectionTestUtils.getField(jwtTokenProvider, "secretKey");
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(secretKey, actual);
    }

    @Test
    void createToken_ok() {
        String actual = jwtTokenProvider.createToken("login", new ArrayList<>());
        Assertions.assertNotNull(actual);
    }

    @Test
    void getAuthentication_ok() {
        String token = jwtTokenProvider.createToken("login", new ArrayList<>());
        Role role = new Role(Role.RoleName.USER);
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role
                .getRoleName().name()));
        User user = new User("user", "1111", authorities);
        UserDetails details = new User(user.getUsername(), user.getPassword(), user.isEnabled(),
                user.isAccountNonExpired(), user.isCredentialsNonExpired(),
                user.isAccountNonLocked(), user.getAuthorities());
        Mockito.doReturn(details).when(userDetailsService).loadUserByUsername("login");
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(authorities, actual.getAuthorities());
        Assertions.assertEquals(details.getUsername(), actual.getName());
    }

    @Test
    void getUsername_ok() {
        String token = jwtTokenProvider.createToken("login", new ArrayList<>());
        String username = jwtTokenProvider.getUsername(token);
        Assertions.assertNotNull(token);
        Assertions.assertEquals("login", username);
    }

    @Test
    void resolveToken_ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn(null);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNull(actual);
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
    void resolveToken_invalidToken_notOk() {
        String token = jwtTokenProvider.createToken("login", new ArrayList<>());
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn(token);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNull(actual);
    }

    @Test
    void validateToken_ok() {
        String token = jwtTokenProvider.createToken("login", new ArrayList<>());
        Assertions.assertNotNull(token);
    }

    @Test
    void validateToken_expiredToken_notOk() {
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                -validityInMilliseconds);
        String token = jwtTokenProvider.createToken("login", new ArrayList<>());
        Throwable exception = Assertions.assertThrows(RuntimeException.class, () -> {
            jwtTokenProvider.validateToken(token);
        }, "RuntimeException was expected");
        Assertions.assertEquals("Expired or invalid JWT token", exception.getLocalizedMessage());
    }

    @Test
    void validateToken_invalidToken_notOk() {
        String token = jwtTokenProvider.createToken("login", new ArrayList<>());
        Throwable exception = Assertions.assertThrows(RuntimeException.class, () -> {
            jwtTokenProvider.validateToken(token.substring(1));
        }, "RuntimeException was expected");
        Assertions.assertEquals("Expired or invalid JWT token", exception.getLocalizedMessage());
    }

    @Test
    void validateToken_nullToken_notOk() {
        Throwable exception = Assertions.assertThrows(RuntimeException.class, () -> {
            jwtTokenProvider.validateToken(null);
        }, "RuntimeException was expected");
        Assertions.assertEquals("Expired or invalid JWT token", exception.getLocalizedMessage());
    }
}

package mate.academy.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import io.jsonwebtoken.MalformedJwtException;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;
    private HttpServletRequest request;
    private User user;
    private String token;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        request = Mockito.mock(HttpServletRequest.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000L);
        user = new User();
        user.setEmail("bob@i.ua");
        user.setPassword("1234");
        user.setRoles(Set.of(new Role(Role.RoleName.ADMIN), new Role(Role.RoleName.USER)));
        token = jwtTokenProvider.createToken(user.getEmail(), List.of("ADMIN", "USER"));
    }

    @Test
    void createToken_OK() {
        String actual = token;
        assertNotNull(actual);
        assertEquals(3, actual.split("\\.").length);
    }

    @Test
    void getAuthentication_OK() {
        UserBuilder builder = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail());
        builder.password(user.getPassword());
        builder.roles(user.getRoles()
                .stream()
                .map(x -> x.getRoleName().name())
                .toArray(String[]::new));
        Mockito.when(userDetailsService.loadUserByUsername(user.getEmail()))
                .thenReturn(builder.build());
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        assertNotNull(actual);
        assertEquals("bob@i.ua", actual.getName());
        assertEquals(2, actual.getAuthorities().size());
    }

    @Test
    void getUsername_OK() {
        String actual = jwtTokenProvider.getUsername(token);
        assertNotNull(actual);
        assertEquals("bob@i.ua", actual);
    }

    @Test
    void getUsername_nullToken_notOK() {
        assertThrows(IllegalArgumentException.class,() -> jwtTokenProvider.getUsername(null));
    }

    @Test
    void getUsername_badToken_notOK() {
        assertThrows(MalformedJwtException.class,() -> jwtTokenProvider.getUsername("123"));
    }

    @Test
    void resolveToken_OK() {
        Mockito.when(request.getHeader(any())).thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(request);
        assertNotNull(actual);
        assertEquals(3, actual.split("\\.").length);
        assertFalse(actual.startsWith("Bearer"));
    }

    @Test
    void resolveToken_notOK() {
        Mockito.when(request.getHeader("Authorization")).thenReturn(null);
        String actual = jwtTokenProvider.resolveToken(request);
        assertNull(actual);
    }

    @Test
    void validateToken_OK() {
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_notOK() {
        try {
            jwtTokenProvider.validateToken("token.token.token");
        } catch (RuntimeException e) {
            Assertions.assertEquals("Expired or invalid JWT token", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive Runtime Exception");
    }
}

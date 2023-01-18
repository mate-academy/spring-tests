package mate.academy.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String EMAIL = "user@gmail.com";
    private static final String PASSWORD = "12345";
    private static final String USER_ROLE_NAME = "USER";
    private static final Role role = new Role(Role.RoleName.USER);
    private static final List<String> ROLES = List.of(USER_ROLE_NAME);
    private static HttpServletRequest httpServletRequest;
    private static UserDetailsService userDetailsService;
    private static JwtTokenProvider jwtTokenProvider;

    @BeforeAll
    static void beforeAll() {
        httpServletRequest = mock(HttpServletRequest.class);
        userDetailsService = mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000L);
    }

    @Test
    void createToken_Ok() {
        assertNotNull(jwtTokenProvider.createToken(EMAIL, ROLES));
    }

    @Test
    void getAuthentication_Ok() {
        String token = jwtTokenProvider.createToken(EMAIL, ROLES);
        UserDetails userDetails = User.withUsername(EMAIL)
                .password(PASSWORD).roles(role.getRoleName().name()).build();
        when(userDetailsService.loadUserByUsername(EMAIL))
                .thenReturn(userDetails);
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        assertNotNull(authentication);
        assertTrue(authentication.isAuthenticated());
    }

    @Test
    void getUsername_Ok() {
        String token = jwtTokenProvider.createToken(EMAIL, ROLES);
        String actual = jwtTokenProvider.getUsername(token);
        assertEquals(EMAIL, actual);
    }

    @Test
    void resolveToken_Ok() {
        String token = jwtTokenProvider.createToken(EMAIL, ROLES);
        when(httpServletRequest.getHeader("Authorization"))
                .thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(httpServletRequest);
        assertNotNull(actual);
        assertEquals(token, actual);
    }

    @Test
    void resolveToken_NullToken_NotOk() {
        when(httpServletRequest.getHeader("Authorization"))
                .thenReturn(null);
        String actual = jwtTokenProvider.resolveToken(httpServletRequest);
        assertNull(actual);
    }

    @Test
    void validateToken_Ok() {
        String token = jwtTokenProvider.createToken(EMAIL, ROLES);
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_NotValidToken_NotOk() {
        String token = jwtTokenProvider.createToken(EMAIL, ROLES);
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(token + "!"));
        Assertions.assertEquals("Expired or invalid JWT token", exception.getMessage());
    }
}

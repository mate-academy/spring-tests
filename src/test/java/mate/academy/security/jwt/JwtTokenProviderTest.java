package mate.academy.security.jwt;

import static org.mockito.Mockito.mock;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String EMAIL = "bob@gmail.com";
    private static final List<String> ROLES = List.of(new Role(Role.RoleName.USER)
            .getRoleName().name());
    private static final String PASSWORD = "12345";
    private static JwtTokenProvider jwtTokenProvider;
    private static UserDetailsService userDetailsService;
    private static String token;

    @BeforeAll
    static void beforeAll() {
        userDetailsService = mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000L);
        token = jwtTokenProvider.createToken(EMAIL, ROLES);
    }

    @Test
    void createToken_Ok() {
        Assertions.assertNotNull(token);
    }

    @Test
    void getAuthentication_Ok() {
        User.UserBuilder builder = User.withUsername(EMAIL);
        builder.roles(ROLES.get(0));
        builder.username(EMAIL);
        builder.password(PASSWORD);
        Mockito.when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(builder.build());
        Assertions.assertNotNull(jwtTokenProvider.getAuthentication(token));
    }

    @Test
    void getUsername_Ok() {
        String username = jwtTokenProvider.getUsername(token);
        Assertions.assertEquals(EMAIL, username);
    }

    @Test
    void resolveTokenWithBearer_Ok() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
        String expected = "valid-token";
        String resolvedToken = jwtTokenProvider.resolveToken(request);
        Assertions.assertEquals(expected, resolvedToken);
    }

    @Test
    void resolveTokenWithoutBearer_Ok() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Error");
        String resolvedToken = jwtTokenProvider.resolveToken(request);
        Assertions.assertNull(resolvedToken);
    }

    @Test
    void validateToken_Ok() {
        boolean validateTrue = jwtTokenProvider.validateToken(token);
        Assertions.assertTrue(validateTrue);
    }

    @Test
    void validateToken_NotOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken("error"));
    }
}

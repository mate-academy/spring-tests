package mate.academy.security.jwt;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.security.CustomUserDetailsService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {
    private static final String USERNAME = "bob";
    private static final String PASSWORD = "1234";
    private static final Role ROLE_USER = new Role(Role.RoleName.USER);
    private static String TOKEN = "eyJhbGciOiJIUzI1NiJ9."
                                  + "eyJzdWIiOiJib2IiLCJyb2xlcyI6WyJVU0VSIl0sImlhdCI6MTY5MDI5NTM0NC"
                                  + "wiZXhwIjoxNjkwMjk4OTQ0fQ.HPFanDXSBuQWA-MMyS91wgs1Oz_KHhfSb"
                                  + "_ii9KoCpQs";
    private JwtTokenProvider jwtTokenProvider;

    private UserDetailsService userDetailsService;
    @Mock
    private UserService userService;
    @Mock
    private HttpServletRequest req;
    private Field secretKey;
    private Field validityInMilliseconds;

    @BeforeEach
    void setUp() {
        userDetailsService = new CustomUserDetailsService(userService);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        try {
            secretKey = jwtTokenProvider.getClass().getDeclaredField("secretKey");
            secretKey.setAccessible(true);
            secretKey.set(jwtTokenProvider, "secret");
            validityInMilliseconds = jwtTokenProvider.getClass()
                    .getDeclaredField("validityInMilliseconds");
            validityInMilliseconds.setAccessible(true);
            validityInMilliseconds.set(jwtTokenProvider, 3600000L);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createToken_validUser_Ok() {
        List<String> roles = List.of("USER");
        String actual = jwtTokenProvider.createToken(USERNAME, roles);
        assertNotNull(actual);
    }

    @Test
    void getAuthentication_validToken_Ok() {
        jwtTokenProvider = spy(jwtTokenProvider);
        User user = new User(USERNAME, PASSWORD,Set.of(ROLE_USER));
        doReturn(USERNAME).when(jwtTokenProvider).getUsername(TOKEN);
        when(userService.findByEmail(USERNAME)).thenReturn(Optional.of(user));
        Authentication authentication = jwtTokenProvider.getAuthentication(TOKEN);
        assertNotNull(authentication);
    }

    @Test
    void resolveToken_validToken_ok() {
        when(req.getHeader("Authorization")).thenReturn("Bearer " + TOKEN);
        String resolvedToken = jwtTokenProvider.resolveToken(req);
        assertNotNull(resolvedToken);
    }

    @Test
    void validateToken_expiredToken_notOk() {
        assertThrows(RuntimeException.class, () -> jwtTokenProvider.validateToken(TOKEN));
    }

    @Test
    void validateToken_invalidToken_notOk() {
        String invalidToken = "invalidToken";
        assertThrows(RuntimeException.class, () -> jwtTokenProvider.validateToken(invalidToken));
    }
}

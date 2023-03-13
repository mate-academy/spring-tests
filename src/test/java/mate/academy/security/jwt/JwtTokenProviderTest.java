package mate.academy.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String USER_EMAIL = "bob@gmail.com";
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000L);
    }

    @Test
    void createToken_Ok() {
        String actual = jwtTokenProvider.createToken(USER_EMAIL,
                List.of(Role.RoleName.USER.name()));
        assertNotNull(actual);
    }

    @Test
    void getUsername_Ok() {
        String token = jwtTokenProvider.createToken(USER_EMAIL, List.of(Role.RoleName.USER.name()));
        String actual = jwtTokenProvider.getUsername(token);
        assertNotNull(token);
        assertEquals(USER_EMAIL, actual);
    }

    @Test
    void validateToken_Ok() {
        String token = jwtTokenProvider.createToken(USER_EMAIL, List.of(Role.RoleName.USER.name()));
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_nullToken_NotOk() {
        assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(null));
    }
}

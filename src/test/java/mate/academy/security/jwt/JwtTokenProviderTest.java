package mate.academy.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String USER_EMAIL = "email@gmail.com";
    private static final String USER_PASSWORD = "password";
    private static final String ROLE_NAME = Role.RoleName.USER.name();
    private JwtTokenProvider underTest;
    private UserDetailsService userDetailsService;
    private String token;

    @BeforeEach
    void setUp() {
        userDetailsService = mock(UserDetailsService.class);
        underTest = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(underTest, "secretKey", "google");
        ReflectionTestUtils.setField(underTest, "validityInMilliseconds", 3600000L);
        token = underTest.createToken(USER_EMAIL, List.of(ROLE_NAME));
    }

    @Test
    void createTokenSuccess() {
        String actual = underTest.createToken(USER_EMAIL, List.of(ROLE_NAME));
        assertNotNull(actual);
        assertEquals(token, actual);
    }

    @Test
    void authenticationSuccess() {
        User.UserBuilder userBuilder = User.withUsername(USER_EMAIL);
        userBuilder.password(USER_PASSWORD);
        userBuilder.roles(ROLE_NAME);
        when(userDetailsService.loadUserByUsername(USER_EMAIL))
                .thenReturn(userBuilder.build());
        assertNotNull(underTest.getAuthentication(token));
    }

    @Test
    void getUserNameSuccess() {
        String username = underTest.getUsername(token);
        assertNotNull(username);
        assertEquals(USER_EMAIL, username);
    }

    @Test
    void resolveTokenSuccess() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getHeader("Authorization"))
                .thenReturn("Bearer " + token);
        String actual = underTest.resolveToken(req);
        assertNotNull(actual);
        assertEquals(token, actual);
    }

    @Test
    void resolveTokenException() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getHeader("Authorization"))
                .thenReturn("Bearer " + "IncorrectToken");
        String actual = underTest.resolveToken(req);
        assertEquals("IncorrectToken", actual);
    }

    @Test
    void validateTokenSuccess() {
        assertTrue(underTest.validateToken(token));
    }

    @Test
    void validateTokenException() {
        RuntimeException
                runtimeException = assertThrows(RuntimeException.class,
                    () -> underTest.validateToken("incorrect token"), "Expected RuntimeException");
        assertEquals("Expired or invalid JWT token", runtimeException.getLocalizedMessage());
    }
}

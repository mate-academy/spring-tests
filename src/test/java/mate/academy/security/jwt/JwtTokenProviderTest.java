package mate.academy.security.jwt;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String SECRET_KEY = "secret";
    private static final int VALIDITY_IN_MILLISECONDS = 3600000;
    private static final String DEFAULT_USERNAME = "mark@i.ua";
    private static final String DEFAULT_PASSWORD = "12345678";
    private static final String USER_ROLE = Role.RoleName.USER.name();
    private static final String ADMIN_ROLE = Role.RoleName.ADMIN.name();
    private String jwtToken;
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtTokenProvider,
                "validityInMilliseconds", VALIDITY_IN_MILLISECONDS);
        jwtToken = jwtTokenProvider.createToken(DEFAULT_USERNAME, List.of(USER_ROLE));
    }

    @Test
    void createToken_defaultAdmin_ok() {
        Assertions.assertNotNull(jwtTokenProvider
                .createToken(DEFAULT_USERNAME, List.of(ADMIN_ROLE)));
    }

    @Test
    void getAuthentication_defaultUser_ok() {
        UserBuilder userBuilder = User.withUsername(DEFAULT_USERNAME);
        userBuilder.password(DEFAULT_PASSWORD);
        userBuilder.roles(USER_ROLE);
        Mockito.when(userDetailsService.loadUserByUsername(DEFAULT_USERNAME))
                .thenReturn(userBuilder.build());
        Assertions.assertNotNull(jwtTokenProvider.getAuthentication(jwtToken));
    }

    @Test
    void getUsername_defaultByToken_ok() {
        Assertions.assertEquals(DEFAULT_USERNAME, jwtTokenProvider.getUsername(jwtToken));
    }

    @Test
    void resolveToken_validToken_ok() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);
        Assertions.assertEquals(jwtToken, jwtTokenProvider.resolveToken(req));
    }

    @Test
    void resolveToken_nullToken_notOk() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn("Bearer " + null);
        Assertions.assertNotEquals(jwtToken, jwtTokenProvider.resolveToken(req));
    }

    @Test
    void validateToken_validToken_ok() {
        assertTrue(jwtTokenProvider.validateToken(jwtToken));
    }

    @Test
    void validateToken_invalidToken_notOk() {
        Exception exception = assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken("Invalid jwt token"));
        String expectedMessage = "Expired or invalid JWT token";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validateToken_nullTokenException_notOk() {
        Exception exception = assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken("null"));
        Assertions.assertEquals(RuntimeException.class, exception.getClass());
    }
}

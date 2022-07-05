package mate.academy.security.jwt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String SECRET_KEY = "secret";
    private static final int VALIDITY_IN_MILLISECONDS = 3600000;
    private static final String LOGIN = "test@mail.ua";
    private static final String PASSWORD = "123456789";
    private static final List<String> ROLES = List.of("USER");
    private static String TOKEN;
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                VALIDITY_IN_MILLISECONDS);
        TOKEN = jwtTokenProvider.createToken(LOGIN, ROLES);
    }

    @Test
    void createToken_ok() {
        String actual = jwtTokenProvider.createToken(LOGIN, ROLES);
        Assertions.assertNotNull(actual);
    }

    @Test
    void getAuthentication_ok() {
        User.UserBuilder builder =
                org.springframework.security.core.userdetails.User.withUsername(LOGIN);
        builder.password(PASSWORD);
        builder.roles(ROLES.get(0));
        Mockito.when(userDetailsService.loadUserByUsername(jwtTokenProvider.getUsername(TOKEN)))
                .thenReturn(builder.build());
        Authentication authentication = jwtTokenProvider.getAuthentication(TOKEN);
        Assertions.assertNotNull(authentication);
    }

    @Test
    void getUsername_ok() {
        String username = jwtTokenProvider.getUsername(TOKEN);
        Assertions.assertNotNull(username);
    }

    @Test
    void resolveToken_ok() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn("Bearer " + TOKEN);
        String actual = jwtTokenProvider.resolveToken(req);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(TOKEN, actual);
    }

    @Test
    void validateToken_ok() {
        boolean actual = jwtTokenProvider.validateToken(TOKEN);
        Assertions.assertTrue(actual);
    }

    @Test
    void validateToken_invalidToken_notOk() {
        try {
            jwtTokenProvider.validateToken("invalid token");
        } catch (RuntimeException e) {
            Assertions.assertEquals("Expired or invalid JWT token", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive RuntimeException");
    }
}

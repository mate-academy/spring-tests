package mate.academy.security;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.security.jwt.JwtTokenProvider;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String USER_EMAIL = "bob@i.ua";
    private static final String USER_PASSWORD = "password";
    private static final String USER_ROLE = Role.RoleName.USER.name();
    private static final String SECRET_KEY = "secret";
    private static final long TOKEN_LIFETIME = 3600000L;
    private static final String TOKEN_SAMPLE = "eyJhbGciOiJIUzI1NiJ9"
            + ".eyJzdWIiOiJib2JAaS51YSIsInJvbGVzIjpbIlVTRVIiXSwiaWF0IjoxNjY4Mjg4MjM2LCJleHAiOj"
            + "M2MDAxNjY4Mjg4MjM2fQ.O36pG05hrEL8VNbXwlmvLIH-YBOcmkwEY4KYEUZ4bFw";
    private static final String TOKEN_EXPIRED_SAMPLE = "eyJhbGciOiJIUzI1NiJ9"
            + ".eyJzdWIiOiJib2JAaS51YSIsInJvbGVzIjpbIlVTRVIiXSwiaWF0IjoxNjY4MjgxNTE0LCJleHAiOjE"
            + "2NjgyODUxMTR9.68BgILyvcDHWnfr-p1usi9-6CvU9Ydjrg3iNg_ZTfW4";
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        jwtTokenProvider = Mockito.spy(jwtTokenProvider);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", TOKEN_LIFETIME);
    }

    @Test
    void createToken_Ok() {
        String actual = jwtTokenProvider.createToken(USER_EMAIL, List.of(USER_ROLE));
        Assertions.assertNotNull(actual);
    }

    @Test
    void getAuthentication_Ok() {
        User bob = new User();
        bob.setEmail(USER_EMAIL);
        bob.setPassword(USER_PASSWORD);
        bob.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.of(bob));
        Authentication actual = jwtTokenProvider.getAuthentication(TOKEN_SAMPLE);
        Assertions.assertNotNull(actual);
    }

    @Test
    void getUsername_Ok() {
        String actual = jwtTokenProvider.getUsername(TOKEN_SAMPLE);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, USER_EMAIL);
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization"))
                .thenReturn("Bearer " + TOKEN_SAMPLE);
        String actual = jwtTokenProvider.resolveToken(req);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, TOKEN_SAMPLE);
    }

    @Test
    void validateToken_Ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(TOKEN_SAMPLE));
    }

    @Test
    void validateToken_ExpiredToken_NotOk() {
        try {
            jwtTokenProvider.validateToken(TOKEN_EXPIRED_SAMPLE);
        } catch (RuntimeException e) {
            Assertions.assertEquals("Expired or invalid JWT token", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive JwtException or IllegalArgumentException!");
    }
}

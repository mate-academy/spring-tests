package mate.academy.security.jwt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String TEST_EMAIL = "bob@i.ua";
    private static final String TEST_PASSWORD = "password";
    private static final List<String> ROLES = List.of(Role.RoleName.USER.name());
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private String token;

    @BeforeEach
    void setUp() throws Exception {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret", String.class);
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                3600000L, long.class);
        token = jwtTokenProvider.createToken(TEST_EMAIL, ROLES);
    }

    @Test
    void createToken_ok() {
        Assertions.assertNotNull(jwtTokenProvider.createToken(TEST_EMAIL, ROLES));
    }

    @Test
    void getUsername_ok() {
        Assertions.assertEquals(TEST_EMAIL, jwtTokenProvider.getUsername(token),
                String.format("Expected user's name %s, but was %s",
                        TEST_EMAIL, jwtTokenProvider.getUsername(token)));
    }

    @Test
    void getAuthentication_ok() {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .roles(ROLES.get(0))
                .build();
        Mockito.when(userDetailsService.loadUserByUsername(TEST_EMAIL)).thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(TEST_EMAIL, actual.getName(),
                String.format("Expected user's name %s, but was %s",
                        TEST_EMAIL, actual.getName()));
    }

    @Test
    void resolveToken_ok() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(req);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(token, actual,
                String.format("Expected token should be %s, but was %s", token, actual));
    }

    @Test
    void resolveToken_nullBearerToken_notOk() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn(null);
        Assertions.assertNull(jwtTokenProvider.resolveToken(req));
    }

    @Test
    void resolveToken_notStartWithBearer_notOk() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn(token);
        Assertions.assertNull(jwtTokenProvider.resolveToken(req));
    }

    @Test
    void validateToken_ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_expiredToken_notOk() throws Exception {
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                -320000L, long.class);
        String invalidToken = jwtTokenProvider.createToken(TEST_EMAIL, ROLES);
        try {
            jwtTokenProvider.validateToken(invalidToken);
        } catch (RuntimeException e) {
            Assertions.assertEquals("Expired or invalid JWT token", e.getMessage());
            return;
        }
        Assertions.fail("Expected RuntimeException to be thrown");
    }

    @Test
    void validateToken_invalidToken_notOk() {
        Assertions.assertThrows(RuntimeException.class, () ->
                jwtTokenProvider.validateToken("invalidToken"),
                "Expected RuntimeException to be thrown");
    }
}

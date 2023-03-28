package mate.academy.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {
    @Mock
    private static UserDetailsService userDetailsService;
    private static JwtTokenProvider jwtTokenProvider;
    private static final String SECRET_KEY = "secretKey";
    private static final Long VALIDITY_IN_MILLISECONDS = 3_600_000L;
    private static final String TEST_EMAIL = "some.name@test.test";
    private static Date NOW;

    @BeforeAll
    static void beforeAll() {
        NOW = new Date();
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(
                jwtTokenProvider, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(
                jwtTokenProvider, "validityInMilliseconds", VALIDITY_IN_MILLISECONDS);
    }

    @Test
    void createToken_validInput_ok() {
        List<String> roles = List.of("USER");
        String expected = createToken(TEST_EMAIL, roles, NOW);
        String actual = jwtTokenProvider.createToken(TEST_EMAIL, roles);
        Assertions.assertEquals(expected, actual,
                "Should create valid token for login: " + TEST_EMAIL
                        + ", roles: " + roles
                        + ", time: " + NOW);
    }

    @Test
    void resolveToken_validInput_ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String bearerToken = "Bearer BIe6o.m3btFVU.vVgh0v";
        Mockito.when(request.getHeader("Authorization")).thenReturn(bearerToken);
        String expected = "BIe6o.m3btFVU.vVgh0v";
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertEquals(expected, actual,
                "Method should return " + expected
                        + " for request with Authorization header: " + bearerToken);
    }

    @Test
    void resolveToken_nullBearerToken_ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn(null);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNull(actual,
                "Method should return null for request with Authorization header: null");
    }

    @Test
    void validateToken_validToken_ok() {
        List<String> roles = List.of("USER");
        String token = createToken(TEST_EMAIL, roles, NOW);
        boolean actual = jwtTokenProvider.validateToken(token);
        Assertions.assertTrue(actual,
                "Method should return true for valid token created at: " + NOW
                        + " and validity: " + VALIDITY_IN_MILLISECONDS);
    }

    @Test
    void validateToken_expiredToken_notOk() {
        Date now = new Date(NOW.getTime() - VALIDITY_IN_MILLISECONDS - 1000L);
        List<String> roles = List.of("USER");
        String token = createToken(TEST_EMAIL, roles, now);
        Assertions.assertThrows(
                RuntimeException.class,
                () -> jwtTokenProvider.validateToken(token),
                "Method should throw RuntimeException for token created at: "
                        + now + " and validity: " + VALIDITY_IN_MILLISECONDS);
    }

    @Test
    void validateToken_invalidToken_notOk() {
        String token = "header.claims.sign";
        Assertions.assertThrows(
                RuntimeException.class,
                () -> jwtTokenProvider.validateToken(token),
                "Method should throw RuntimeException for invalid token: " + token);
    }

    private String createToken(String login, List<String> roles, Date now) {
        Claims claims = Jwts.claims().setSubject(login);
        claims.put("roles", roles);
        Date validity = new Date(now.getTime() + VALIDITY_IN_MILLISECONDS);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}

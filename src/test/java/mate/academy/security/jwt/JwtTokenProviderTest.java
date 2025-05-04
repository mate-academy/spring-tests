package mate.academy.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private String secretKey;
    private Long validityInMilliseconds;

    @BeforeEach
    public void setUp() {
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        secretKey = "secretKey";
        validityInMilliseconds = 3_600_000L;
        ReflectionTestUtils.setField(
                jwtTokenProvider, "secretKey", secretKey);
        ReflectionTestUtils.setField(
                jwtTokenProvider, "validityInMilliseconds", validityInMilliseconds);
    }

    @Test
    void createToken_validInput_ok() {
        String login = "valid@email.com";
        List<String> roles = List.of("USER");
        Date now = new Date();
        String expected = createToken(login, roles, now);
        String actual = jwtTokenProvider.createToken(login, roles);
        Assertions.assertEquals(expected, actual,
                "Method should create valid token for login %s, roles %s and time %s\n"
                        .formatted(login, roles, now));
    }

    @Test
    void resolveToken_validInput_ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String bearerToken = "Bearer BIe6o.m3btFVU.vVgh0v";
        Mockito.when(request.getHeader("Authorization")).thenReturn(bearerToken);
        String expected = "BIe6o.m3btFVU.vVgh0v";
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertEquals(expected, actual,
                "Method should return %s for request with Authorization header: %s\n"
                        .formatted(expected, bearerToken));
    }

    @Test
    void resolveToken_nullBearerToken_ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn(null);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNull(actual,
                "Method should return null for request with Authorization header: null\n");
    }

    @Test
    void validateToken_validToken_ok() {
        Date now = new Date();
        String login = "valid@email.com";
        List<String> roles = List.of("USER");
        String token = createToken(login, roles, now);
        boolean actual = jwtTokenProvider.validateToken(token);
        Assertions.assertTrue(actual,
                "Method should return true for valid token created at: %s and validity: %s\n"
                        .formatted(now, validityInMilliseconds));
    }

    @Test
    void validateToken_expiredToken_notOk() {
        Date now = new Date(new Date().getTime() - validityInMilliseconds - 1000L);
        String login = "valid@email.com";
        List<String> roles = List.of("USER");
        String token = createToken(login, roles, now);
        Assertions.assertThrows(
                RuntimeException.class,
                () -> jwtTokenProvider.validateToken(token),
                "Method should throw RuntimeException for token created at: %s and validity: %s\n"
                        .formatted(now, validityInMilliseconds));
    }

    @Test
    void validateToken_invalidToken_notOk() {
        String token = "Jbuvu.ldrKHtHlkNnGK.kblBLKJJLljh";
        Assertions.assertThrows(
                RuntimeException.class,
                () -> jwtTokenProvider.validateToken(token),
                "Method should throw RuntimeException for invalid token: " + token);
    }

    private String createToken(String login, List<String> roles, Date now) {
        Claims claims = Jwts.claims().setSubject(login);
        claims.put("roles", roles);
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}

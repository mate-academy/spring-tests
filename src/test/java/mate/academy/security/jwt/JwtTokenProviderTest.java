package mate.academy.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String SECRET_KEY = "secret";
    private static final long VALIDITY_IN_MILLISECONDS = 3600000;
    private String login;
    private List<String> roles;
    private String token;

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(Mockito.mock(UserDetailsService.class));
        ReflectionTestUtils.setField(jwtTokenProvider,
                                     JwtTokenProvider.class,
                                     "secretKey",
                                     Base64.getEncoder().encodeToString(SECRET_KEY.getBytes()),
                                     String.class);
        ReflectionTestUtils.setField(jwtTokenProvider,
                                     JwtTokenProvider.class,
                                     "validityInMilliseconds",
                                     VALIDITY_IN_MILLISECONDS,
                                     long.class);
        login = "bob@i.ua";
        roles = List.of("USER", "ADMIN");
        token = createToken(login, roles);
    }

    @Test
    void createToken_Ok() {
        String actual = jwtTokenProvider.createToken(login, roles);
        Assertions.assertNotNull(actual);
    }

    @Test
    void getUsername_Ok() {
        String actual = jwtTokenProvider.getUsername(token);
        Assertions.assertEquals(login, actual);
    }

    @Test
    void validateToken_Ok() {
        try {
            Assertions.assertTrue(jwtTokenProvider.validateToken(token));
        } catch (RuntimeException e) {
            Assertions.fail("Correct token shouldn't throw exception", e);
        }
    }

    @Test
    void validateToken_TokenCorrupted_NotOk() {
        String token = "token";
        try {
            Assertions.assertFalse(jwtTokenProvider.validateToken(token));
        } catch (RuntimeException e) {
            Assertions.assertEquals("Expired or invalid JWT token", e.getMessage());
            return;
        }
        Assertions.fail("Incorrect token should throw JwtException or IllegalArgumentException");
    }

    @Test
    void resolveToken_Ok() {
        String token = "Bearer " + this.token;

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader(Mockito.anyString())).thenReturn(token);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNotNull(actual);
    }

    @Test
    void resolveToken_BadValueOfAuthorizationHeaderInHttpServletRequest_NotOk() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader(Mockito.anyString())).thenReturn(token);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNull(actual, "Method resolveToken must return null");
    }

    private String createToken(String login, List<String> roles) {
        String secretKey = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
        Claims claims = Jwts.claims().setSubject(login);
        claims.put("roles", roles);
        Date now = new Date();
        Date validity = new Date(now.getTime() + VALIDITY_IN_MILLISECONDS);
        return Jwts.builder()
                   .setClaims(claims)
                   .setIssuedAt(now)
                   .setExpiration(validity)
                   .signWith(SignatureAlgorithm.HS256, secretKey)
                   .compact();
    }
}

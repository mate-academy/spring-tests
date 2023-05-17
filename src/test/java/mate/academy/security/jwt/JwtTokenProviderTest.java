package mate.academy.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JwtTokenProviderConfig.class})
class JwtTokenProviderTest {
    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds;
    private String login = "den@gmail.com";
    @Mock
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    protected String convert(String secretKey) {
        return Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    @Test
    void createToken_Ok() {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String actual = createToken();
        String[] dataFromToken = actual.split("\\.");
        String header = new String(decoder.decode(dataFromToken[0]));
        String payload = new String(decoder.decode(dataFromToken[1]));
        String actualLogin = payload.substring(payload.indexOf(":") + 2, payload.indexOf(",") - 1);
        Assertions.assertEquals(login, actualLogin);
    }

    @Test
    void getUsername_Ok() {
        String token = createToken();
        String actualLogin = jwtTokenProvider.getUsername(token);
        Assertions.assertEquals(login, actualLogin);
    }

    @Test
    void validateToken_Ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(createToken()));
    }

    @Test
    void validateToken_NotOk() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkZW5AZ21haWwuY29tIiwicm"
                + "9sZXMiOlsiVVNFUiJdLCJpYXQiOjE2ODAwMTU2NjEsImV4cCI6MTY4MDAxOTI2MX0.bp0G-eoF"
                + "23UUPxpnVQjL16dPmFIdH753Su7QTQ99UdQ";
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class,
                () -> {
                jwtTokenProvider.validateToken(token);
                }, "RuntimeExceptionException was expected");
        Assertions.assertEquals("Expired or invalid JWT token", exception.getMessage());
    }

    public String createToken() {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(Role.RoleName.USER));
        Claims claims = Jwts.claims().setSubject(login);
        claims.put("roles", roles);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, convert(secretKey))
                .compact();
    }
}

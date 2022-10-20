package mate.academy.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;
    private String secretKey = "secret";
    private long validityInMilliseconds = 3600000;
    private String jwt;
    private String login;

    @BeforeEach
    void setUp() {

        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtTokenProvider,
                "validityInMilliseconds", validityInMilliseconds);
        login = "bob";
        Claims claims = Jwts.claims().setSubject(login);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        jwt = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();

    }

    @Test
    void createToken_OK() {
        List<String> roles = new ArrayList<>();
        roles.add(Role.RoleName.USER.name());
        roles.add(Role.RoleName.ADMIN.name());
        String actual = jwtTokenProvider.createToken("bob",roles);
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(jwtTokenProvider.validateToken(actual));
    }

    @Test
    void getUsername_OK() {

        String actual = jwtTokenProvider.getUsername(jwt);
        Assertions.assertEquals(login,actual);
    }

    @Test
    void resolveToken_OK() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertEquals(actual,jwt);

    }

    @Test
    void validateToken_OK() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(jwt));
    }
}

package mate.academy.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Date;
import java.util.List;

class JwtTokenProviderTest {
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
    }

    @Test
    void createToken() {
        String login = "bob@i.ua";
        List<String> roles = List.of("USER");
        Date now = new Date();
        Claims claims = Jwts.claims().setSubject(login);
        claims.put("roles", roles);
        Date validity = new Date(now.getTime() + 3600000L);
        String expected = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, "superSecretKey")
                .compact();
        String actual = jwtTokenProvider.createToken(login, roles);
        Assertions.assertEquals(expected, actual,
                "Should create valid token for login %s, roles %s and time %s\n"
                        .formatted(login, roles, now));
    }

    @Test
    void getAuthentication() {
    }

    @Test
    void getUsername() {
    }

    @Test
    void resolveToken() {
    }

    @Test
    void validateToken() {
    }
}
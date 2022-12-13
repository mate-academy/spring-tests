package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class JwtTokenProviderTest {
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;
    private final String secret = Base64.getEncoder().encodeToString("secret".getBytes());
    private Long validity = 3600000L;
    private final String tokenBob = "eyJhbGciOiJIUzI1NiJ9."
            + "eyJzdWIiOiJib2JAZ21haWwuY29tIiwicm9sZXMiOlsiVVNFUiIsIk"
            + "FETUlOIl0sImlhdCI6MTY3MDg2NDgyNCwiZXhwIjoxNjcwODY4NDI0fQ"
            + ".cqAOKWUJ1OtxXlNq-p5n_Mmezc8TsSw3Uqjx9UdVFGg";

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        Field secretKey = null;
        Field validityInMilliseconds = null;
        try {
            secretKey = jwtTokenProvider.getClass().getDeclaredField("secretKey");
            secretKey.setAccessible(true);
            secretKey.set(jwtTokenProvider, secret);
            validityInMilliseconds = jwtTokenProvider.getClass()
                    .getDeclaredField("validityInMilliseconds");
            validityInMilliseconds.setAccessible(true);
            validityInMilliseconds.setLong(jwtTokenProvider, validity);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Can not get field: " + e.getMessage(), e);
        }
    }

    @Test
    void createToken_Ok() {
        String login = "bob@gmail.com";
        List<String> roles = List.of("USER", "ADMIN");
        String token = jwtTokenProvider.createToken(login, roles);
        assertNotNull(token);
        assertTrue(Jwts.parser().setSigningKey(secret).isSigned(token));
        System.out.println(token);
    }

    @Test
    void getAuthentication_Ok() {
        String login = "bob@gmail.com";
        String password = "12345678";
        String[] roles = {"USER", "ADMIN"};
        List<String> rolesList = List.of("USER", "ADMIN");
        UserDetails userDetails = User.withUsername(login)
                .password(password)
                .roles(roles).build();
        String validToken = createTestToken(login, rolesList, secret, validity);
        Mockito.when(userDetailsService.loadUserByUsername(login)).thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(validToken);
        assertNotNull(actual);
        org.springframework.security.core.userdetails.User actualUser =
                (User) actual.getPrincipal();
        Assertions.assertEquals(login, actualUser.getUsername());
        Assertions.assertEquals(2, actualUser.getAuthorities().size());
    }

    @Test
    void getAuthentication_notOk() {
        String login = "bob@gmail.com";
        String password = "12345678";
        List<String> rolesList = List.of("USER", "ADMIN");
        String validToken = createTestToken(login, rolesList, secret, validity);
        Mockito.when(userDetailsService.loadUserByUsername(login))
                .thenThrow(new UsernameNotFoundException("User not found."));
        try {
            jwtTokenProvider.getAuthentication(validToken);
        } catch (UsernameNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            return;
        }
        fail("Expected - UsernameNotFoundException: User not found.");
    }

    @Test
    void getUsername_tokenMatches_ok() {
        String expected = "bob@gmail.com";
        List<String> rolesList = List.of("USER", "ADMIN");
        String token = createTestToken(expected, rolesList, secret, validity);
        String actual = jwtTokenProvider.getUsername(token);
        assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getUsername_tokenDontMatch_notOk() {
        String expected = "notBob@gmail.com";
        List<String> rolesList = List.of("USER", "ADMIN");
        String token = createTestToken("bob@gmail.com", rolesList, secret, validity);
        String actual = jwtTokenProvider.getUsername(token);
        assertNotNull(actual);
        assertNotEquals(expected, actual);
    }

    @Test
    void resolveToken_checkBearer_Ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + tokenBob);
        String actual = jwtTokenProvider.resolveToken(request);
        assertNotNull(actual);
        Assertions.assertEquals(tokenBob, actual);
    }

    @Test
    void resolveToken_checkBearer_notOk() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("NotBearer " + tokenBob);
        String actual = jwtTokenProvider.resolveToken(request);
        assertNull(actual);
    }

    @Test
    void validateToken_validToken_Ok() {
        String login = "bob@gmail.com";
        List<String> roles = List.of("USER", "ADMIN");
        String validToken = createTestToken(login, roles, secret, validity);
        assertTrue(jwtTokenProvider.validateToken(validToken));
    }

    @Test
    void validateToken_invalidToken_notOk() {
        String login = "bob@gmail.com";
        List<String> roles = List.of("USER", "ADMIN");
        String validToken = createTestToken(login, roles, secret, -5000L);
        try {
            jwtTokenProvider.validateToken(validToken);
        } catch (RuntimeException e) {
            Assertions.assertEquals("Expired or invalid JWT token", e.getMessage());
            return;
        }
        fail("Expected - RuntimeException: Expired or invalid JWT token");
    }

    private String createTestToken(String login, List<String> roles,
                                   String secretKey, Long validityInMilliseconds) {
        Claims claims = Jwts.claims().setSubject(login);
        claims.put("roles", roles);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}

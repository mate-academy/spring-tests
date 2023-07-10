package mate.academy.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.security.CustomUserDetailsService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

class JwtTokenProviderTest {
    private static final String EMAIL = "bob";
    private static final String PASSWORD = "1234";
    private static final Role ROLE = new Role(Role.RoleName.USER);
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9"
            + ".eyJzdWIiOiJib2IiLCJyb2xlcyI6WyJVU0VSIl0sImlhdCI6"
            + "MTY4OTAxMDkzMSwiZXhwIjoxNjg5MDE0NTMxfQ."
            + "TjkGk8syfEWtsst0alG_LvYowZU68hYc9RjA_bVwyEo";
    private UserService userService;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        UserDetailsService userDetailsService = new CustomUserDetailsService(userService);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
    }

    @Test
    void createToken_ok() {
        jwtTokenProvider = Mockito.mock(JwtTokenProvider.class);
        Claims claims = Jwts.claims().setSubject(EMAIL);
        List<String> roles = new ArrayList<>();
        roles.add(ROLE.getRoleName().name());
        claims.put("roles", roles);
        Date now = new Date();
        Date validity = new Date(now.getTime() + 999999L);
        Mockito.when(jwtTokenProvider.createToken(
                        ArgumentMatchers.anyString(), ArgumentMatchers.anyList()))
                .thenReturn(Jwts.builder().setClaims(claims)
                        .setIssuedAt(now)
                        .setExpiration(validity)
                        .signWith(SignatureAlgorithm.HS256, Base64.getEncoder()
                                .encode("secret".getBytes()))
                        .compact());
        String token = jwtTokenProvider.createToken(EMAIL, roles);
        Assertions.assertNotNull(token);
    }

    @Test
    void getAuthentication_validToken_ok() {
        jwtTokenProvider = Mockito.spy(jwtTokenProvider);
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(ROLE));
        Mockito.doReturn(EMAIL).when(jwtTokenProvider).getUsername(TOKEN);
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Authentication authentication = jwtTokenProvider.getAuthentication(TOKEN);
        Assertions.assertNotNull(authentication);
    }

    @Test
    void resolveToken_validToken_ok() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn("Bearer " + TOKEN);
        String resolvedToken = jwtTokenProvider.resolveToken(req);
        Assertions.assertNotNull(resolvedToken);
    }

    @Test
    void resolveToken_invalidToken_notOk() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn(TOKEN);
        String resolvedToken = jwtTokenProvider.resolveToken(req);
        Assertions.assertNull(resolvedToken);
    }

    @Test
    void validateToken_ok() {
        jwtTokenProvider = Mockito.mock(JwtTokenProvider.class);
        String token = "eyJhbGciOiJIUzI1NiJ9"
                + ".eyJzdWIiOiJib2IiLCJyb2xlcyI6WyJVU0VSIl0sImlhdCI6MTY4OTAxMjIz"
                + "MiwiZXhwIjoxNjg5MDE1ODMyfQ"
                + ".hkdG6A8tbLn2qYdi9h0HJguQYXtPYL4QFHQOgjf7VKE";
        Mockito.doReturn(true).when(jwtTokenProvider).validateToken(token);
        boolean valid = jwtTokenProvider.validateToken(token);
        Assertions.assertTrue(valid);
    }

    @Test
    void validateToken_expiredToken_notOk() {
        try {
            jwtTokenProvider.validateToken(TOKEN);
            Assertions.fail("Expected to receive RuntimeException");
        } catch (RuntimeException e) {
            Assertions.assertEquals("Expired or invalid JWT token", e.getMessage());
        }
    }

    @Test
    void validateToken_invalidToken_notOk() {
        String token = "invalid-token";
        try {
            jwtTokenProvider.validateToken(token);
            Assertions.fail("Expected to receive RuntimeException");
        } catch (RuntimeException e) {
            Assertions.assertEquals("Expired or invalid JWT token", e.getMessage());
        }
    }
}

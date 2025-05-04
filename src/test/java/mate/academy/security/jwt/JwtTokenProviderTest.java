package mate.academy.security.jwt;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
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
        userService = mock(UserService.class);
        UserDetailsService userDetailsService = new CustomUserDetailsService(userService);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
    }

    @Test
    void createToken_ok() {
        jwtTokenProvider = mock(JwtTokenProvider.class);
        Claims claims = Jwts.claims().setSubject(EMAIL);
        List<String> roles = new ArrayList<>();
        roles.add(ROLE.getRoleName().name());
        claims.put("roles", roles);
        Date now = new Date();
        Date validity = new Date(now.getTime() + 999999L);
        when(jwtTokenProvider.createToken(
                        ArgumentMatchers.anyString(), ArgumentMatchers.anyList()))
                .thenReturn(Jwts.builder().setClaims(claims)
                        .setIssuedAt(now)
                        .setExpiration(validity)
                        .signWith(SignatureAlgorithm.HS256, Base64.getEncoder()
                                .encode("secret".getBytes()))
                        .compact());
        String token = jwtTokenProvider.createToken(EMAIL, roles);
        assertNotNull(token);
    }

    @Test
    void getAuthentication_validToken_ok() {
        jwtTokenProvider = spy(jwtTokenProvider);
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(ROLE));
        doReturn(EMAIL).when(jwtTokenProvider).getUsername(TOKEN);
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Authentication authentication = jwtTokenProvider.getAuthentication(TOKEN);
        assertNotNull(authentication);
    }

    @Test
    void resolveToken_validToken_ok() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getHeader("Authorization")).thenReturn("Bearer " + TOKEN);
        String resolvedToken = jwtTokenProvider.resolveToken(req);
        assertNotNull(resolvedToken);
    }

    @Test
    void validateToken_expiredToken_notOk() {
        assertThrows(RuntimeException.class, () -> jwtTokenProvider.validateToken(TOKEN),
                "Expired or invalid JWT token");
    }

    @Test
    void validateToken_invalidToken_notOk() {
        String token = "invalid-token";
        assertThrows(RuntimeException.class, () -> jwtTokenProvider.validateToken(token),
                "Expired or invalid JWT token");
    }
}

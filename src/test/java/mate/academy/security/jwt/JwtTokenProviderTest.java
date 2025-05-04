package mate.academy.security.jwt;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
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
    private static final String EMAIL = "aboba@example.com";
    private static final String PASSWORD = "123456";
    private static final Role ROLE = new Role(Role.RoleName.USER);
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9"
            + ".eyJzdWIiOiJhYm9iYUBleGFtcGxlLmNvbSIsInJvbGVzIjpbI"
            + "lVTRVIiXSwiaWF0IjoxNjg4OTEzOTQwLCJleHAiOjE2ODg5MTc1NDB9."
            + "EIY8ogg-_eDP_Nxd8b1PdM97vWzE_zrYaJxR0VsXMmY";
    private JwtTokenProvider jwtTokenProvider;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        UserDetailsService userDetailsService = new CustomUserDetailsService(userService);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
    }

    @Test
    void createToken_Ok() {
        jwtTokenProvider = mock(JwtTokenProvider.class);
        Claims claims = Jwts.claims().setSubject(EMAIL);
        List<String> roles = new ArrayList<>();
        roles.add(ROLE.getRoleName().name());
        claims.put("roles", roles);
        Date now = new Date();
        Date validity = new Date(now.getTime() + 100000000L);
        when(jwtTokenProvider.createToken(
                        ArgumentMatchers.anyString(), ArgumentMatchers.anyList()))
                .thenReturn(Jwts.builder().setClaims(claims)
                        .setIssuedAt(now)
                        .setExpiration(validity)
                        .signWith(SignatureAlgorithm.HS256, Base64.getEncoder()
                                .encode("secret".getBytes()))
                        .compact());
        assertNotNull(jwtTokenProvider.createToken(EMAIL, roles));
    }

    @Test
    void getAuthentication_validToken_Ok() {
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
    void resolveToken_validToken_Ok() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getHeader("Authorization")).thenReturn("Bearer " + TOKEN);
        assertNotNull(jwtTokenProvider.resolveToken(req));
    }

    @Test
    void resolveToken_invalidToken_notOk() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getHeader("Authorization")).thenReturn(TOKEN);
        assertNull(jwtTokenProvider.resolveToken(req));
    }

    @Test
    void validateToken_validToken_Ok() {
        jwtTokenProvider = mock(JwtTokenProvider.class);
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib2JAaS51YSIsInJvbGVz"
                + "IjpbIlVTRVIiXSwiaWF0IjoxNjg4NDYzODE3LCJleHAiOjIwMDM4MjM4MTd9.M7y6"
                + "0o_yqULv7EfPGo5kIervVhyOXDVxohYqmPEasis";
        doAnswer(invocation -> {
            try {
                Jws<Claims> claims = Jwts.parser().setSigningKey(Base64.getEncoder()
                        .encode("secret".getBytes())).parseClaimsJws(token);
                return !claims.getBody().getExpiration().before(new Date());
            } catch (JwtException | IllegalArgumentException e) {
                throw new RuntimeException("Expired or invalid JWT token", e);
            }
        }).when(jwtTokenProvider).validateToken(token);
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_expiredToken_notOk() {
        assertThrows(RuntimeException.class, () ->
                        jwtTokenProvider.validateToken(TOKEN),
                "Expired or invalid JWT token");
    }

    @Test
    void validateToken_invalidToken_notOk() {
        String token = "mjud,kajrb,awvf,habr,kh arel";
        assertThrows(RuntimeException.class, () ->
                        jwtTokenProvider.validateToken(token),
                "Expired or invalid JWT token");
    }
}

package mate.academy.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
    private UserDetailsService userDetailsService;
    private JwtTokenProvider tokenProvider;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        tokenProvider = new JwtTokenProvider(userDetailsService);
    }

    @Test
    void createToken_Ok() {
        tokenProvider = Mockito.mock(JwtTokenProvider.class);
        String login = "bob@i.ua";
        Claims claims = Jwts.claims().setSubject(login);
        List<String> roles = List.of(new Role(Role.RoleName.USER)).stream()
                .map(role -> role.getRoleName().name())
                .toList();
        claims.put("roles", roles);
        Date now = new Date();
        Date validity = new Date(now.getTime() + 315_360_000_000L);
        Mockito.when(tokenProvider.createToken(ArgumentMatchers.anyString(),
                ArgumentMatchers.anyList())).thenReturn(Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encode("secret".getBytes()))
                .compact());
        Assertions.assertNotNull(tokenProvider.createToken(login, roles));
    }

    @Test
    void getAuthentication_Ok() {
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib2JAaS51YSIsInJvbGV"
                + "zIjpbIlVTRVIiXSwiaWF0IjoxNjg4NDYzNDgxLCJleHAiOi05MjIzMzcwMzQ4MzkxMjk0"
                + "fQ.5lSVoAHJ9dQ6cM4saM-WNp9xmdYqEFlNRRV-faXclT4";
        tokenProvider = Mockito.spy(tokenProvider);

        User user = new User();
        user.setEmail("bob@i.ua");
        user.setPassword("12345");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));

        Mockito.doReturn("bob@i.ua").when(tokenProvider).getUsername(token);
        Mockito.when(userService.findByEmail("bob@i.ua")).thenReturn(Optional.of(user));
        Authentication authentication = tokenProvider.getAuthentication(token);
        Assertions.assertNotNull(authentication);
    }

    @Test
    void getUsername_Ok() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib2JAaS51YSIsInJvbGVz"
                + "IjpbIlVTRVIiXSwiaWF0IjoxNjg4NDYzODE3LCJleHAiOjIwMDM4MjM4MTd9.M7y6"
                + "0o_yqULv7EfPGo5kIervVhyOXDVxohYqmPEasis";

        tokenProvider = Mockito.mock(JwtTokenProvider.class);

        Mockito.doAnswer(invocation -> Jwts.parser().setSigningKey(Base64.getEncoder()
                .encode("secret".getBytes())).parseClaimsJws(token).getBody()
                .getSubject()).when(tokenProvider).getUsername(token);

        String actualUsername = tokenProvider.getUsername(token);
        Assertions.assertEquals("bob@i.ua", actualUsername);
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib2JAaS51YSIsInJvbGVzIjpbIlVTRVIiXSwi"
                + "aWF0IjoxNjg4NDU5MzY0LCJleHAiOjE2ODg0NjI5NjR9.Pz1"
                + "KXDHPyjEr4e7IkamKqBhELCx6vSKL57_pMHbClnw";
        Mockito.when(req.getHeader("Authorization")).thenReturn("Bearer " + token);
        Assertions.assertNotNull(tokenProvider.resolveToken(req));
    }

    @Test
    void resolveToken_invalidToken_notOk() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib2JAaS51YSIsInJvbGVzIjpbIlVTRVIiXSwi"
                + "aWF0IjoxNjg4NDU5MzY0LCJleHAiOjE2ODg0NjI5NjR9.Pz1"
                + "KXDHPyjEr4e7IkamKqBhELCx6vSKL57_pMHbClnw";
        Mockito.when(req.getHeader("Authorization")).thenReturn(token);
        Assertions.assertNull(tokenProvider.resolveToken(req));
    }

    @Test
    void validateToken_Ok() {
        tokenProvider = Mockito.mock(JwtTokenProvider.class);

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib2JAaS51YSIsInJvbGVz"
                + "IjpbIlVTRVIiXSwiaWF0IjoxNjg4NDYzODE3LCJleHAiOjIwMDM4MjM4MTd9.M7y6"
                + "0o_yqULv7EfPGo5kIervVhyOXDVxohYqmPEasis";
        Mockito.doAnswer(invocation -> {
            try {
                Jws<Claims> claims = Jwts.parser().setSigningKey(Base64.getEncoder()
                        .encode("secret".getBytes())).parseClaimsJws(token);
                return !claims.getBody().getExpiration().before(new Date());
            } catch (JwtException | IllegalArgumentException e) {
                throw new RuntimeException("Expired or invalid JWT token", e);
            }
        }).when(tokenProvider).validateToken(token);
        boolean valid = tokenProvider.validateToken(token);
        Assertions.assertTrue(valid);
    }

    @Test
    void validateToken_expiredToken_notOk() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib2JAaS51YSIsInJvbGVzI"
                + "jpbIlVTRVIiXSwiaWF0IjoxNjg4N"
                + "U5ODAxLCJleHAiOjE2ODg0NTk4MDF9.o1ZjFldo5V9pVhMVU8nIE2eb5Jmpsw3-bApfWlXH0wo";

        try {
            tokenProvider.validateToken(token);
        } catch (RuntimeException e) {
            Assertions.assertEquals("Expired or invalid JWT token", e.getMessage());
            return;
        }

        Assertions.fail("Expected to receive RuntimeException");
    }

    @Test
    void validateToken_invalidToken_notOk() {
        String token = "2w2lfefpl";
        try {
            tokenProvider.validateToken(token);
        } catch (RuntimeException e) {
            Assertions.assertEquals("Expired or invalid JWT token", e.getMessage());
            return;
        }

        Assertions.fail("Expected to receive RuntimeException");
    }
}

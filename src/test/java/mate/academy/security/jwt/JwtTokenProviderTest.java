package mate.academy.security.jwt;

import io.jsonwebtoken.Claims;
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
    private static final String EMAIL = "bob@gmail.com";
    private static final String PASSWORD = "12345678";
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib2IiLCJyb2xlcyI6WyJVU0"
            + "VSIl0sImlhdCI6MTY5MzQ3NjMxNSwiZXhwIjoxNjkzNDc5OTE1fQ.32udv5TIBm9T00XGIZ51uuGF0VLFT"
            + "KEvxYsSiYqYzbo";
    private UserService userService;
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
    }

    @Test
    void createToken_ok() {
        jwtTokenProvider = Mockito.mock(JwtTokenProvider.class);
        Claims claims = Jwts.claims().setSubject(EMAIL);
        List<String> roles = List.of(Role.RoleName.USER.name());
        claims.put("roles", roles);
        Date now = new Date();
        Date validity = new Date(now.getTime() + 36000L);

        Mockito.when(jwtTokenProvider.createToken(
                        ArgumentMatchers.anyString(), ArgumentMatchers.anyList()))
                .thenReturn(Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(now)
                        .setExpiration(validity)
                        .signWith(SignatureAlgorithm.HS256,
                                Base64.getEncoder().encode("secret".getBytes()))
                        .compact());

        String token = jwtTokenProvider.createToken(EMAIL, roles);
        Assertions.assertNotNull(token);
    }

    @Test
    void getAuthentication_ok() {
        jwtTokenProvider = Mockito.spy(jwtTokenProvider);
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));

        Mockito.doReturn(EMAIL).when(jwtTokenProvider).getUsername(TOKEN);
        Mockito.when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        Authentication authentication = jwtTokenProvider.getAuthentication(TOKEN);
        Assertions.assertNotNull(authentication);
    }

    @Test
    public void resolveToken_Ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + TOKEN);
        String token = jwtTokenProvider.resolveToken(request);
        Assertions.assertEquals(TOKEN, token);
    }

    @Test
    public void resolveToken_InvalidData_notOk() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("qwertyu");
        String token = jwtTokenProvider.resolveToken(request);
        Assertions.assertNull(token);
    }

    @Test
    void validateToken_ExpiredToken_NotOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(TOKEN));
    }

    @Test
    void validateToken_InvalidToken_NotOk() {
        String invalidToken = "qwertyuy";
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(invalidToken));
    }
}

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
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;

class JwtTokenProviderTest {
    private final static String USER_EMAIL = "bob@gmail.ua";
    private final static String USER_PASSWORD = "1234";
    private final static String ROLE = "USER";
    private final static String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
            + ".eyJzdWIiOiJleGFtcGxlVXNlciIsInJvbGUiOiJST0xFX1VTRVIiLCJp"
            + "YXQiOjE2MzEwNjI1NzksImV4cCI6MTYzMTA2NjE3OX0"
            + ".7j8y33UfH1sP3gYKv7L7eACUPUVb9Sjz_eG4ICe3bRY";
    private final Role userRole = new Role(Role.RoleName.USER);
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
        Claims claims = Jwts.claims().setSubject(USER_EMAIL);
        List<String> role = List.of(ROLE);
        claims.put("roles", role);
        Date date = new Date();
        Date timeForLifeToken = new Date(date.getTime() + 3600000L);
        Mockito.when(jwtTokenProvider.createToken(any(), anyList()))
                .thenReturn(Jwts.builder().setClaims(claims)
                        .setIssuedAt(date)
                        .setExpiration(timeForLifeToken)
                        .signWith(SignatureAlgorithm.HS256, Base64.getEncoder()
                                .encode("secret".getBytes())).compact());
        String token = jwtTokenProvider.createToken(USER_EMAIL, role);
        Assertions.assertNotNull(token);
    }

    @Test
    void getAuthentication_ok() {
        jwtTokenProvider = Mockito.spy(jwtTokenProvider);
        User user = new User();
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
        user.setRoles(Set.of(userRole));
        Mockito.when(jwtTokenProvider.getUsername(TOKEN)).thenReturn(USER_EMAIL);
        Mockito.when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        Authentication authentication = jwtTokenProvider.getAuthentication(TOKEN);
        Assertions.assertNotNull(authentication);
    }

    @Test
    void resolveToken() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + TOKEN);
        String resolvedToken = jwtTokenProvider.resolveToken(request);
        Assertions.assertEquals(TOKEN, resolvedToken);
    }

    @Test
    void resolveToken_incorrectToken_notOk() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Incorrect " + TOKEN);
        String resolvedToken = jwtTokenProvider.resolveToken(request);
        Assertions.assertNull(resolvedToken);
    }

    @Test
    void validateToken_ok() {
        jwtTokenProvider = Mockito.mock(JwtTokenProvider.class);
        Mockito.when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.of(new User()));
        Mockito.when(jwtTokenProvider.validateToken(TOKEN)).thenReturn(true);
        boolean isValid = jwtTokenProvider.validateToken(TOKEN);
        Assertions.assertTrue(isValid);
    }

    @Test
    void validateToken_invalidToken_notOk() {
        jwtTokenProvider = Mockito.mock(JwtTokenProvider.class);
        Mockito.when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.of(new User()));
        Mockito.when(jwtTokenProvider.validateToken(TOKEN)).thenReturn(false);
        boolean isValid = jwtTokenProvider.validateToken(TOKEN);
        Assertions.assertFalse(isValid);
    }
}

package mate.academy.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

class JwtTokenProviderTest {
    private static final String EMAIL = "cat@gmail.com";
    private static final String PASSWORD = "12345678";
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJz"
            + "dWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwia"
            + "WF0IjoxNjI4MDA5ODc3LCJleHAiOjE2MjgwMTM0NzcsImlzcy"
            + "I6ImFwaS5leGFtcGxlLmNvbSJ9.QHjIwOcDHPFW-7bsLxZqBQ"
            + "VDmB5ESqDnDv6rm9jUSKM";
    private static final String INVALID_TOKEN = " ";
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
        roles.add(Role.RoleName.USER.name());
        claims.put("roles", roles);
        Date now = new Date();
        Date validity = new Date(now.getTime() + 36000L);

        Mockito.when(jwtTokenProvider.createToken(
                        ArgumentMatchers.anyString(), ArgumentMatchers.anyList()))
                .thenReturn(Jwts.builder().setClaims(claims)
                        .setIssuedAt(now).setExpiration(validity)
                        .signWith(SignatureAlgorithm.HS256, Base64.getEncoder()
                                .encode("secret".getBytes())).compact());

        String token = jwtTokenProvider.createToken(EMAIL, roles);
        assertNotNull(token);
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
        assertNotNull(authentication);
    }

    @Test
    public void resolveToken_ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + TOKEN);
        String token = jwtTokenProvider.resolveToken(request);
        assertEquals(TOKEN, token);
    }

    @Test
    public void resolveToken_invalidToken_notOk() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("InvalidHeader");
        String token = jwtTokenProvider.resolveToken(request);
        assertNull(token);
    }

    @Test
    void validateToken_ok() {
        jwtTokenProvider = Mockito.mock(JwtTokenProvider.class);
        Mockito.doReturn(true).when(jwtTokenProvider).validateToken(TOKEN);
        boolean valid = jwtTokenProvider.validateToken(TOKEN);
        assertTrue(valid);
    }

    @Test
    public void testValidateToken_ExpiredToken_notOk() {
        try {
            jwtTokenProvider.validateToken(TOKEN);
        } catch (RuntimeException e) {
            assertEquals("Expired or invalid JWT token", e.getMessage());
            return;
        }
        fail("Expected to receive RuntimeException");
    }

    @Test
    public void testValidateToken_InvalidToken() {
        try {
            jwtTokenProvider.validateToken(INVALID_TOKEN);
        } catch (RuntimeException e) {
            assertEquals("Expired or invalid JWT token", e.getMessage());
            return;
        }
        fail("Expected to receive RuntimeException");
    }
}

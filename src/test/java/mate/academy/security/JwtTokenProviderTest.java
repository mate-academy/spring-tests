package mate.academy.security;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import mate.academy.model.Role;
import mate.academy.security.jwt.JwtTokenProvider;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;
import static org.mockito.ArgumentMatchers.any;

public class JwtTokenProviderTest {
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void setup() {
        UserService userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        jwtTokenProvider = Mockito.spy(jwtTokenProvider);
        String expectedSecretKey = Base64.getEncoder().encodeToString("secret".getBytes());
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", expectedSecretKey);
    }

    @Test
    void createToken_ok() {
        String login = "testUser";
        List<String> roles = Arrays.asList("ADMIN", "USER");
        String token = jwtTokenProvider.createToken(login, roles);
        Assertions.assertNotNull(token);
        Assertions.assertTrue(token.length() > 0);
    }

    @Test
    void getAuthentication_ok() {
        String token = "checkToken";
        String email = "tom@i.com";
        User.UserBuilder builder;
        builder = User.withUsername(email);
        builder.password("1234");
        builder.roles(Role.RoleName.USER.name());
        UserDetails userDetails = builder.build();
        Mockito.when(userDetailsService.loadUserByUsername(any())).thenReturn(userDetails);
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        Assertions.assertNotNull(authentication);
        Assertions.assertEquals(userDetails, authentication.getPrincipal());
        Assertions.assertEquals("", authentication.getCredentials());
        Assertions.assertEquals(userDetails.getAuthorities(), authentication.getAuthorities());
    }

    @Test
    void getUsername_ok() {
        String token = "checkToken";
        Claims claims = Jwts.claims().setSubject("tom.brown");
        Mockito.when(Jwts.parser()
                .setSigningKey(String.valueOf(any()))
                .parseClaimsJws(token))
                .thenReturn((Jws<Claims>) claims);

        String username = jwtTokenProvider.getUsername(token);

        Assertions.assertNotNull(username);
        Assertions.assertEquals("tom.brown", username);
    }

    @Test
    void resolveToken_ok() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String token = "validToken";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        String result = jwtTokenProvider.resolveToken(request);

        assertEquals(token, result);
    }

    @Test
    void resolveToken_notOk() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(null);

        String result = jwtTokenProvider.resolveToken(request);

        assertNull(result);
    }

    @Test
    void validateToken_ok() {
        @Test
        public void validateToken_WithValidToken_ReturnsTrue() {
            String token = "validToken";
            Date expiration = new Date(System.currentTimeMillis() + 1000);
            Jws<Claims> claims = mock(Jws.class);
            Claims claimsBody = mock(Claims.class);
            when(claims.getBody()).thenReturn(claimsBody);
            when(claimsBody.getExpiration()).thenReturn(expiration);
            when(Jwts.parser().setSigningKey(any()).parseClaimsJws(token
    }
}

package mate.academy.security;

import static org.mockito.ArgumentMatchers.any;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import mate.academy.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

public class JwtTokenProviderTest {
    private static UserDetailsService userDetailsService;
    private static JwtTokenProvider jwtTokenProvider;

    @BeforeAll
    static void setup() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        jwtTokenProvider = Mockito.spy(jwtTokenProvider);
        String expectedSecretKey = Base64.getEncoder().encodeToString("secret".getBytes());
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", expectedSecretKey);
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000L);
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
        UserDetails userDetails = new User("test", "", List.of());
        Mockito.when(userDetailsService.loadUserByUsername(any())).thenReturn(userDetails);
        String token = jwtTokenProvider.createToken("test", List.of(Role.RoleName.USER.name()));
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        Assertions.assertNotNull(authentication);
        Assertions.assertEquals(userDetails, authentication.getPrincipal());
        Assertions.assertEquals("", authentication.getCredentials());
    }

    @Test
    void resolveToken_ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer token");
        String token = jwtTokenProvider.resolveToken(request);
        Assertions.assertEquals("token", token);
    }

    @Test
    void resolveToken_notOk() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("token");
        String token = jwtTokenProvider.resolveToken(request);
        Assertions.assertNull(token);
    }

    @Test
    void validateToken_ok() {
        String token = jwtTokenProvider.createToken("test",
                List.of(Role.RoleName.USER.name()));
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_notOk() {
        Assertions.assertThrows(RuntimeException.class, () ->
                jwtTokenProvider.validateToken("bad token"));
    }
}

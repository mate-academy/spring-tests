package mate.academy.security.jwt;

import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

class JwtTokenProviderTest {
    private static JwtTokenProvider jwtTokenProvider;
    private static UserDetailsService userDetailsService;

    @BeforeAll
    static void beforeAll() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000);
    }

    @Test
    void createToken_OK() {
        String login = "bchupika@mate.academy";
        List<String> roles = List.of(Role.RoleName.USER.name());
        String actual = jwtTokenProvider.createToken(login, roles);
        Assertions.assertNotNull(actual,"Token must not be null for login: " + login + " roles: " + roles);
    }

    @Test
    void getAuthentication_Ok() {
        String login = "bchupika@mate.academy";
        String password = "12345678";
        List<String> roles = List.of(Role.RoleName.USER.name());
        String validToken = jwtTokenProvider.createToken(login, roles);
        User.UserBuilder builder = User.withUsername(login);
        builder.password(password);
        builder.authorities("USER");
        UserDetails userDetails = builder.build();
        Mockito.when(userDetailsService.loadUserByUsername(Mockito.any())).thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(validToken);
        Assertions.assertNotNull(actual, "Authentication must not be null for token: " + validToken);
        Assertions.assertEquals(login, actual.getName(), "Login must be: "
                + login + " but was: " + actual.getName());
    }

    @Test
    void getUsername_Ok() {
        String login = "bchupika@mate.academy";
        List<String> roles = List.of(Role.RoleName.USER.name());
        String validToken = jwtTokenProvider.createToken(login, roles);
        String actual = jwtTokenProvider.getUsername(validToken);
        Assertions.assertNotNull(actual, "Username must not be null for token: " + validToken);
        Assertions.assertEquals(login, actual, "Username must be: " + login + " but was: " + actual);
    }

    @Test
    void resolveToken_Ok() {
        String login = "bchupika@mate.academy";
        List<String> roles = List.of(Role.RoleName.USER.name());
        String validToken = jwtTokenProvider.createToken(login, roles);
        String bearerToken = "Bearer " + validToken;
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(httpServletRequest.getHeader(Mockito.any())).thenReturn(bearerToken);
        String actual = jwtTokenProvider.resolveToken(httpServletRequest);
        Assertions.assertNotNull(actual, "Token must not be null for: " + bearerToken);
        Assertions.assertEquals(validToken, actual, "Token must be: " + validToken + " but was: " + actual);
    }

    @Test
    void validateToken_Ok() {
        String login = "bchupika@mate.academy";
        List<String> roles = List.of(Role.RoleName.USER.name());
        String validToken = jwtTokenProvider.createToken(login, roles);
        boolean actual = jwtTokenProvider.validateToken(validToken);
        Assertions.assertTrue(actual,"Token must be valid for token: " + validToken);
    }

    @Test
    void validateToken_invalidToken_NotOk() {
        String login = "bchupika@mate.academy";
        List<String> roles = List.of(Role.RoleName.USER.name());
        String validToken = jwtTokenProvider.createToken(login, roles);
        String invalidToken = validToken + "bobobob";
        Assertions.assertThrows(RuntimeException.class,() -> {jwtTokenProvider.validateToken(invalidToken);},
                "RuntimeException was expected");
    }
}

package mate.academy.security.jwt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final long VALIDITY_IN_MILLISECONDS = 3600000;
    private static final String LOGIN = "user@gmail.com";
    private static final String PASSWORD = "123456";
    private static final String SECRET_KEY = "secret";
    private static final List<String> ROLES = List.of(Role.RoleName.USER.name());

    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;
    private String token;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(
                jwtTokenProvider, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(
                jwtTokenProvider, "validityInMilliseconds", VALIDITY_IN_MILLISECONDS);
        token = jwtTokenProvider.createToken(LOGIN, ROLES);
    }

    @Test
    void createToken_ok() {
        String actual = token;
        Assertions.assertNotNull(actual);
    }

    @Test
    void getAuthentication_ok() {
        UserDetails userDetails = User.withUsername(LOGIN)
                .password(PASSWORD)
                .roles(String.valueOf(ROLES))
                .build();
        Mockito.when(userDetailsService.loadUserByUsername(LOGIN))
                .thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.getPrincipal().toString().contains(LOGIN));
    }

    @Test
    void getUsername_ok() {
        String actual = jwtTokenProvider.getUsername(token);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(LOGIN, actual);
    }

    @Test
    void resolveToken_ok() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization"))
                .thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(req);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, token);
    }

    @Test
    void validateToken_ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_tokenIsNull_notOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(null));
    }

    @Test
    void validateToken_incorrectToken_notOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken("token"));
    }
}

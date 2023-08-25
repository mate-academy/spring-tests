package mate.academy.security.jwt;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.security.CustomUserDetailsService;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String EMAIL = "bob@mail.com";
    private static final String PASSWORD = "1234";
    private static final String ROLE_NAME = "USER";
    private static final String SECRET_KEY = "USER";
    private static final Long VALIDITY = 3600000L;
    private static final String INVALID_TOKEN = "Some invalid token";
    private static UserDetailsService userDetailsService;
    private static UserService userService;
    private static JwtTokenProvider jwtTokenProvider;
    private static HttpServletRequest httpServletRequest;
    private String testToken;

    @BeforeAll
    static void beforeAll() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", VALIDITY);
        httpServletRequest = Mockito.mock(HttpServletRequest.class);
    }

    @BeforeEach
    void setUp() {
        testToken = jwtTokenProvider.createToken(EMAIL, List.of(ROLE_NAME));
    }

    @Test
    void createToken_Ok() {
        String token = jwtTokenProvider.createToken(EMAIL, List.of(ROLE_NAME));
        Assertions.assertNotNull(token);
    }

    @Test
    void getAuthentication_Ok() {
        User bob = new User();
        String email = EMAIL;
        bob.setEmail(email);
        bob.setPassword(PASSWORD);
        bob.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userService.findByEmail(email)).thenReturn(Optional.of(bob));
        Authentication auth = jwtTokenProvider.getAuthentication(testToken);
        Assertions.assertNotNull(auth);
        Assertions.assertEquals(bob.getEmail(), auth.getName());
    }

    @Test
    void getUsername_Ok() {
        String actual = jwtTokenProvider.getUsername(testToken);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual);
    }

    @Test
    void resolveToken_Ok() {
        Mockito.when(httpServletRequest.getHeader("Authorization"))
                .thenReturn("Bearer " + testToken);
        String actual = jwtTokenProvider.resolveToken(httpServletRequest);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(testToken, actual);
    }

    @Test
    void validateToken_Ok() {
        boolean expect = true;
        boolean actual = jwtTokenProvider.validateToken(testToken);
        Assertions.assertEquals(expect, actual);
    }

    @Test
    void validateToken_notOk() {
        try {
            boolean actual = jwtTokenProvider.validateToken(INVALID_TOKEN);
        } catch (Exception e) {
            Assertions.assertEquals("Expired or invalid JWT token", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive RuntimeException");
    }
}

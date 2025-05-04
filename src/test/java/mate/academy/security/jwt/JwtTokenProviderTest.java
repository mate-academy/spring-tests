package mate.academy.security.jwt;

import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String DEFAULT_USER_EMAIL = "user@gmail.com";
    private static final int VALIDITY_IN_MILLISECONDS = 3600000;
    private static final String SECRET_KEY = "secret";
    private static Role adminRole;
    private static Role userRole;
    private static List<String> roleList;
    private String token;
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    @BeforeAll
    static void init() {
        userRole = new Role();
        userRole.setId(1L);
        userRole.setRoleName(Role.RoleName.USER);
        adminRole = new Role();
        adminRole.setId(2L);
        adminRole.setRoleName(Role.RoleName.ADMIN);
        roleList = List.of(userRole.getRoleName().name(),
                adminRole.getRoleName().name());
    }

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(
                jwtTokenProvider, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(
                jwtTokenProvider, "validityInMilliseconds", VALIDITY_IN_MILLISECONDS);
        token = jwtTokenProvider.createToken(DEFAULT_USER_EMAIL, roleList);
    }

    @Test
    void createToken_correctValue_ok() {
        Assertions.assertEquals(3, token.split("\\.").length);
    }

    @Test
    void getUsername_correctValue_ok() {
        String actual = jwtTokenProvider.getUsername(token);
        Assertions.assertEquals(DEFAULT_USER_EMAIL, actual,
                "Username should be: " + DEFAULT_USER_EMAIL + ", actual: " + actual);
    }

    @Test
    void resolveToken_correctValue_ok() {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + token);
        Assertions.assertEquals(
                token, jwtTokenProvider.resolveToken(httpServletRequest), "Must be correct");
    }

    @Test
    void resolveToken_nullToken_notOk() {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(httpServletRequest.getHeader("Authorization")).thenReturn(null);
        Assertions.assertNull(jwtTokenProvider.resolveToken(httpServletRequest),
                "Token = null, the test should not working");
    }

    @Test
    void resolveToken_incorrectToken_notOk() {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(httpServletRequest.getHeader("Authorization")).thenReturn("incorrectToken");
        Assertions.assertNull(jwtTokenProvider.resolveToken(httpServletRequest),
                "Incorrect token, the test should not working");
    }

    @Test
    void validateToken_correctCase_ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(token),
                "Method should return true for valid token");
    }

    @Test
    void validateToken_nullToken_notOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(null),
                "Should throw RuntimeException when parameter is null");
    }

    @Test
    void validateToken_incorrectToken_notOk() {
        String fakeToken = "random.text.example";
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(fakeToken),
                "Should throw RuntimeException for incorrect token");
    }

    @Test
    void getAuthentication_correctCase_ok() {
        User.UserBuilder builder = User.withUsername(DEFAULT_USER_EMAIL);
        builder.roles(userRole.getRoleName().name(), adminRole.getRoleName().name());
        builder.password("123");
        UserDetails userDetails = builder.build();
        int expectedSize = userDetails.getAuthorities().size();
        Mockito.when(userDetailsService.loadUserByUsername(any())).thenReturn(userDetails);
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        Assertions.assertTrue(authentication.isAuthenticated(),
                "Must be authenticated, but return false");
        Assertions.assertEquals(expectedSize, authentication.getAuthorities().size(),
                "Authentication must have" + expectedSize
                        + "authorities, but is " + authentication.getAuthorities().size());
    }

    @Test
    void getAuthentication_nullUserDetailsService_notOk() {
        Mockito.when(userDetailsService.loadUserByUsername(any())).thenReturn(null);
        Assertions.assertThrows(NullPointerException.class,
                () -> jwtTokenProvider.getAuthentication(token),
                "Method must throw NullPointerException");
    }
}

package mate.academy.security.jwt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String EMAIL = "bob@gmail.com";
    private static final String PASSWORD = "12345678";
    private static final String USER_ROLE = "USER";
    private static final String VALID_TOKEN  = "eyJhbGciOiJIUzI1NiJ9"
            + ".eyJzdWIiOiJib2JAZ21haWwuY29tIiwicm9sZXMiOlsiVVNFUiJdLCJpYXQ"
            + "iOjE2NTE2NjMwNTcsImV4cCI6OTQ0NTEwMjQyNjU3fQ"
            + ".mk_l0poZNHTAqbunh6BQ22KQfoIowdbs4MsfR9ixKMw";
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000L);
        jwtTokenProvider.init();
    }

    @Test
    void createToken_ok() {
        String actual = jwtTokenProvider.createToken(EMAIL, List.of(USER_ROLE));
        Assertions.assertNotNull(actual, "After creation token should no be null");
    }

    @Test
    void getUsername_ok() {
        String actual = jwtTokenProvider.getUsername(VALID_TOKEN);
        Assertions.assertEquals(EMAIL, actual);
    }

    @Test
    void resolveToken_ok() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn("Bearer " + VALID_TOKEN);
        String actual = jwtTokenProvider.resolveToken(req);
        Assertions.assertEquals(VALID_TOKEN, actual, "Should return valid token");
    }

    @Test
    void validateToken_ok() {
        boolean actual = jwtTokenProvider.validateToken(VALID_TOKEN);
        Assertions.assertTrue(actual, "Should return true for valid token");
    }

    @Test
    void getAuthentication_ok() {
        UserBuilder builder = User.withUsername(EMAIL);
        UserDetails userDetails = builder.password(PASSWORD).roles(USER_ROLE).build();
        Mockito.when(userDetailsService.loadUserByUsername(Mockito.any())).thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(VALID_TOKEN);
        User actualUser = (User) actual.getPrincipal();
        Assertions.assertNotNull(actual,
                "Authentication object should not be null for valid token");
        Assertions.assertNotNull(actualUser,
                "Should return valid user for valid token");
        Assertions.assertEquals(EMAIL, actualUser.getUsername(),
                "Should be valid email for user with valid token");
        Assertions.assertEquals(PASSWORD, actualUser.getPassword(),
                "Should be valid password for user with valid token");
    }
}

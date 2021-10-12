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
    private static final String EMAIL = "bchupika@mate.academy";
    private static final String PASSWORD = "12345678";
    private static final String USER_ROLE = "USER";
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiY2h1cGlrYUBtYXRlLmF"
            + "jYWRlbXkiLCJyb2xlcyI6WyJVU0VSIl0sImlhdCI6MTYzNDA0MzU0MywiZXh"
            + "wIjoxNjM0MDQ3MTQzfQ._jmQOP_3pSXmPNMxVWoCLQjIQaRqjWUS0FvbuRhbmvA";
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
        Assertions.assertNotNull(actual);
    }

    @Test
    void getUsername_Ok() {
        String actual = jwtTokenProvider.getUsername(TOKEN);
        Assertions.assertEquals(EMAIL, actual);
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn("Bearer " + TOKEN);
        String actual = jwtTokenProvider.resolveToken(req);
        Assertions.assertEquals(TOKEN, actual);
    }

    @Test
    void validateToken_Ok() {
        boolean actual = jwtTokenProvider.validateToken(TOKEN);
        Assertions.assertTrue(actual);
    }

    @Test
    void getAuthentication_Ok() {
        UserBuilder builder = User.withUsername(EMAIL);
        UserDetails userDetails = builder.password(PASSWORD).roles(USER_ROLE).build();
        Mockito.when(userDetailsService.loadUserByUsername(Mockito.any())).thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(TOKEN);
        User actualUser = (User) actual.getPrincipal();
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(EMAIL, actualUser.getUsername());
        Assertions.assertEquals(PASSWORD, actualUser.getPassword());
    }
}

package mate.academy.security.jwt;

import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
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
    private static final String USER_EMAIL = "user@gmail.com";
    private static final String USER_PASSWORD = "12345";
    private static final String TOKEN_VALID = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGdtYWlsLmNv"
            + "bSIsInJvbGVzIjpbIlVTRVIiXSwiaWF0IjoxNjUwNjQ0NjY3LCJleHAiOjE2NTI3OTIxNTF9.LimbixoatK"
            + "jc6SmCmiLAVA03iMrft_x7bMIEuJDbnHA";
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secretTestKey");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", Integer.MAX_VALUE);
    }

    @Test
    void createToken_OK() {
        List<String> roles = List.of("USER");
        String actual = jwtTokenProvider.createToken(USER_EMAIL, roles);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(TOKEN_VALID.length(), actual.length());
    }

    @Test
    void getAuthentication() {
        User.UserBuilder builder = org.springframework.security.core.userdetails
                .User.withUsername(USER_EMAIL)
                .password(USER_PASSWORD)
                .roles(List.of("USER").toArray(String[]::new));
        UserDetails userDetails = builder.build();
        Mockito.when(this.userDetailsService.loadUserByUsername(any())).thenReturn(userDetails);
        Authentication authentication = jwtTokenProvider.getAuthentication(TOKEN_VALID);
        User actual = (User) authentication.getPrincipal();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.getUsername(), USER_EMAIL);
    }

    @Test
    void getUsername() {
        String actual = jwtTokenProvider.getUsername(TOKEN_VALID);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, USER_EMAIL);
    }

    @Test
    void resolveToken() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn("Bearer " + TOKEN_VALID);
        String actual = jwtTokenProvider.resolveToken(req);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, TOKEN_VALID);
    }

    @Test
    void validateToken() {
        boolean actual = jwtTokenProvider.validateToken(TOKEN_VALID);
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual);
    }
}

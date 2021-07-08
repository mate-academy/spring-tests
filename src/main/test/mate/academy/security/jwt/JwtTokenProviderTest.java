package mate.academy.security.jwt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;


class JwtTokenProviderTest {
    private static final String MOCK_TOKEN = "eyJhbGciOiJIUzI1NiJ9"
            + ".eyJzdWIiOiJib2IiLCJyb2xlcyI6WyJVc2VyIl0sImlhdCI6MTYyNTc0NTE0NSwiZXhwIjoxNjI1"
            + "NzQ4NzQ1fQ"
            + ".YmgRbkW6cT4bPChmWW7xcBwup9YBHE3qsA_TzkRmgok";
    private JwtTokenProvider tokenProvider;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        tokenProvider = new JwtTokenProvider(userDetailsService);
        tokenProvider = Mockito.spy(tokenProvider);
        ReflectionTestUtils.setField(tokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(tokenProvider, "validityInMilliseconds", 3600000L);
        tokenProvider.init();
    }

    @Test
    void createToken_Ok() {
        String actual = tokenProvider.createToken("bob", List.of("User"));
        Assertions.assertNotEquals(MOCK_TOKEN, actual);
    }

    @Test
    void getAuthentication_Ok() {
        UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername("bob");
        builder.password("132456789");
        builder.roles("User");
        UserDetails userDetails = builder.build();
        UsernamePasswordAuthenticationToken expected = new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
        Mockito.when(userDetailsService.loadUserByUsername(Mockito.any())).thenReturn(userDetails);

        Authentication actual = tokenProvider.getAuthentication(MOCK_TOKEN);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getUsername_Ok() {
        String token = tokenProvider.createToken("bob", List.of("User"));
        String username = tokenProvider.getUsername(token);
        Assertions.assertEquals("bob", username);
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn("Bearer example");
        String actual = tokenProvider.resolveToken(req);
        Assertions.assertEquals("example", actual);
    }

    @Test
    void resolveToken_NoBearer() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn("example");
        String actual = tokenProvider.resolveToken(req);
        Assertions.assertNull(actual);
    }

    @Test
    void resolveToken_nullHeader() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn(null);
        String actual = tokenProvider.resolveToken(req);
        Assertions.assertNull(actual);
    }

    @Test
    void validateToken_Ok() {
        String token = tokenProvider.createToken("bob", List.of("User"));
        Assertions.assertTrue(tokenProvider.validateToken(token));
    }

    @Test
    void validateToken_tooOld() {
        ReflectionTestUtils.setField(tokenProvider, "validityInMilliseconds", 0L);
        String token = tokenProvider.createToken("bob", List.of("User"));
        try {
            tokenProvider.validateToken(token);
        } catch (RuntimeException e) {
            Assertions.assertEquals("Expired or invalid JWT token", e.getMessage());
        }
    }
}

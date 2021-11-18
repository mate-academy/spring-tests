package mate.academy.security.jwt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

public class JwtTokenProviderTest {
    private static final String EMAIL = "bob@gmail.com";
    private static final String PASSWORD = "12345678";
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib2JAZ21haWwuY29"
            + "tIiwicm9sZXMiOlsiVVNFUiJdLCJpYXQiOjE2MzcyMzkwNDEsImV4cCI6MTYzNzI0MjY0MX0.n7bznsgu"
            + "vOVYcO_yo_rYlwzaYyDGLFWgJRyvZvf58jg";
    private static final String INCORRECT_TOKEN = "eyJhbGciOiJIUzI1NiJ9."
            + "eyJzdWIiOiJiY2h1cGlrYUBtYXRlLmFjYWRlbXkiLCJyb2xlcyI6WyJVU0VSIl0s"
            + "ImlhdCI6MTYzNzE1NDkxNywiZXhwIjoxNjM3MTU4NTE3fQ"
            + ".1guog15bNOVrrNmWBcEXJVcunvtxNMZyDySZAF-jq2Q";
    private static final String USER_ROLE = Role.RoleName.USER.name();
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        request = Mockito.mock(HttpServletRequest.class);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 36000000L);
        jwtTokenProvider.init();
    }

    @Test
    void createToken_Ok() {
        String actual = jwtTokenProvider.createToken(EMAIL, List.of(USER_ROLE));
        Assertions.assertNotNull(actual);
        String[] tokenParts = actual.split("\\.");
        Assertions.assertEquals(3, tokenParts.length);
    }

    @Test
    void getAuthentication_Ok() {
        User.UserBuilder userBuilder = User.withUsername(EMAIL);
        UserDetails userDetails = userBuilder.password(PASSWORD).roles(USER_ROLE).build();
        Mockito.when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);
        UsernamePasswordAuthenticationToken expected
                = new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
        Authentication actual = jwtTokenProvider.getAuthentication(TOKEN);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getUsername_Ok() {
        String actual = jwtTokenProvider.getUsername(TOKEN);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual);
    }

    @Test
    void resolveToken_Ok() {
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + TOKEN);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(TOKEN, actual);
    }

    @Test
    void validateToken_Ok() {
        boolean actual = jwtTokenProvider.validateToken(TOKEN);
        Assertions.assertTrue(actual);
    }

    @Test
    void validateToken_NotOk() {
        try {
            jwtTokenProvider.validateToken(INCORRECT_TOKEN);
        } catch (RuntimeException e) {
            Assertions.assertEquals("Expired or invalid JWT token", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive RuntimeException: Expired or invalid JWT token");
    }
}

package mate.academy.security.jwt;

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
    private String invalidToken;
    private String login;
    private List<String> roles;
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private String validToken;

    @BeforeEach
    void setUp() {
        invalidToken = "eyJhbGciOiJIUzI1NiJ9"
                + ".eyJzdWIiOiJiY2h1cGlrYUBtYXRlLmFjYWRlbXkiLCJyb2xlcyI6Wy"
                + "JVU0VSIl0sImlhdCI6MTY0OTc4MTI0OSwiZXhwIjoxNjQ5Nzg0ODQ5fQ"
                + ".ZK6PasueR53-bfYBoSizeJKpESrTrq8";
        login = "bchupika@mate.academy";
        roles = List.of("USER");
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        jwtTokenProvider = Mockito.spy(jwtTokenProvider);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000L);
        jwtTokenProvider.init();
        validToken = jwtTokenProvider.createToken(login, roles);
    }

    @Test
    void createToken_Ok() {
        String actual = jwtTokenProvider.createToken(login, roles);
        Assertions.assertNotNull(actual);
    }

    @Test
    void getUsername_Ok() {
        String actual = jwtTokenProvider.getUsername(validToken);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(login, actual);
    }

    @Test
    void getAuthentication_Ok() {
        User.UserBuilder userBuilder;
        userBuilder = User.withUsername(login);
        userBuilder.password("12345678");
        userBuilder.roles("USER");
        UserDetails userDetails = userBuilder.build();
        Mockito.when(userDetailsService.loadUserByUsername(Mockito.any())).thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(validToken);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(login, actual.getName());
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest mock = Mockito.mock(HttpServletRequest.class);
        Mockito.when(mock.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        String actual = jwtTokenProvider.resolveToken(mock);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(validToken, actual);
    }

    @Test
    void validateToken_Ok() {
        boolean actual = jwtTokenProvider.validateToken(validToken);
        Assertions.assertTrue(actual);
    }

    @Test
    void validateToken_notOk() {
        try {
            jwtTokenProvider.validateToken(invalidToken);
        } catch (RuntimeException e) {
            Assertions.assertEquals("Expired or invalid JWT token", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive UsernameNotFoundException");
    }
}

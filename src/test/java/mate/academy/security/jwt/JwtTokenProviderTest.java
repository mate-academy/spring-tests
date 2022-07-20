package mate.academy.security.jwt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static UserDetailsService userDetailsService;
    private static JwtTokenProvider jwtTokenProvider;
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib2IiLCJyb2xlcyI6WyJV"
            + "U0VSIl0sImlhdCI6MTY1ODI2MzcyNSwiZXhwIjoxNjU4MjY3MzI1fQ.9ujNBcbV9d5EE_C35CI1-3ykwmAr"
            + "sq0pIAwxzAOBpdI";

    @BeforeAll
    public static void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000);
    }

    @Test
    public void createToken_ok() {
        String login = "bob";
        List<String> roles = List.of("USER");
        String actual = jwtTokenProvider.createToken(login, roles);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(3, actual.split("\\.").length);
    }

    @Test
    public void createToken_nullInputData_ok() {
        String actual = jwtTokenProvider.createToken(null, null);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(3, actual.split("\\.").length);
    }

    @Test
    public void createToken_invalidInputData_ok() {
        String actual = jwtTokenProvider.createToken("", List.of());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(3, actual.split("\\.").length);
    }

    @Test
    public void getUsername_ok() {
        String actual = jwtTokenProvider.getUsername(TOKEN);
        String expected = "bob";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getUsername_invalidToken_notOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.getUsername("Hello world"));
    }

    @Test
    public void getUsername_nullToken_notOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.getUsername(null));
    }

    @Test
    public void getAuthentication_Ok() {
        User.UserBuilder builder = User.withUsername("bob");
        builder.password("1234");
        builder.roles("USER");
        UserDetails userDetails = builder.build();
        Authentication expected = new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
        Mockito.when(userDetailsService.loadUserByUsername("bob")).thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(TOKEN);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getAuthentication_invalidToken_notOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.getAuthentication(""));
    }

    @Test
    public void getAuthentication_nullToken_notOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.getAuthentication(null));
    }

    @Test
    public void resolveToken_ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + TOKEN);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertEquals(TOKEN, actual);
    }

    @Test
    public void resolveToken_nullAuthorizationHeader_ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn(null);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNull(actual);
    }

    @Test
    public void resolveToken_authorizationHeaderDoesNotStartWithBearer_ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Hello world");
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNull(actual);
    }

    @Test
    public void resolveToken_nullInput_notOk() {
        Assertions.assertThrows(RuntimeException.class, () -> jwtTokenProvider.resolveToken(null));
    }

    @Test
    public void validateToken_ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(TOKEN));
    }

    @Test
    public void validateToken_invalidInput_notOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken("Hello world"));
    }

    @Test
    public void validateToken_nullInput_notOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(null));
    }
}

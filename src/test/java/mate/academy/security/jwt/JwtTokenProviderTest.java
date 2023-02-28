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
    }

    @Test
    public void createToken_nullInputData_ok() {
        String actual = jwtTokenProvider.createToken(null, null);
        Assertions.assertNotNull(actual);
    }

    @Test
    public void createToken_emptyInputData_ok() {
        String actual = jwtTokenProvider.createToken("", List.of());
        Assertions.assertNotNull(actual);
    }

    @Test
    public void getUsername_ok() {
        String actual = jwtTokenProvider.getUsername(jwtTokenProvider
                .createToken("bob", List.of("USER")));
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
        Authentication actual = jwtTokenProvider.getAuthentication(jwtTokenProvider
                .createToken("bob", List.of("USER")));
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
        String token = jwtTokenProvider
                .createToken("bob", List.of("USER"));
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertEquals(token, actual);
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
        Assertions.assertTrue(jwtTokenProvider.validateToken(jwtTokenProvider
                .createToken("bob", List.of("USER"))));
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

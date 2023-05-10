package mate.academy.security.jwt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static UserDetailsService userDetailsService;
    private static JwtTokenProvider jwtTokenProvider;
    private static String createdToken;
    private static String expectedLogin;

    @BeforeAll
    static void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000L);
        expectedLogin = "vitaliy@mail.com";
        createdToken = jwtTokenProvider.createToken(expectedLogin, List.of("USER"));
    }

    @Test
    void createToken_Ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(createdToken));
        Assertions.assertEquals(expectedLogin, jwtTokenProvider.getUsername(createdToken));
    }

    @Test
    void validateToken_Ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(createdToken));
    }

    @Test
    void validateToken_emptyValue_notOk() {
        Assertions.assertThrows(RuntimeException.class, () ->
                jwtTokenProvider.validateToken(""));
    }

    @Test
    void validateToken_nullValue_notOk() {
        Assertions.assertThrows(RuntimeException.class, () ->
                jwtTokenProvider.validateToken(null));
    }

    @Test
    void getUserName_Ok() {
        Assertions.assertEquals(expectedLogin, jwtTokenProvider.getUsername(createdToken));
    }

    @Test
    void getUserName_emptyValue_notOk() {
        Assertions.assertThrows(RuntimeException.class, () ->
                jwtTokenProvider.getUsername(""));
    }

    @Test
    void getUserName_nullValue_notOk() {
        Assertions.assertThrows(RuntimeException.class, () ->
                jwtTokenProvider.getUsername(null));
    }

    @Test
    void getAuthentication_Ok() {
        UserBuilder userBuilder = User.withUsername(expectedLogin);
        userBuilder.password("12345678");
        userBuilder.roles("USER", "ADMIN");
        UserDetails expected = userBuilder.build();
        Mockito.when(userDetailsService.loadUserByUsername(expectedLogin)).thenReturn(expected);
        Authentication authentication = jwtTokenProvider.getAuthentication(createdToken);
        UserDetails actual = (UserDetails) authentication.getPrincipal();
        Assertions.assertEquals(expected.getUsername(), actual.getUsername());
        Assertions.assertEquals(expected.getPassword(), actual.getPassword());
        Assertions.assertEquals(expected.getAuthorities(), actual.getAuthorities());
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn("Bearer " + createdToken);
        String actual = jwtTokenProvider.resolveToken(req);
        Assertions.assertEquals(createdToken, actual);
        Assertions.assertTrue(jwtTokenProvider.validateToken(actual));
        Assertions.assertEquals(expectedLogin, jwtTokenProvider.getUsername(actual));
    }

    @Test
    void resolveToken_nullHeaderValue_Ok() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        String expected = null;
        Mockito.when(req.getHeader("Authorization")).thenReturn(expected);
        String actual = jwtTokenProvider.resolveToken(req);
        Assertions.assertEquals(expected, actual);
    }
}

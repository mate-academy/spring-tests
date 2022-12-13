package mate.academy.security.jwt;

import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.exception.InvalidJwtAuthenticationException;
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
    private static UserDetailsService userDetailsService;
    private static HttpServletRequest httpServletRequest;
    private static JwtTokenProvider jwtTokenProvider;
    private String token;

    @BeforeAll
    static void beforeAll() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        httpServletRequest = Mockito.mock(HttpServletRequest.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000);
    }

    @BeforeEach
    void setUp() {
        token = jwtTokenProvider.createToken("user1@gmail.com", List.of("USER"));
    }

    @Test
    void createToken_Ok() {
        Assertions.assertNotNull(token);
    }

    @Test
    void getAuthentication_Ok() {
        String[] roles = new String[] {"USER"};
        UserDetails userDetails =
                User.withUsername("user1@gmail.com").password("12345678").roles(roles).build();
        Mockito.when(userDetailsService.loadUserByUsername(any())).thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        Assertions.assertNotNull(actual);
    }

    @Test
    void getUsername_Ok() {
        String expected = "user1@gmail.com";
        String actual = jwtTokenProvider.getUsername(token);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void resolveToken_Ok() {
        Mockito.when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(httpServletRequest);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(token, actual);
    }

    @Test
    void resolveToken_nullToken_notOk() {
        Mockito.when(httpServletRequest.getHeader("Authorization")).thenReturn(null);
        String actual = jwtTokenProvider.resolveToken(httpServletRequest);
        Assertions.assertNull(actual);
    }

    @Test
    void validateToken_Ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_invalidToken() {
        Assertions.assertThrows(InvalidJwtAuthenticationException.class,
                () -> jwtTokenProvider.validateToken(token + "putin huilo"));
    }
}

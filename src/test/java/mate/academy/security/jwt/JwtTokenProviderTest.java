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
    private static final String VALID_EMAIL = "bob@i.ua";
    private static final String VALID_PASSWORD = "1234";
    private static final String SECRET_KEY = "secret";
    private static final long VALIDITY = 3600000L;
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private String token;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(
                jwtTokenProvider, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(
                jwtTokenProvider, "validityInMilliseconds", VALIDITY);
        token = jwtTokenProvider.createToken(VALID_EMAIL, List.of("USER"));
    }

    @Test
    void createToken_Ok() {
        String actual = jwtTokenProvider.createToken(VALID_EMAIL, List.of("USER"));
        Assertions.assertNotNull(actual);
    }

    @Test
    void getUsername_Ok() {
        Assertions.assertNotNull(jwtTokenProvider.getUsername(token));
        Assertions.assertEquals(VALID_EMAIL, jwtTokenProvider.getUsername(token));
    }

    @Test
    void getAuthentication_Ok() {
        UserDetails userDetails = User.builder()
                .username(VALID_EMAIL)
                .password(VALID_PASSWORD)
                .roles("USER")
                .build();
        Mockito.when(userDetailsService.loadUserByUsername(VALID_EMAIL))
                .thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        Assertions.assertNotNull(actual.toString());
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(httpServletRequest.getHeader("Authorization"))
                .thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(httpServletRequest);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(token, actual);
    }

    @Test
    void resolveTokenWithoutHeader_notOk() {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(httpServletRequest.getHeader("Authorization"))
                .thenReturn("Empty");
        String actual = jwtTokenProvider.resolveToken(httpServletRequest);
        Assertions.assertNull(actual);
    }

    @Test
    void validateToken_Ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateTokenIsInvalid_notOk() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            jwtTokenProvider.validateToken("a" + token);
        });
    }

    @Test
    void validateTokenIsNull_notOk() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            jwtTokenProvider.validateToken(null);
        });
    }
}

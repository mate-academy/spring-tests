package mate.academy.security.jwt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtTokenProviderTest {
    private static final String LOGIN = "hello@i.am";
    private static final String ROLE_USER = "USER";
    private static final String PASSWORD = "1221";
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9" +
            ".eyJzdWIiOiJoZWxsb0BpLmFtIiwicm9sZXMiOlsiVVNFUiJdLCJpYXQiOjE2NTAzNTQzMjEsImV4cCI6MTY1MDM1NzkyMX0" +
            ".WCSZYN9d9ycsnRh2Yr0nS0cjv10SZ5nYW4rAKhiqRMI";
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
    void createToken_Ok() {
        String actual = jwtTokenProvider.createToken(LOGIN, List.of(ROLE_USER));
        Assertions.assertNotNull(actual);
        Assertions.assertNotEquals(actual, "");
    }

    @Test
    void getAuthentication_Ok() {
        User.UserBuilder builder = User.withUsername(LOGIN);
        UserDetails userDetails = builder.password(PASSWORD).roles(ROLE_USER).build();
        Mockito.when(userDetailsService.loadUserByUsername(Mockito.anyString())).thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(TOKEN);
        User actualUser = (User) actual.getPrincipal();
        Assertions.assertNotNull(actual);
        Assertions.assertNotEquals(actualUser.getUsername(), "");
        Assertions.assertNotEquals(actualUser.getPassword(), "");
        Assertions.assertNotEquals(actualUser.getAuthorities(), Set.of());
        Assertions.assertEquals(actualUser.getUsername(), LOGIN);
        Assertions.assertEquals(actualUser.getPassword(), PASSWORD);
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest mock = Mockito.mock(HttpServletRequest.class);
        Mockito.when(mock.getHeader("Authorization")).thenReturn("Bearer " + TOKEN);
        String actual = jwtTokenProvider.resolveToken(mock);
        Assertions.assertNotNull(actual);
        Assertions.assertNotEquals(actual, "");
        Assertions.assertEquals(3, actual.split("\\.").length);
        Assertions.assertEquals(actual, TOKEN);
    }

    @Test
    void resolveToken_notOk() {
        HttpServletRequest mock = Mockito.mock(HttpServletRequest.class);
        Mockito.when(mock.getHeader("Authorization")).thenReturn("" + TOKEN);
        String actual = jwtTokenProvider.resolveToken(mock);
        Assertions.assertNull(actual);
    }

    @Test
    void validateToken_Ok() {
        boolean actual = jwtTokenProvider.validateToken(TOKEN);
        Assertions.assertTrue(actual);
    }

    @Test
    void validateToken_NotOk() {
        String notValid = "";
        assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(notValid));

    }
}

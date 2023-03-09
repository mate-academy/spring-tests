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
    private static final String TEST_EMAIL = "test@test.ua";
    private static final List TEST_ROLES_LIST = List.of("ADMIN", "USER");
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() throws Exception {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000);
    }

    @Test
    void createToken_Ok() {
        String actual = jwtTokenProvider.createToken(TEST_EMAIL, TEST_ROLES_LIST);
        Assertions.assertNotNull(actual,
                String.format("Token can't be empty for email %s", TEST_EMAIL));
    }

    @Test
    void getAuthentication_Ok() {
        String token = jwtTokenProvider.createToken(TEST_EMAIL, TEST_ROLES_LIST);
        UserDetails userDetails = User.builder()
                .username("test@test.com")
                .password("")
                .roles("USER")
                .build();
        Mockito.when(this.userDetailsService.loadUserByUsername(TEST_EMAIL))
                .thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        Assertions.assertNotNull(actual.toString(),
                String.format("Method should create Authentication object, but was: %s", actual));
    }

    @Test
    void getUsername_Ok() {
        String token = jwtTokenProvider.createToken(TEST_EMAIL, TEST_ROLES_LIST);
        String actual = jwtTokenProvider.getUsername(token);
        Assertions.assertEquals(TEST_EMAIL, actual,
                String.format("Method should return email: %s, but was: %s", TEST_EMAIL, actual));
    }

    @Test
    void resolveToken_Ok() {
        String token = jwtTokenProvider.createToken(TEST_EMAIL, TEST_ROLES_LIST);
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(httpServletRequest.getHeader("Authorization"))
                .thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(httpServletRequest);
        Assertions.assertEquals(token, actual,
                String.format("Method should return token: %s, but was: %s", token, actual));
    }

    @Test
    void resolveToken_tokenWithoutHeader_Ok() {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(httpServletRequest.getHeader("Authorization"))
                .thenReturn("token");
        String actual = jwtTokenProvider.resolveToken(httpServletRequest);
        Assertions.assertNull(actual,
                String.format("Method should return token: %s, but was: %s", null, actual));
    }

    @Test
    void resolveToken_tokenIsNull_Ok() {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(httpServletRequest.getHeader("Authorization"))
                .thenReturn(null);
        String actual = jwtTokenProvider.resolveToken(httpServletRequest);
        Assertions.assertNull(actual,
                String.format("Method should return token: %s, but was: %s", null, actual));
    }

    @Test
    void validateToken_Ok() {
        String token = jwtTokenProvider.createToken(TEST_EMAIL, TEST_ROLES_LIST);
        boolean actual = jwtTokenProvider.validateToken(token);
        Assertions.assertTrue(actual, "Method should return true for valid token");
    }

    @Test
    void validateToken_tokenIsNull_notOk() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            jwtTokenProvider.validateToken(null);
        });
    }

    @Test
    void validateToken_tokenIsInvalid_notOk() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            jwtTokenProvider.validateToken("token");
        });
    }
}

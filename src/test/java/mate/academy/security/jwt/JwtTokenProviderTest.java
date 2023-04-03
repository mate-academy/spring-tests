package mate.academy.security.jwt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
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
    private static final String TEST_EMAIL = "dima@gmail.com";
    private static final List TEST_ROLES_LIST = List.of(Role.RoleName.USER,
            Role.RoleName.ADMIN.name());
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                3600000);
    }

    @Test
    void createToken_ok() {
        String actual = jwtTokenProvider.createToken(TEST_EMAIL, TEST_ROLES_LIST);
        Assertions.assertNotNull(actual, String.format("Token can't be empty "
                + "for email %s", TEST_EMAIL));
    }

    @Test
    void getAuthentication_Ok() {
        String token = jwtTokenProvider.createToken(TEST_EMAIL, TEST_ROLES_LIST);
        UserDetails userDetails = User.builder()
                .username(TEST_EMAIL)
                .password("qwert")
                .roles("USER")
                .build();
        Mockito.when(this.userDetailsService.loadUserByUsername(TEST_EMAIL))
                .thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        Assertions.assertNotNull(actual.toString(), String.format("Expect Authentication object, "
                + "but was: %s", actual));
    }

    @Test
    void getUsername_ok() {
        String actual = jwtTokenProvider.getUsername(jwtTokenProvider
                .createToken(TEST_EMAIL, TEST_ROLES_LIST));
        Assertions.assertEquals(TEST_EMAIL, actual,
                String.format("Expect email: %s, but was: %s", TEST_EMAIL, actual));
    }

    @Test
    void resolveToken_ok() {
        String token = jwtTokenProvider.createToken(TEST_EMAIL, TEST_ROLES_LIST);
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(httpServletRequest);
        Assertions.assertEquals(token, actual, String.format("Expect token: %s, but was: %s",
                token, actual));

    }

    @Test
    void validateToken_ok() {
        String token = jwtTokenProvider.createToken(TEST_EMAIL, TEST_ROLES_LIST);
        boolean actual = jwtTokenProvider.validateToken(token);
        Assertions.assertTrue(actual, "Expect true for valid token");
    }

    @Test
    void validateTokenIsNull_notOk() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            jwtTokenProvider.validateToken(null);
        });
    }

    @Test
    void validateTokenIsInvalid_notOk() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            jwtTokenProvider.validateToken("token");
        });
    }
}

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
    private static final String EMAIL = "bob@i.ua";
    private static final List ROLES_LIST = List.of(Role.RoleName.ADMIN, Role.RoleName.USER);
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private String providerToken;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000);
        providerToken = jwtTokenProvider.createToken(EMAIL, ROLES_LIST);
    }

    @Test
    void createToken_ok() {
        String actual = jwtTokenProvider.createToken(EMAIL, ROLES_LIST);
        Assertions.assertNotNull(actual,
                String.format("Token couldn't be empty for email %s", EMAIL));
    }

    @Test
    void getAuthentication_ok() {
        UserDetails userDetails = User.builder()
                .username(EMAIL)
                .password("1234")
                .roles("USER")
                .build();
        Mockito.when(this.userDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(providerToken);
        Assertions.assertNotNull(actual.toString(),
                String.format("Should create Authentication object, but was: %s", actual));
    }

    @Test
    void getUsername_ok() {
        String actual = jwtTokenProvider.getUsername(providerToken);
        Assertions.assertEquals(EMAIL, actual,
                String.format("Should return email: %s, but was: %s", EMAIL, actual));
    }

    @Test
    void resolveToken_ok() {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(httpServletRequest.getHeader("Authorization"))
                .thenReturn("Bearer " + providerToken);
        String actual = jwtTokenProvider.resolveToken(httpServletRequest);
        Assertions.assertEquals(providerToken, actual,
                String.format("Should return token: %s, but was: %s", providerToken, actual));
    }

    @Test
    void validateToken_ok() {
        boolean actual = jwtTokenProvider.validateToken(providerToken);
        Assertions.assertTrue(actual, "Should return true for valid token");
    }

    @Test
    void validateToken_notValidToken_notOk() {
        String notValidProviderToken = "notValidToken";
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            jwtTokenProvider.validateToken(notValidProviderToken);
        });
        Assertions.assertEquals("Expired or invalid JWT token", thrown.getMessage(),
                "Should return: java.lang.RuntimeException: Expired or invalid JWT token");
    }
}

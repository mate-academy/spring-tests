package mate.academy.security.jwt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    public static final String EMAIL = "bob@gmail.com";
    public static final String PASSWORD = "12345";
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000L);
    }

    @Test
    void createToken_Ok() {
        String actual = jwtTokenProvider.createToken(EMAIL, List.of(Role.RoleName.USER.name()));
        Assertions.assertNotNull(actual);
    }

    @Test
    void getUsername_Ok() {
        String actual = jwtTokenProvider.createToken(EMAIL, List.of(Role.RoleName.USER.name()));
        String actualUsername = jwtTokenProvider.getUsername(actual);
        Assertions.assertNotNull(actualUsername);
        Assertions.assertEquals(EMAIL, actualUsername);
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn("token");
        String actual = jwtTokenProvider.resolveToken(req);
        Assertions.assertNull(actual);
    }

    @Test
    void validateToken_Ok() {
        String actual = jwtTokenProvider.createToken(EMAIL, List.of(Role.RoleName.USER.name()));
        Assertions.assertTrue(jwtTokenProvider.validateToken(actual));
    }
}

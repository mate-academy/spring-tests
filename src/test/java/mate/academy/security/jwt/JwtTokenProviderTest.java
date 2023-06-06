package mate.academy.security.jwt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String LOGIN = "bob@i.ua";
    private static final String PASSWORD = "testPass";
    private static final List<String> ROLES = List.of(Role.RoleName.USER.name());
    private static JwtTokenProvider tokenProvider;
    private static UserDetailsService userDetailsService;
    private static String token;

    @BeforeAll
    static void setUpBeforeClass() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        tokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(tokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(tokenProvider, "validityInMilliseconds", 3600000L);
        token = tokenProvider.createToken(LOGIN, ROLES);
    }

    @Test
    void createToken_ok() {
        Assertions.assertNotNull(token);
    }

    @Test
    void getAuthentication_ok() {
        UserDetails build = User.withUsername(LOGIN)
                .password(PASSWORD)
                .roles(ROLES.get(0))
                .build();
        Mockito.when(userDetailsService.loadUserByUsername(LOGIN)).thenReturn(build);
        Authentication auth = tokenProvider.getAuthentication(token);
        Assertions.assertNotNull(auth);
        Assertions.assertEquals(LOGIN, auth.getName());
    }

    @Test
    void getUsername_ok() {
        Assertions.assertEquals(LOGIN, tokenProvider.getUsername(token));
    }

    @Test
    void resolveToken_withValidParams_ok() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn("Bearer token");
        String actual = tokenProvider.resolveToken(req);
        Assertions.assertEquals("token", actual);
    }

    @Test
    void resolveToken_withoutValidParams_notOk() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn("");
        Assertions.assertNull(tokenProvider.resolveToken(req));
    }

    @Test
    void validateToken_ok() {
        Assertions.assertTrue(tokenProvider.validateToken(token));
    }

    @Test
    void validateToken_notOk() {
        Assertions.assertThrows(RuntimeException.class, () -> tokenProvider.validateToken(""));
    }
}

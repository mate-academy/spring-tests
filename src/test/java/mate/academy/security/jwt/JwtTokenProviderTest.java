package mate.academy.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String LOGIN = "test@g.com";
    private static final String PASSWORD = "12345678";
    private static final List<String> ROLES = List.of(Role.RoleName.USER.name());
    private static JwtTokenProvider tokenProvider;
    private static UserDetailsService userDetailsService;
    private static String token;

    @BeforeAll
    static void beforeAll() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        tokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(tokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(tokenProvider, "validityInMilliseconds", 3600000L);
        token = tokenProvider.createToken(LOGIN, ROLES);
    }

    @Test
    void createToken_Ok() {
        assertNotNull(token);
    }

    @Test
    void getAuthentication_Ok() {
        UserDetails details = User.withUsername(LOGIN)
                .password(PASSWORD)
                .roles(ROLES.get(0))
                .build();
        Mockito.when(userDetailsService.loadUserByUsername(LOGIN)).thenReturn(details);
        Authentication authentication = tokenProvider.getAuthentication(token);
        assertNotNull(authentication);
        assertEquals(LOGIN, authentication.getName());
    }

    @Test
    void getUsername_ok() {
        assertEquals(LOGIN, tokenProvider.getUsername(token));
    }

    void resolveToken_ValidParams_ok() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn("Bearer token");
        String actual = tokenProvider.resolveToken(req);
        assertEquals("token", actual);
    }

    @Test
    void resolveToken_invalidParams_notOk() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn("");
        assertNull(tokenProvider.resolveToken(req));
    }

    @Test
    void validateToken_ok() {
        assertTrue(tokenProvider.validateToken(token));
    }

    @Test
    void validateToken_notOk() {
        assertThrows(RuntimeException.class, () -> tokenProvider.validateToken(""));
    }

}

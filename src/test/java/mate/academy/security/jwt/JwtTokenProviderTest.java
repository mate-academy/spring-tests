package mate.academy.security.jwt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String SECRET_KEY = "secret";
    private static final Long VALIDITY_IN_MILLISECONDS = 3600000L;
    private static final String USER_LOGIN = "bchupika@mate.academy";
    private static final String USER_PASSWORD = "12345678";
    private static final String NOT_VALID_TOKEN = "Bearer token";
    private static final String TOKEN_WITHOUT_BODY = "Bearer ";
    private static final String EXPIRED_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJz"
            + "dWIiOiJiY2h1cGlrYUBtYXRlLmFjYWRlbXkiLCJyb2xlcyI6WyJVU0VSIl0s"
            + "ImlhdCI6MTY2NDY0ODc3NSwiZXhwIjoxNjY0NjUyMzc1fQ.ZjSDUMQuPKgGiwc"
            + "Trnw2DnenDPYmauHud4GmcNGOPrw";
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                VALIDITY_IN_MILLISECONDS);
    }

    @Test
    void createToken_Ok() {
        List<String> roles = List.of(Role.RoleName.USER.name());
        Assertions.assertNotNull(jwtTokenProvider.createToken(USER_LOGIN, roles));
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn(NOT_VALID_TOKEN);
        String actual = jwtTokenProvider.resolveToken(req);
        Assertions.assertNotNull(actual);
    }

    @Test
    void resolveToken_NotOk_Null() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn("");
        String actual = jwtTokenProvider.resolveToken(req);
        Assertions.assertNull(actual);
    }

    @Test
    void getAuthentication_Ok() {
        User.UserBuilder builder = User.withUsername(USER_LOGIN);
        builder.password(USER_PASSWORD);
        builder.roles("USER");
        Mockito.when(userDetailsService.loadUserByUsername(USER_LOGIN))
                .thenReturn(builder.build());
        String token = jwtTokenProvider.createToken(USER_LOGIN,
                List.of(Role.RoleName.USER.name()));
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        Assertions.assertInstanceOf(UsernamePasswordAuthenticationToken.class,
                actual);
    }

    @Test
    void getUsername_Ok() {
        String token = jwtTokenProvider.createToken(USER_LOGIN,
                List.of(Role.RoleName.USER.name()));
        String actual = jwtTokenProvider.getUsername(token);
        Assertions.assertEquals(USER_LOGIN, actual);
    }

    @Test
    void validateToken_Ok() {
        List<String> roles = List.of(Role.RoleName.USER.name());
        String token = jwtTokenProvider.createToken(USER_LOGIN, roles);
        boolean actual = jwtTokenProvider.validateToken(token);
        Assertions.assertTrue(actual);
    }

    @Test
    void validateToken_NotOk_RuntimeException() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(EXPIRED_TOKEN));
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(TOKEN_WITHOUT_BODY));
    }
}

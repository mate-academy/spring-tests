package mate.academy.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {

    private static final String VALID_USERNAME = "bob";
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private String token;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider,
                "secretKey", "google");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                3600000L);
        token = jwtTokenProvider.createToken(VALID_USERNAME, List.of(Role.RoleName.USER.name()));
    }

    @Test
    void createToken_Ok() {
        String actual = jwtTokenProvider.createToken(VALID_USERNAME,
                List.of(Role.RoleName.USER.name()));
        assertNotNull(actual);
        assertEquals(token, actual);
    }

    @Test
    void authentication_Ok() {
        User.UserBuilder userBuilder = User.withUsername(VALID_USERNAME);
        userBuilder.password("1234");
        userBuilder.roles(Role.RoleName.USER.name());
        Mockito.when(userDetailsService.loadUserByUsername(VALID_USERNAME))
                .thenReturn(userBuilder.build());
        assertNotNull(jwtTokenProvider.getAuthentication(token));
    }

    @Test
    void getUserName_Ok() {
        String username = jwtTokenProvider.getUsername(token);
        assertNotNull(username);
        assertEquals(VALID_USERNAME, username);
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization"))
                .thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(req);
        assertNotNull(actual);
        assertEquals(token, actual);
    }

    @Test
    void resolveToken_IncorrectToken_NotOk() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization"))
                .thenReturn("Bearer " + "IncorrectToken");
        String actual = jwtTokenProvider.resolveToken(req);
        assertEquals("IncorrectToken", actual);
    }

    @Test
    void validateToken_Ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_IncorrectToken_NotOk() {
        RuntimeException
                runtimeException = assertThrows(RuntimeException.class, () ->
                        jwtTokenProvider.validateToken("incorrect token"),
                "Expected RuntimeException");
        assertEquals("Expired or invalid JWT token",
                runtimeException.getLocalizedMessage());
    }
}

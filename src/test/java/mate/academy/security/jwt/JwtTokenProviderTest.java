package mate.academy.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

public class JwtTokenProviderTest {
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9."
            + "eyJzdWIiOiJsb2dpbkBkb21haW4uY29tIiwicm9sZXMiOlsiVV"
            + "NFUiJdLCJpYXQiOjE2NjI4MjU3NTYsImV4cCI6MTY2MjgyOTM1Nn0."
            + "bDT69C53iK9vlTVlRTefeh6nQyPJV8771XS1wE0_WhE";
    private static final String LOGIN = "login@domain.com";
    private static final String PASSWORD = "12345678";
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() throws Exception {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider,
                "secretKey", "secret", String.class);
        ReflectionTestUtils.setField(jwtTokenProvider,
                "validityInMilliseconds", 3600000L, long.class);
    }

    @Test
    public void createToken_Ok() {
        List<String> roles = List.of(Role.RoleName.USER.name());
        String token = jwtTokenProvider.createToken(LOGIN, roles);
        System.out.println(token);
        assertNotNull(token);
        assertEquals(168, token.length());
    }

    @Test
    public void getAuthentication_Ok() {
        UserDetails userDetails = User.withUsername(LOGIN)
                .password(PASSWORD)
                .roles(Role.RoleName.USER.name())
                .build();
        Mockito.when(userDetailsService.loadUserByUsername(LOGIN)).thenReturn(userDetails);
        UsernamePasswordAuthenticationToken expected =
                new UsernamePasswordAuthenticationToken(userDetails, "",
                        userDetails.getAuthorities());
        Authentication actual = jwtTokenProvider.getAuthentication(TOKEN);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void getUsername_Ok() {
        String actual = jwtTokenProvider.getUsername(TOKEN);
        assertNotNull(actual);
        assertEquals(LOGIN, actual);
    }

    @Test
    public void resolveToken_Ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + TOKEN);
        String actual = jwtTokenProvider.resolveToken(request);
        assertNotNull(actual);
        assertEquals(TOKEN, actual);
    }

    @Test
    public void validateToken_Ok() {
        assertTrue(jwtTokenProvider.validateToken(TOKEN));
    }

    @Test
    public void validateToken_NotOk() {
        String invalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9."
                + "eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIi"
                + "wiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fw"
                + "pMeJf36POk6yJV_adQssw5c";
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(invalidToken));
        assertEquals("Expired or invalid JWT token", exception.getMessage());
    }
}

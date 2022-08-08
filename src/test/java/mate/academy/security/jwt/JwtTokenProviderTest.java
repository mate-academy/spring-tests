package mate.academy.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private String login;
    private String password;
    private String token;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        request = Mockito.mock(HttpServletRequest.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "den.shl");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000);
        jwtTokenProvider.init();
        login = "denys@gmail.com";
        password = "qaz!23";
        token = jwtTokenProvider.createToken(login, List.of(Role.RoleName.USER.name()));
    }

    @Test
    void createToken_valid_Ok() {
        String actualToken
                = jwtTokenProvider.createToken(login, List.of(Role.RoleName.USER.name()));
        assertNotNull(actualToken);
        String[] arr = actualToken.split("\\.");
        assertEquals(3, arr.length);
    }

    @Test
    void getAuthentication_valid_Ok() {
        UserDetails userDetails = User.withUsername(login)
                .password(password)
                .roles(Role.RoleName.USER.name())
                .build();
        Mockito.when(userDetailsService.loadUserByUsername(login)).thenReturn(userDetails);
        Authentication authenticationActual = jwtTokenProvider.getAuthentication(token);
        assertNotNull(authenticationActual);
        assertEquals(login, authenticationActual.getName());
        User actualPrincipal = (User) authenticationActual.getPrincipal();
        assertEquals(login, actualPrincipal.getUsername());
        assertEquals(password, actualPrincipal.getPassword());
        assertEquals("ROLE_USER", actualPrincipal.getAuthorities()
                .stream()
                .parallel()
                .findFirst()
                .get()
                .getAuthority());
    }

    @Test
    void getUsername_validUsername_Ok() {
        String actual = jwtTokenProvider.getUsername(token);
        assertEquals(login, actual);
    }

    @Test
    void resolveToken_valid_OK() {
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        String bearerToken = request.getHeader("Authorization");
        assertTrue(bearerToken.startsWith("Bearer "));
        String resolveToken = jwtTokenProvider.resolveToken(request);
        assertEquals(token, resolveToken);
    }

    @Test
    void resolveToken_notPresent_notOk() {
        Mockito.when(request.getHeader("Authorization")).thenReturn("Basic");
        String resolveTokenFalse = jwtTokenProvider.resolveToken(request);
        assertNull(resolveTokenFalse);
    }

    @Test
    void validateToken_Ok() {
        boolean validateToken = jwtTokenProvider.validateToken(token);
        assertTrue(validateToken);
    }

    @Test
    void validateToken_expected_Exception_notOk() {
        assertThrows(RuntimeException.class, () -> {
            jwtTokenProvider.validateToken("sdD$gse^rg.r34252525wt4fr+g.df");
        });
    }

    @Test
    void validateToken_expected_InvalidJwtAuthenticationException_notOk() {
        try {
            jwtTokenProvider.validateToken("");
        } catch (RuntimeException e) {
            assertEquals("Expired or invalid JWT token", e.getMessage());
            return;
        }
        fail("Expected to receive InvalidJwtAuthenticationException");
    }
}

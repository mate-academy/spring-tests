package mate.academy.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {
    private static final String EMAIL = "test@ukr.net";
    private static final String PASSWORD = "12345";
    private static final String secretKey = "secret";
    private static final long validityInMilliseconds = 3600000L;
    private static final List<String> ROLES = List.of("USER");
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private UserDetailsService userDetailsService;
    private String token;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtTokenProvider,
                "validityInMilliseconds", validityInMilliseconds);
        token = jwtTokenProvider.createToken(EMAIL, ROLES);
    }

    @Test
    void createToken_validValues_ok() {
        assertNotNull(jwtTokenProvider.createToken(EMAIL, ROLES),
                "method should not return null with valid email and roles");
    }

    @Test
    void getAuthentication_validToken_ok() {
        UserDetails userDetails = User.withUsername(EMAIL)
                                      .password(PASSWORD)
                                      .roles(ROLES.toString())
                                      .build();
        Mockito.when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);
        assertNotNull(jwtTokenProvider.getAuthentication(token),
                "method should not return null with valid User data");
        assertEquals(userDetails, jwtTokenProvider.getAuthentication(token).getPrincipal(),
                "method should return Authentication with valid User email");
    }

    @Test
    void getUsername_validToken_ok() {
        assertNotNull(jwtTokenProvider.getUsername(token),
                "method should not return null with valid token");
        assertEquals(EMAIL, jwtTokenProvider.getUsername(token),
                "method should return existed email with valid token");
    }

    @Test
    void resolveToken_validToken_ok(@Mock HttpServletRequest req) {
        Mockito.when(req.getHeader("Authorization")).thenReturn("Bearer " + token);
        assertEquals(token, jwtTokenProvider.resolveToken(req),
                "method should return valid token from HttpServletRequest");
    }

    @Test
    void resolveToken_nullOrInvalidToken_notOk(@Mock HttpServletRequest req) {
        Mockito.when(req.getHeader("Authorization")).thenReturn("");
        assertNull(jwtTokenProvider.resolveToken(req),
                "method should return null when no token in HttpServletRequest");
        Mockito.when(req.getHeader("Authorization")).thenReturn(null);
        assertNull(jwtTokenProvider.resolveToken(req),
                "method should return null when no token is null");
    }

    @Test
    void validateToken_validToken_ok() {
        assertTrue(jwtTokenProvider.validateToken(token),
                "method should return true when token is valid");
    }

    @Test
    void validateToken_nullOrInvalidToken_notOk() {
        assertThrows(RuntimeException.class, () -> jwtTokenProvider.validateToken(null),
                "method should throw RuntimeException when token is null");
        assertThrows(RuntimeException.class, () -> jwtTokenProvider.validateToken(""),
                "method should throw RuntimeException when token is invalid");
    }

}

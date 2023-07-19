package mate.academy.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;

class JwtTokenProviderTest {
    private static final String VALID_USERNAME = "Taras";
    private static final String INVALID_USERNAME = "Andrew";
    private UserDetailsService userDetailsService = Mockito.mock(UserDetailsService.class);
    private JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(userDetailsService);
    private String token;

    @BeforeEach
    void setUp() {
        setField(jwtTokenProvider, "secretKey", "secret");
        setField(jwtTokenProvider, "validityInMilliseconds",
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
    void getUsername_Ok() {
        String actual = jwtTokenProvider.getUsername(token);
        assertEquals(VALID_USERNAME, actual);
    }

    @Test
    void getUsername_Not_Ok() {
        String actual = jwtTokenProvider.getUsername(token);
        assertNotEquals(INVALID_USERNAME, actual);
    }

    @Test
    void validateToken_Ok() {
        boolean actual = jwtTokenProvider.validateToken(token);
        assertTrue(actual);
    }

    @Test
    void validateToken() {
        assertThrows(RuntimeException.class, () ->
                jwtTokenProvider.validateToken("Wrong token"));
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(request);
        assertEquals(token, actual);
    }

    @Test
    void resolveToken_Incorrect_Token_Not_Ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + "Wrong token");
        String actual = jwtTokenProvider.resolveToken(request);
        assertNotEquals(token, actual);
    }
}

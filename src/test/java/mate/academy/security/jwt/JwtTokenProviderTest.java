package mate.academy.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.ReflectionTestUtils.setField;

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

public class JwtTokenProviderTest {
    private static final String TEST_USERNAME_OK = "Artur";
    private static final String TEST_USERNAME_NOT_OK = "somedude";
    private UserDetailsService userDetailsService = Mockito.mock(UserDetailsService.class);
    private JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(userDetailsService);
    private String token;

    @BeforeEach
    void setUp() {
        setField(jwtTokenProvider, "secretKey", "secret");
        setField(jwtTokenProvider, "validityInMilliseconds",
                3600000L);
        token = jwtTokenProvider.createToken(TEST_USERNAME_OK, List.of(Role.RoleName.USER.name()));
    }

    @Test
    void createToken_Ok() {
        String actual = jwtTokenProvider.createToken(TEST_USERNAME_OK,
                List.of(Role.RoleName.USER.name()));

        assertNotNull(actual);
        assertEquals(token, actual);
    }

    @Test
    void createToken_With_Different_Role_Not_Ok() {
        String actual = jwtTokenProvider.createToken(TEST_USERNAME_OK,
                List.of(Role.RoleName.ADMIN.name()));
        assertNotNull(actual);
        assertNotEquals(token, actual);
    }

    @Test
    void createToken_With_Different_Username_Not_Ok() {
        String actual = jwtTokenProvider.createToken(TEST_USERNAME_NOT_OK,
                List.of(Role.RoleName.USER.name()));
        assertNotNull(actual);
        assertNotEquals(token, actual);
    }

    @Test
    void getAuthentication_Ok() {
        String expectedPassword = "1234";
        User.UserBuilder user = User.withUsername(TEST_USERNAME_OK)
                .password(expectedPassword).roles(Role.RoleName.USER.name());
        Mockito.when(userDetailsService.loadUserByUsername(TEST_USERNAME_OK))
                .thenReturn(user.build());
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        assertNotNull(actual);
        String actualUsername = ((UserDetails) actual.getPrincipal()).getUsername();
        String actualPassword = ((UserDetails) actual.getPrincipal()).getPassword();
        assertEquals(TEST_USERNAME_OK, actualUsername);
        assertEquals(expectedPassword, actualPassword);
    }

    @Test
    void getUsername_Ok() {
        String actual = jwtTokenProvider.getUsername(token);
        assertEquals(TEST_USERNAME_OK, actual);
    }

    @Test
    void getUsername_Not_Ok() {
        String actual = jwtTokenProvider.getUsername(token);
        assertNotEquals(TEST_USERNAME_NOT_OK, actual);
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

    @Test
    void resolveToken_Null_Token_Not_Ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn(null);
        String actual = jwtTokenProvider.resolveToken(request);
        assertNull(actual);
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
}

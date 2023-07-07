package mate.academy.security.jwt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String TEST_USERNAME_OK = "Artem";
    private static final String TEST_USERNAME_NOT_OK = "Dima";
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;
    private String token;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                3600000L);
        token = jwtTokenProvider.createToken(TEST_USERNAME_OK, List.of(Role.RoleName.USER.name()));
    }

    @Test
    void createToken_Ok() {
        String actual = jwtTokenProvider.createToken(TEST_USERNAME_OK,
                List.of(Role.RoleName.USER.name()));
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(token, actual);
    }

    @Test
    void createToken_With_Different_Role_Not_Ok() {
        String actual = jwtTokenProvider.createToken(TEST_USERNAME_OK,
                List.of(Role.RoleName.ADMIN.name()));
        Assertions.assertNotNull(actual);
        Assertions.assertNotEquals(token, actual);
    }

    @Test
    void createToken_With_Different_Username_Not_Ok() {
        String actual = jwtTokenProvider.createToken(TEST_USERNAME_NOT_OK,
                List.of(Role.RoleName.USER.name()));
        Assertions.assertNotNull(actual);
        Assertions.assertNotEquals(token, actual);
    }

    @Test
    void getAuthentication_Ok() {
        String expectedPassword = "1234";
        User.UserBuilder user = User.withUsername(TEST_USERNAME_OK)
                .password(expectedPassword).roles(Role.RoleName.USER.name());
        Mockito.when(userDetailsService.loadUserByUsername(TEST_USERNAME_OK))
                .thenReturn(user.build());
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        Assertions.assertNotNull(actual);
        String actualUsername = ((UserDetails) actual.getPrincipal()).getUsername();
        String actualPassword = ((UserDetails) actual.getPrincipal()).getPassword();
        Assertions.assertEquals(TEST_USERNAME_OK, actualUsername);
        Assertions.assertEquals(expectedPassword, actualPassword);
    }

    @Test
    void getUsername_Ok() {
        String actual = jwtTokenProvider.getUsername(token);
        Assertions.assertEquals(TEST_USERNAME_OK, actual);
    }

    @Test
    void getUsername_Not_Ok() {
        String actual = jwtTokenProvider.getUsername(token);
        Assertions.assertNotEquals(TEST_USERNAME_NOT_OK, actual);
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertEquals(token, actual);
    }

    @Test
    void resolveToken_Incorrect_Token_Not_Ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + "Wrong token");
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNotEquals(token, actual);
    }

    @Test
    void resolveToken_Null_Token_Not_Ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn(null);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNull(actual);
    }

    @Test
    void validateToken_Ok() {
        boolean actual = jwtTokenProvider.validateToken(token);
        Assertions.assertTrue(actual);
    }

    @Test
    void validateToken() {
        Assertions.assertThrows(RuntimeException.class, () ->
                jwtTokenProvider.validateToken("Wrong token"));
    }
}

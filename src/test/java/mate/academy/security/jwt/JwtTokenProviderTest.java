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
    private static final String VALID_USERNAME = "bchupika@mate.academy";
    private static final String VALID_PASSWORD = "1234567";
    private static final List<String> ROLES = List.of(Role.RoleName.USER.name());
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private String token;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider,
                "secretKey", "123456");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                3600000L);
        token = jwtTokenProvider.createToken(VALID_USERNAME, ROLES);
    }

    @Test
    void getAuthentication_Ok() {
        UserDetails userDetails;
        User.UserBuilder builder;
        builder = org.springframework.security.core.userdetails.User.withUsername(VALID_USERNAME)
                .password(VALID_PASSWORD)
                .roles(ROLES.toString());
        userDetails = builder.build();
        Mockito.when(userDetailsService.loadUserByUsername(VALID_USERNAME)).thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        String actualUsername = ((UserDetails) actual.getPrincipal()).getUsername();
        String actualPassword = ((UserDetails) actual.getPrincipal()).getPassword();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(VALID_USERNAME, actualUsername);
        Assertions.assertEquals(VALID_PASSWORD, actualPassword);
    }

    @Test
    void createToken_Ok() {
        Assertions.assertNotNull(token);
        Assertions.assertTrue(token.length() > 0);
    }

    @Test
    void getUsername_Ok() {
        String actualUsername = jwtTokenProvider.getUsername(token);
        Assertions.assertNotNull(actualUsername);
        Assertions.assertTrue(actualUsername.length() > 0);
        Assertions.assertEquals(VALID_USERNAME, actualUsername);
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(token, actual);
    }

    @Test
    void validateToken_Ok() {
        boolean actual = jwtTokenProvider.validateToken(token);
        Assertions.assertTrue(actual);
    }

    @Test
    void resolveToken_IncorrectToken_NotOk() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + "Wrong token");
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNotEquals(token, actual);
    }

    @Test
    void validateToken_tokenIsNull_NotOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(null));
    }
}

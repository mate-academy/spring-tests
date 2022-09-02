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
    private static final String SECRET_KEY = "secret";
    private static final Long VALIDITY_IN_MILLISECONDS = 3600000L;
    private static final String USER_LOGIN_TEST = "bob@i.ua";
    private static final String USER_PASSWORD_TEST = "1234";
    private static final int ACTUAL_SIZE = 3;
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;
    private String login;
    private String token;
    private String password;

    @BeforeEach
    public void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                VALIDITY_IN_MILLISECONDS);
        jwtTokenProvider.init();
        login = USER_LOGIN_TEST;
        password = USER_PASSWORD_TEST;
        token = jwtTokenProvider.createToken(login, List.of(Role.RoleName.USER.name()));
    }

    @Test
    public void createToken_ok() {
        String actualToken = jwtTokenProvider.createToken(login,
                List.of(Role.RoleName.USER.name()));
        Assertions.assertNotNull(actualToken);
        String[] split = actualToken.split("[.]");
        Assertions.assertTrue(split.length == ACTUAL_SIZE);
    }

    @Test
    public void getAuthentication_ok() {
        UserDetails userDetails = User.withUsername(login).password(password)
                .roles(Role.RoleName.USER.name()).build();
        Mockito.when(userDetailsService.loadUserByUsername(login)).thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        Assertions.assertNotNull(actual);
        User actualUser = (User) actual.getPrincipal();
        Assertions.assertEquals(login, actual.getName());
        Assertions.assertEquals(password, actualUser.getPassword());
    }

    @Test
    public void getUserName_ok() {
        String actual = jwtTokenProvider.getUsername(token);
        Assertions.assertEquals(login, actual);
    }

    @Test
    public void resolveToken_ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertEquals(token, actual);
    }

    @Test
    public void validateToken_ok() {
        boolean actual = jwtTokenProvider.validateToken(token);
        Assertions.assertTrue(actual);
    }
}

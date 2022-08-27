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
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;
    private String login;
    private String token;
    private String password;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000L);
        jwtTokenProvider.init();
        login = "bob@i.ua";
        password = "1234";
        token = jwtTokenProvider.createToken(login, List.of(Role.RoleName.USER.name()));
    }

    @Test
    void createToken_Ok() {
        String actualToken = jwtTokenProvider.createToken(login,
                List.of(Role.RoleName.USER.name()));
        Assertions.assertNotNull(actualToken);
        String[] split = actualToken.split("[.]");
        Assertions.assertTrue(split.length == 3);
    }

    @Test
    void getAuthentication_Ok() {
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
    void getUserName_Ok() {
        String actual = jwtTokenProvider.getUsername(token);
        Assertions.assertEquals(login, actual);
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertEquals(token, actual);
    }

    @Test
    void validateToken_Ok() {
        boolean actual = jwtTokenProvider.validateToken(token);
        Assertions.assertTrue(actual);
    }
}

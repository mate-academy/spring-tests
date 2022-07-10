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

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000L);
        jwtTokenProvider.init();
        login = "bob@i.ua";
        token = jwtTokenProvider.createToken(login, List.of(Role.RoleName.USER.name()));
    }

    @Test
    void createToken_Ok() {
        String actualToken = jwtTokenProvider
                .createToken(login, List.of(Role.RoleName.USER.name()));
        Assertions.assertNotNull(actualToken);
        String[] split = actualToken.split("[.]");
        Assertions.assertTrue(split.length == 3);
    }

    @Test
    void getAuthentication_Ok() {
        User.UserBuilder builder;
        builder = org.springframework.security.core.userdetails.User.withUsername(login);
        builder.password(new String("1234"));
        builder.roles(Role.RoleName.USER.name());
        UserDetails userDetails = builder.build();
        Mockito.when(userDetailsService.loadUserByUsername(login)).thenReturn(userDetails);

        Authentication actual = jwtTokenProvider.getAuthentication(token);
        Assertions.assertNotNull(actual);
    }

    @Test
    void getUsername_Ok() {
        String actual = jwtTokenProvider.getUsername(token);
        Assertions.assertEquals(login, actual);
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest reg = Mockito.mock(HttpServletRequest.class);
        Mockito.when(reg.getHeader("Authorization")).thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(reg);
        Assertions.assertEquals(token, actual);
    }

    @Test
    void validateToken_Ok() {
        boolean actual = jwtTokenProvider.validateToken(token);
        Assertions.assertTrue(actual);
    }
}

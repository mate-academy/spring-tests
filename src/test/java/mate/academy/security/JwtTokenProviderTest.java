package mate.academy.security;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import mate.academy.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String USER_LOGIN = "bob123@gmail.com";
    private static final String USER_PASSWORD = "bob123";
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000L);
    }

    @Test
    void createToken_Ok() {
        String actual = jwtTokenProvider
                .createToken(USER_LOGIN, List.of(Role.RoleName.USER.name()));
        Assertions.assertNotNull(actual);
    }

    @Test
    void getAuthentication_Ok() {
        UserDetails userDetails = User.withUsername(USER_LOGIN)
                .password(USER_PASSWORD).roles(Role.RoleName.USER.name()).build();
        Mockito.when(userDetailsService.loadUserByUsername(USER_LOGIN)).thenReturn(userDetails);
        String token = jwtTokenProvider.createToken(USER_LOGIN, List.of(Role.RoleName.USER.name()));
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        Authentication expected = new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getUsername_Ok() {
        String token = jwtTokenProvider.createToken(USER_LOGIN, List.of(Role.RoleName.USER.name()));
        Assertions.assertEquals(USER_LOGIN, jwtTokenProvider.getUsername(token));
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("token");
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNull(actual);
    }

    @Test
    void validateToken_Ok() {
        String token = jwtTokenProvider.createToken(USER_LOGIN, List.of(Role.RoleName.USER.name()));
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateTokenNullValue_NotOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(null));
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken("wrong token"));
    }
}

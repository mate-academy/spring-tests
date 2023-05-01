package mate.academy.security.jwt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String VALID_USERNAME = "bob@i.ua";
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private String token;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider,
                "secretKey","secret");
        ReflectionTestUtils.setField(jwtTokenProvider,
                "validityInMilliseconds", 3600000L);
        token = jwtTokenProvider.createToken(VALID_USERNAME, List.of(Role.RoleName.USER.name()));
    }

    @Test
    void createToken_Ok() {
        String actual = jwtTokenProvider.createToken(
                VALID_USERNAME, List.of(Role.RoleName.USER.name()));
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(token, actual);
    }

    @Test
    void authentication_Ok() {
        User.UserBuilder userBuilder = User.withUsername(VALID_USERNAME);
        userBuilder.password("1234");
        userBuilder.roles(Role.RoleName.USER.name());
        Mockito.when(userDetailsService.loadUserByUsername(VALID_USERNAME))
                .thenReturn(userBuilder.build());
        Assertions.assertNotNull(jwtTokenProvider.getAuthentication(token));
    }

    @Test
    void username_Ok() {
        String actual = jwtTokenProvider.getUsername(token);
        Assertions.assertEquals(VALID_USERNAME, actual);
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertEquals(token, actual);
    }

    @Test
    void validateToken_Ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void authenticationNull_NotOk() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            jwtTokenProvider.getAuthentication(null);
        });
    }

    @Test
    void username_NotOk() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            jwtTokenProvider.getUsername(null);
        });
    }

    @Test
    void resolveToken_WrongToken_NotOk() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + "invalid_token");
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertEquals("invalid_token", actual);
    }

    @Test
    void validateTokenNull_NotOk() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            jwtTokenProvider.validateToken(null);
        });
    }
}

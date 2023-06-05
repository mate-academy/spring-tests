package mate.academy.security;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import mate.academy.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String VALID_USERNAME = "bob";
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private String token;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider,
                "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider,
                "validityInMilliseconds", 3600000L);
        token = jwtTokenProvider.createToken(VALID_USERNAME, List.of(Role.RoleName.USER.name()));
    }

    @Test
    void createToken_Ok() {
        String actual = jwtTokenProvider.createToken(VALID_USERNAME,
                List.of(Role.RoleName.USER.name()));
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(token, actual);
    }

    @Test
    void getAuthentication_Ok() {
        User.UserBuilder user = User.withUsername(VALID_USERNAME);
        user.password("123456");
        user.roles(Role.RoleName.USER.name());
        Mockito.when(userDetailsService.loadUserByUsername(VALID_USERNAME))
                .thenReturn(user.build());
        Assertions.assertNotNull(jwtTokenProvider.getAuthentication(token));
    }

    @Test
    void getUsername_Ok() {
        String actual = jwtTokenProvider.getUsername(token);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(VALID_USERNAME, actual);
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(token, actual);
    }

    @Test
    void resolveToken_IncorrectToken_NotOk() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + "Incorrect token");
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNotEquals(token, actual);
    }

    @Test
    void validateToken_Ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_IncorrectToken_NotOk() {
        RuntimeException runtimeException = Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken("Incorrect token"));
        Assertions.assertEquals("Expired or invalid JWT token",
                runtimeException.getMessage());
    }
  
}

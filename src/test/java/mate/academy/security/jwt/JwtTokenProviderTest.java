package mate.academy.security.jwt;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import org.junit.Assert;
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
    private final String secret = "secretKey";
    private final Long validityInMilliseconds = 3600000L;
    private final String userName = "bob@i.ua";
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", secret);
        ReflectionTestUtils.setField(jwtTokenProvider,
                "validityInMilliseconds", validityInMilliseconds);
        jwtTokenProvider.init();
    }

    @Test
    void createToken_ok() {
        String actual = jwtTokenProvider.createToken(userName, List.of(Role.RoleName.USER.name()));
        Assert.assertNotNull(actual);
    }

    @Test
    void getAuthentication_ok() {
        User.UserBuilder builder = org.springframework.security.core
                .userdetails.User.withUsername(userName);
        builder.password("1234");
        builder.roles(Set.of(new Role(Role.RoleName.USER))
                .stream()
                .map(x -> x.getRoleName().name())
                .toArray(String[]::new));
        UserDetails userDetails = builder.build();
        doReturn(userDetails).when(userDetailsService).loadUserByUsername(userName);
        jwtTokenProvider = Mockito.spy(jwtTokenProvider);
        doReturn(userName).when(jwtTokenProvider).getUsername(any());
        Authentication actual = jwtTokenProvider.getAuthentication("some token");
        Assertions.assertNotNull(actual);
    }

    @Test
    void getUsername_ok() {
        String token = jwtTokenProvider.createToken(userName, List.of("USER"));
        String actualUserName = jwtTokenProvider.getUsername(token);
        Assert.assertEquals(userName, actualUserName);
    }

    @Test
    void resolveToken_ok() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn("Bearer valid token");
        String actualToken = jwtTokenProvider.resolveToken(req);
        Assertions.assertEquals("valid token", actualToken);
    }

    @Test
    void resolveToken_notOk() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn("wrong token");
        String actualToken = jwtTokenProvider.resolveToken(req);
        Assertions.assertNull(actualToken);
    }

    @Test
    void validateToken_Ok() {
        String token = jwtTokenProvider.createToken(userName, List.of(Role.RoleName.USER.name()));
        try {
            Boolean actual = jwtTokenProvider.validateToken(token);
            Assertions.assertTrue(actual);
        } catch (RuntimeException e) {
            Assertions.fail("NOT Expected to receive RuntimeException");
        }
    }

    @Test
    void validateToken_Expired_notOk() {
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 1);
        String token = jwtTokenProvider.createToken(userName, List.of("USER"));
        try {
            Boolean actual = jwtTokenProvider.validateToken(token);
        } catch (RuntimeException e) {
            Assertions.assertEquals("Expired or invalid JWT token", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive RuntimeException");
    }

    @Test
    void validateToken_invalidToken_notOk() {
        try {
            Boolean actual = jwtTokenProvider.validateToken("wrong token");
        } catch (RuntimeException e) {
            Assertions.assertEquals("Expired or invalid JWT token", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive RuntimeException");
    }
}

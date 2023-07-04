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
    private static final String TEST_USERNAME_OK = "Bob";
    private static final String TEST_USERNAME_NotOK = "Alice";

    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private String token;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider,"secretKey","12345678");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                3600000L);
        token = jwtTokenProvider.createToken(TEST_USERNAME_OK, List.of(Role.RoleName.USER.name()));
    }

    @Test
    void createToken_OK() {
        String actual = jwtTokenProvider.createToken(
                TEST_USERNAME_OK,List.of(Role.RoleName.USER.name()));
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(token,actual);
    }

    @Test
    void getAuthentication_OK() {
        User.UserBuilder userBuilder = User.withUsername(TEST_USERNAME_OK);
        userBuilder.password("12345678");
        userBuilder.roles(Role.RoleName.USER.name());
        Mockito.when(userDetailsService
                .loadUserByUsername(TEST_USERNAME_OK))
                .thenReturn(userBuilder.build());
        Assertions.assertNotNull(jwtTokenProvider.getAuthentication(token));
    }

    @Test
    void getUsername_OK() {
        String actual = jwtTokenProvider.getUsername(token);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(TEST_USERNAME_OK,actual);
    }

    @Test
    void resolveToken_OK() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(req);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(token,actual);
    }

    @Test
    void validateToken_OK() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }
}

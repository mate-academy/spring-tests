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
        token = jwtTokenProvider.createToken("alice@com.ua", List.of(Role.RoleName.USER.name()));
    }

    @Test
    void createToken_ok() {
        String actual = jwtTokenProvider.createToken("alice@com.ua",
                List.of(Role.RoleName.USER.name()));
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(token, actual);
    }

    @Test
    void getAuthentication_ok() {
        User.UserBuilder userBuilder = User.withUsername("alice@com.ua");
        userBuilder.password("1234");
        userBuilder.roles(Role.RoleName.USER.name());
        Mockito.when(userDetailsService.loadUserByUsername("alice@com.ua"))
                .thenReturn(userBuilder.build());
        Assertions.assertNotNull(jwtTokenProvider.getAuthentication(token));
    }

    @Test
    void getUsername_ok() {
        String actual = jwtTokenProvider.getUsername(token);
        Assertions.assertEquals("alice@com.ua", actual);
    }

    @Test
    void resolveToken_ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertEquals(token, actual);
    }

    @Test
    void validateToken_ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }
}

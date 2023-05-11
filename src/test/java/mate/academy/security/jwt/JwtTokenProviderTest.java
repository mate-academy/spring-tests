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
    private String email = "bob@i.ua";
    private String password = "123456";
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private String token;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000L);
        token = jwtTokenProvider.createToken(email, List.of(Role.RoleName.USER.name()));
    }

    @Test
    void createToken_Ok() {
        Assertions.assertNotNull(token);
    }

    @Test
    void getAuthentication_Ok() {
        User.UserBuilder userBuilder = User.withUsername(email);
        userBuilder.password(password);
        userBuilder.roles(Role.RoleName.USER.name());
        Mockito.when(userDetailsService.loadUserByUsername(email))
                .thenReturn(userBuilder.build());
        Assertions.assertNotNull(jwtTokenProvider.getAuthentication(token));
    }

    @Test
    void getUsername_Ok() {
        Assertions.assertEquals(jwtTokenProvider.getUsername(token), email);
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertEquals(actual, token);
    }

    @Test
    void validateToken_Ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_NotOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(""));
    }

    @Test
    void getUsername_NotOk() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> jwtTokenProvider.getUsername(null));
    }
}

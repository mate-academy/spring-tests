package mate.academy.security.jwt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String SECRET_KEY = "secret";
    private static final long VALIDITY_IN_MILLISECONDS = 3600000;
    private static final String USERNAME = "test@gmail.com";
    private static final String PASSWORD = "123qwe";
    private static final String ROLE = "USER";
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private String token;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtTokenProvider,
                "validityInMilliseconds", VALIDITY_IN_MILLISECONDS);
        token = jwtTokenProvider.createToken(USERNAME, List.of(ROLE));
    }

    @Test
    void createToken_ok() {
        Assertions.assertNotNull(jwtTokenProvider.createToken(USERNAME, List.of(ROLE)));
    }

    @Test
    void getAuthentication_ok() {
        UserBuilder userBuilder = User.withUsername(USERNAME);
        userBuilder.password(PASSWORD);
        userBuilder.roles(ROLE);
        Mockito.when(userDetailsService.loadUserByUsername(USERNAME))
                .thenReturn(userBuilder.build());
        Assertions.assertNotNull(jwtTokenProvider.getAuthentication(token));
    }

    @Test
    void getUsername_ok() {
        Assertions.assertEquals(USERNAME, jwtTokenProvider.getUsername(token));
    }

    @Test
    void resolveToken_ok() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn("Bearer " + token);

        Assertions.assertEquals(token, jwtTokenProvider.resolveToken(req));
    }

    @Test
    void validateToken_ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_notOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken("Not a token"));
    }
}

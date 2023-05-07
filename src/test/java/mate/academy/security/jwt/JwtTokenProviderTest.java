package mate.academy.security.jwt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String LOGIN = "bob@i.ua";
    private static final String PASSWORD = "1234";
    private static final String ROLE_NAME = "USER";
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
        token = jwtTokenProvider.createToken(LOGIN, List.of(ROLE_NAME));
    }

    @Test
    void createToken_ok() {
        String actual = jwtTokenProvider.createToken(LOGIN, List.of(ROLE_NAME));
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(token, actual);
    }

    @Test
    void getAuthentication_ok() {
        User.UserBuilder userBuilder = User.withUsername(LOGIN);
        userBuilder.password(PASSWORD);
        userBuilder.roles(ROLE_NAME);
        Mockito.when(userDetailsService.loadUserByUsername(LOGIN))
                .thenReturn(userBuilder.build());
        Assertions.assertNotNull(jwtTokenProvider.getAuthentication(token));
    }

    @Test
    void getUsername_ok() {
        String actual = jwtTokenProvider.getUsername(token);
        Assertions.assertEquals(LOGIN, actual);
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

package mate.academy.security.jwt;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String SECRET_KEY = "secret";
    private static final Long VALIDITY = 3600000L;
    private static final String USER_NAME = "userName";
    private static final String USER_PASSWORD = "password";
    private static final List<String> USER_ROLES_LIST = List.of("USER");
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private String token;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", VALIDITY);
        jwtTokenProvider.init();
        token = jwtTokenProvider.createToken(USER_NAME, USER_ROLES_LIST);
    }

    @Test
    void createToken_ok() {
        Assertions.assertNotNull(token);
    }

    @Test
    void getAuthentication_ok() {
        UserBuilder builder = User.withUsername(USER_NAME);
        builder.password(USER_PASSWORD);
        builder.roles(USER_ROLES_LIST
                .stream()
                .toArray(String[]::new));
        UserDetails userDetails = builder.build();
        Mockito.when(userDetailsService.loadUserByUsername(USER_NAME)).thenReturn(userDetails);
        jwtTokenProvider = Mockito.spy(jwtTokenProvider);
        doReturn(USER_NAME).when(jwtTokenProvider).getUsername(any());
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        Assertions.assertNotNull(actual);
    }

    @Test
    void getUsername_ok() {
        String actual = jwtTokenProvider.getUsername(token);
        Assertions.assertEquals(USER_NAME, actual);
    }

    @Test
    void resolveToken_ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertEquals(token, actual);
    }

    @Test
    void resolveToken_NotBearerToken_Null() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("NotBearer");
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNull(actual);
    }

    @Test
    void validateToken_ok() {
        try {
            Assertions.assertEquals(jwtTokenProvider.validateToken(token), true);
        } catch (RuntimeException exception) {
            Assertions.fail("There isn't expected any exception!");
        }
    }

    @Test
    void validateToken_ExpiredToken_ExceptionExpected() {
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 0);
        token = jwtTokenProvider.createToken(USER_NAME, USER_ROLES_LIST);
        try {
            boolean actual = jwtTokenProvider.validateToken(token);
        } catch (RuntimeException exception) {
            Assertions.assertEquals("Expired or invalid JWT token", exception.getMessage());
            return;
        }
        Assertions.fail("JwtException or IllegalArgumentException exception expected!");
    }

    @Test
    void validateToken_WrongToken_ExceptionExpected() {
        try {
            boolean actual = jwtTokenProvider.validateToken("token");
        } catch (RuntimeException exception) {
            Assertions.assertEquals("Expired or invalid JWT token", exception.getMessage());
            return;
        }
        Assertions.fail("JwtException or IllegalArgumentException exception expected!");
    }
}

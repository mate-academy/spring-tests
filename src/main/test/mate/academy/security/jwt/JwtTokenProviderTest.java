package mate.academy.security.jwt;

import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String EMAIL = "email@gmail.com";
    private static final String PASSWORD = "qwerty";
    private static final List<String> ROLES = List.of(
            Role.RoleName.USER.name()
    );
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private HttpServletRequest request;
    private String token;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000L);
        token = jwtTokenProvider.createToken(EMAIL, ROLES);
    }

    @Test
    void createToken_ok() {
        Assertions.assertNotNull(token);
    }

    @Test
    void getAuthentication_ok() {
        UserDetails userDetails = User.withUsername(EMAIL).password(PASSWORD).roles("USER").build();
        Mockito.when(userDetailsService.loadUserByUsername(any())).thenReturn(userDetails);
        jwtTokenProvider.getAuthentication(token);
    }

    @Test
    void getUsername_ok() {
        Assertions.assertEquals(jwtTokenProvider.getUsername(token), EMAIL);
    }

    @Test
    void getUserName_usernameNull_notOk() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> jwtTokenProvider.getUsername(null));
    }

    @Test
    void getUserName_usernameEmpty_notOk() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> jwtTokenProvider.getUsername(""));
    }

    @Test
    void validateToken_ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_tokenNull_notOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(null));
    }

    @Test
    void validateToken_tokenEmpty_notOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(""));
    }

    @Test
    void resolveToken_ok() {
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        String actualToken = jwtTokenProvider.resolveToken(request);
        Assertions.assertEquals(actualToken, token);
        Assertions.assertEquals(jwtTokenProvider.getUsername(actualToken), EMAIL);
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void resolveToken_headerNull_ok() {
        Mockito.when(request.getHeader("Authorization")).thenReturn(null);
        String actualToken = jwtTokenProvider.resolveToken(request);
        Assertions.assertNull(actualToken);
    }
}

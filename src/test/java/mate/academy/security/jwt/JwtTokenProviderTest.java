package mate.academy.security.jwt;

import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String EMAIL = "bob@i.ui";
    private static final String PASSWORD = "1234";
    private static final List<String> ROLES = List.of("USER");
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private HttpServletRequest request;
    private String token;

    @BeforeEach
    void setUp() {
        request = Mockito.mock(HttpServletRequest.class);
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000L);
        token = jwtTokenProvider.createToken(EMAIL, ROLES);
    }

    @Test
    void createToken_Ok() {
        Assertions.assertNotNull(token);
    }

    @Test
    void getAuthentication_Ok() {
        UserDetails userDetails = User.withUsername(EMAIL).password(PASSWORD).roles("USER").build();
        Mockito.when(userDetailsService.loadUserByUsername(any())).thenReturn(userDetails);
        jwtTokenProvider.getAuthentication(token);
    }

    @Test
    void getUsername_Ok() {
        Assertions.assertEquals(jwtTokenProvider.getUsername(token), EMAIL);
    }

    @Test
    void getUserName_usernameIsNull_NotOk() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> jwtTokenProvider.getUsername(null));
    }

    @Test
    void getUserName_usernameIsEmpty_NotOk() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> jwtTokenProvider.getUsername(""));
    }

    @Test
    void validationToken_Ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validationToken_tokenIsNull_NotOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(null));
    }

    @Test
    void validationToken_tokenIsEmpty_NotOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(""));
    }

    @Test
    void resolveToken_Ok() {
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        String actualToken = jwtTokenProvider.resolveToken(request);
        Assertions.assertEquals(actualToken, token);
        Assertions.assertEquals(jwtTokenProvider.getUsername(actualToken), EMAIL);
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void resolveToken_headerIdNull_NotOk() {
        Mockito.when(request.getHeader("Authorization")).thenReturn(null);
        String actualToken = jwtTokenProvider.resolveToken(request);
        Assertions.assertEquals(actualToken, null);
    }
}

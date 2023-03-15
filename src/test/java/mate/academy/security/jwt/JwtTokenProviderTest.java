package mate.academy.security.jwt;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {
    private static final String testEmail = "test@gmail.com";
    private static String token;
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private UserDetailsService userDetailsService;
    private List<String> roleList;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                3600000L);
        roleList = List.of("USER");
        token = jwtTokenProvider.createToken(testEmail, roleList);
    }

    @Test
    void jwtTokenProvider_createToken_Ok() {
        String actual = jwtTokenProvider.createToken(testEmail, roleList);
        Assertions.assertEquals(token, actual);
    }

    @Test
    void jwtTokenProvider_getAuthentication_Ok() {
        UserDetails userDetails = User.withUsername(testEmail)
                .password("12345678")
                .authorities("ROLE_USER").build();
        Mockito.when(userDetailsService
                        .loadUserByUsername(any()))
                .thenReturn(userDetails);
        jwtTokenProvider.getAuthentication(token);
    }

    @Test
    void jwtTokenProvider_getUsername_Ok() {
        Assertions.assertEquals(testEmail, jwtTokenProvider.getUsername(token));
    }

    @Test
    void jwtTokenProvider_resolveToken_Ok() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Mockito.when(request.getHeader(any())).thenReturn("Bearer " + token);
        Assertions.assertEquals(token, jwtTokenProvider.resolveToken(request));
    }

    @Test
    void jwtTokenProvider_validateToken_Ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void jwtTokenProvider_resolveTokenNotBearer_notOk() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Mockito.when(request.getHeader(any())).thenReturn("Not Bearer " + token);
        Assertions.assertNull(jwtTokenProvider.resolveToken(request));
    }

    @Test
    void jwtTokenProvider_resolveTokenNull_notOk() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Mockito.when(request.getHeader(any())).thenReturn(null);
        Assertions.assertNull(jwtTokenProvider.resolveToken(request));
    }

    @Test
    void jwtTokenProvider_validateTokenNull_notOk() {
        Assertions.assertThrows(RuntimeException.class, () -> jwtTokenProvider.validateToken(null));
    }

    @Test
    void jwtTokenProvider_validateTokenExpired_notOk() {
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                0L);
        String expiredToken = jwtTokenProvider.createToken(testEmail, roleList);
        Assertions.assertThrows(RuntimeException.class, ()
                -> jwtTokenProvider.validateToken(expiredToken));
    }
}

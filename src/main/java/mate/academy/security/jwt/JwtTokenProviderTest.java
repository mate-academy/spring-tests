package mate.academy.security.jwt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class JwtTokenProviderTest {
    private static final String USER_EMAIL = "bob@i.ua";
    private static final String PASSWORD = "1234";
    private static final String ROLE_NAME = "USER";
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private HttpServletRequest req;
    private String token;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000);
        token = jwtTokenProvider.createToken(USER_EMAIL, List.of(ROLE_NAME));
    }

    @Test
    void createToken_Ok() {
        Assertions.assertNotNull(token);
    }

    @Test
    void getAuthentication_Ok() {
        UserDetails userDetails = User.builder()
                .username(USER_EMAIL)
                .password(PASSWORD)
                .roles(ROLE_NAME)
                .build();
        Mockito.when(userDetailsService.loadUserByUsername(USER_EMAIL)).thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        Assertions.assertNotNull(actual);
    }

    @Test
    void getUsername_Ok() {
        String actual = jwtTokenProvider.getUsername(token);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(token, actual);
    }

    @Test
    void resolveToken_Ok() {
        Mockito.when(req.getHeader("Authorization")).thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(req);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(token, actual);
    }

    @Test
    void validateToken_Ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_TokenIsNull_NotOk() {
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(null));
        Assertions.assertEquals("Expired or invalid Jwt Token", ex.getMessage());
    }

    @Test
    void validateToken_InvalidToken_NotOk() {
        RuntimeException ex = Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken("Invalid token"));
        Assertions.assertEquals("Expired or invalid Jwt Token", ex.getMessage());
    }
}

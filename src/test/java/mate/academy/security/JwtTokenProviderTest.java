package mate.academy.security;

import static org.mockito.ArgumentMatchers.any;

import java.util.Base64;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import mate.academy.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String LOGIN = "user@i.ua";
    private static final long VALIDITY_IN_MILLISECONDS = 3600000L;
    private String secretKey = "secret";
    private JwtTokenProvider jwtTokenProvider;
    private String token;

    @BeforeEach
    void setUp() {
        UserDetailsService userDetailsService = Mockito.spy(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", secretKey);
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                VALIDITY_IN_MILLISECONDS);
        token = jwtTokenProvider.createToken(LOGIN, List.of(Role.RoleName.USER.name()));
    }

    @Test
    void createToken_isSuccessful_ok() {
        String actual = jwtTokenProvider.createToken(LOGIN, List.of(Role.RoleName.USER.name()));
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(token, actual);
    }

    @Test
    void getUsername_isSuccessful_Ok() {
        Assertions.assertEquals(LOGIN, jwtTokenProvider.getUsername(token));
    }

    @Test
    void resolveToken_correctHeader_ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNotNull(actual);
    }

    @Test
    void resolveToken_headerIsEmpty_notOk() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader(any())).thenReturn(null);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNull(actual);
    }

    @Test
    void resolveToken_tokenIncorrectStart_notOk() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Start " + token);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNull(actual);
    }

    @Test
    void validateToken_isValid_ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_nullToken_notOk() {
        Assertions.assertThrows(RuntimeException.class, () -> jwtTokenProvider.validateToken(null),
                "This token is null!");
    }

    @Test
    void validateToken_incorrectToken_notOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken("incorrectToken"),
                "This token is not valid!");
    }

    @Test
    void validateToken_emptyToken_notOk() {
        Assertions.assertThrows(RuntimeException.class, () -> jwtTokenProvider.validateToken(""),
                "This token is empty!");
    }
}

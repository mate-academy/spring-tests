package mate.academy.security.jwt;

import static org.mockito.ArgumentMatchers.any;

import io.jsonwebtoken.MalformedJwtException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String EMAIL = "tom@gmail.com";
    private static final String PASSWORD = "1234";
    private static final String SECRET_KEY = "secretKey";
    private static final Long MILLISECONDS = 3600000L;
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;
    private HttpServletRequest request;
    private String token;

    @BeforeEach
    void setUp() {
        request = Mockito.mock(HttpServletRequest.class);
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", MILLISECONDS);
        token = jwtTokenProvider.createToken(EMAIL, getRoles());
    }

    @Test
    void init_Ok() {
        String expectedSecretKey = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
        ReflectionTestUtils.setField(jwtTokenProvider, SECRET_KEY, expectedSecretKey);
        jwtTokenProvider.init();

        String actualSecretKey = (String) ReflectionTestUtils
                .getField(jwtTokenProvider, SECRET_KEY);
        Assertions.assertNotNull(actualSecretKey);
        String decodedActualSecretKey = new String(Base64.getDecoder()
                .decode(actualSecretKey));
        Assertions.assertEquals(expectedSecretKey, decodedActualSecretKey);
    }

    @Test
    void createToken_Ok() {
        Assertions.assertNotNull(token);
    }

    @Test
    void getAuthentication_Ok() {
        UserDetails userDetails = User.withUsername(EMAIL)
                .password(PASSWORD)
                .roles(getRoles().toArray(new String[0])).build();
        Mockito.when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        Assertions.assertEquals(principal.getUsername(), userDetails.getUsername());
        Assertions.assertEquals(principal.getPassword(), userDetails.getPassword());
        Assertions.assertEquals(principal.getAuthorities(), userDetails.getAuthorities());
    }

    @Test
    void getUsername_Ok() {
        Assertions.assertEquals(jwtTokenProvider.getUsername(token), EMAIL);
    }

    @Test
    void resolveToken_Ok() {
        Mockito.when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);
        Assertions.assertEquals(jwtTokenProvider.resolveToken(request), token);
        Assertions.assertEquals(EMAIL, jwtTokenProvider
                .getUsername(jwtTokenProvider.resolveToken(request)));

    }

    @Test
    void validateToken_Ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void getAuthentication_EmptyToken_NotOk() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> jwtTokenProvider.getAuthentication(""));
    }

    @Test
    void getAuthentication_NullToken_NotOk() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> jwtTokenProvider.getAuthentication(null));
    }

    @Test
    void getUsername_EmptyName_NotOk() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> jwtTokenProvider.getUsername(""));
    }

    @Test
    void getUsername_Null_NotOk() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> jwtTokenProvider.getUsername(null));
    }

    @Test
    void getUsername_WrongName_NotOk() {
        Assertions.assertThrows(MalformedJwtException.class,
                () -> jwtTokenProvider.getUsername("invalid_token"));
    }

    @Test
    void resolveToken_EmptyRequest_NotOk() {
        Mockito.when(request.getHeader("Authorization")).thenReturn(null);
        Assertions.assertNull(jwtTokenProvider.resolveToken(request));
    }

    @Test
    void resolveToken_WrongToken_NotOk() {
        Mockito.when(request.getHeader(any())).thenReturn(EMAIL);
        Assertions.assertNotEquals(EMAIL, jwtTokenProvider.resolveToken(request));
    }

    @Test
    void validateToken_WrongToken_NotOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken("1234567890"));
    }

    @Test
    void validateToken_EmptyToken_NotOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(""));
    }

    @Test
    void validateToken_NullToken_NotOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(null));
    }

    private List<String> getRoles() {
        List<String> roles = new ArrayList<>();
        roles.add(Role.RoleName.USER.name());
        roles.add(Role.RoleName.ADMIN.name());
        return roles;
    }
}

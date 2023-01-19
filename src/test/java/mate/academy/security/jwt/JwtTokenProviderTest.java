package mate.academy.security.jwt;

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
    private static final String EMAIL = "test@gmail.com";
    private static final String PASSWORD = "1234";
    private static final String USER_ROLE = "USER";
    private static final Role ROLE = new Role(Role.RoleName.USER);
    private static final List<String> ROLES = List.of(USER_ROLE);
    private static UserDetailsService userDetailsService;
    private static JwtTokenProvider jwtTokenProvider;
    private static String token;
    private static HttpServletRequest req;

    @BeforeEach
    void setUp() {
        req = Mockito.mock(HttpServletRequest.class);
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
    void getAuthentication_OK() {
        UserDetails userDetails =
                User.withUsername(EMAIL).password(PASSWORD).roles(USER_ROLE).build();
        Mockito.when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        Assertions.assertEquals(principal.getUsername(),userDetails.getUsername());
        Assertions.assertEquals(principal.getPassword(),userDetails.getPassword());
        Assertions.assertEquals(principal.getAuthorities(),userDetails.getAuthorities());
    }

    @Test
    void getUsername_Ok() {
        Assertions.assertEquals(jwtTokenProvider.getUsername(token),EMAIL);
    }

    @Test
    void getUsername_Empty_NotOk() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> jwtTokenProvider.getUsername(""));
    }

    @Test
    void getUsername_Null_NotOk() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> jwtTokenProvider.getUsername(null));
    }

    @Test
    void resolveToken_Ok() {
        Mockito.when(req.getHeader("Authorization")).thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(req);
        Assertions.assertEquals(actual,token);
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
        Assertions.assertEquals(EMAIL,jwtTokenProvider.getUsername(actual));
    }

    @Test
    void resolveToken_HeaderNull_NotOk() {
        Mockito.when(req.getHeader("Authorization")).thenReturn(null);
        String actual = jwtTokenProvider.resolveToken(req);
        Assertions.assertEquals(actual,null);
    }

    @Test
    void validateToken_Ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_Empty_NotOk() {
        Assertions.assertThrows(RuntimeException.class,() -> jwtTokenProvider.validateToken(""));
    }

    @Test
    void validateToken_Null_NotOk() {
        Assertions.assertThrows(RuntimeException.class,() -> jwtTokenProvider.validateToken(null));
    }
}

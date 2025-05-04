package mate.academy.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
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
    private static final String LOGIN = "piznak@gmail.com";

    private static final List<String> ROLES = List.of("USER","ADMIN");
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    private HttpServletRequest httpServletRequest;

    @BeforeEach
    void setUp() {
        httpServletRequest = Mockito.mock(HttpServletRequest.class);
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider,"secretKey","secret");
        ReflectionTestUtils.setField(jwtTokenProvider,"validityInMilliseconds",3600000);

    }

    @Test
    public void createToken_Ok() {
        String token = jwtTokenProvider.createToken(LOGIN, ROLES);
        Assertions.assertNotNull(token);
    }

    @Test
    public void getAuthentication_Ok() {
        String token = jwtTokenProvider.createToken(LOGIN, ROLES);
        UserDetails build = User.withUsername(LOGIN)
                .password("bla bla")
                .authorities(ROLES.get(0))
                .build();
        Mockito.when(userDetailsService.loadUserByUsername(LOGIN)).thenReturn(build);
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        Assertions.assertNotNull(authentication);
        Assertions.assertTrue(authentication.isAuthenticated());
    }

    @Test
    public void getUsername_Ok() {
        String token = jwtTokenProvider.createToken(LOGIN, ROLES);
        String username = jwtTokenProvider.getUsername(token);
        assertEquals(LOGIN,username);
    }

    @Test
    public void resolveToken_Ok() {
        Mockito.when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer good");
        String actual = jwtTokenProvider.resolveToken(httpServletRequest);
        String expected = "good";
        assertEquals(actual,expected);
    }

    @Test
    public void validateToken_Ok() {
        String token = jwtTokenProvider.createToken(LOGIN, ROLES);
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }
}

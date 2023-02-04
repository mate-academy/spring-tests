package mate.academy.security.jwt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JwtTokenProviderTest {
    private static final String LOGIN = "larry@i.ua";
    private static final String ROLE = "USER";
    private HttpServletRequest httpServletRequest;
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private String token;

    @BeforeEach
    public void setUp() {
        httpServletRequest = Mockito.mock(HttpServletRequest.class);
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000L);
        token = jwtTokenProvider.createToken(LOGIN, List.of(ROLE));
    }

    @Test
    public void createToken_Ok() {
        assertNotNull(token);
    }

    @Test
    public void getAuthentication_Ok() {
        UserDetails userDetails =
                User.withUsername(LOGIN).password("1234567").roles(ROLE).build();
        Mockito.when(userDetailsService.loadUserByUsername(LOGIN)).thenReturn(userDetails);
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        assertEquals(principal.getUsername(),userDetails.getUsername());
        assertEquals(principal.getPassword(),userDetails.getPassword());
        assertEquals(principal.getAuthorities(),userDetails.getAuthorities());
    }

    @Test
    public void getUsername_Ok() {
        assertEquals(LOGIN, jwtTokenProvider.getUsername(token));
    }

    @Test
    public void getUsername_empty_notOk() {
        assertThrows(IllegalArgumentException.class,
                () -> jwtTokenProvider.getUsername(""));
    }

    @Test
    public void getUsername_null_notOk() {
        assertThrows(IllegalArgumentException.class,
                () -> jwtTokenProvider.getUsername(null));
    }

    @Test
    void resolveToken_Ok() {
        Mockito.when(httpServletRequest.getHeader("Authorization"))
                .thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(httpServletRequest);
        assertEquals(actual,token);
        assertTrue(jwtTokenProvider.validateToken(token));
        assertEquals(LOGIN, jwtTokenProvider.getUsername(actual));
    }

    @Test
    void resolveToken_nullHeader_NotOk() {
        Mockito.when(httpServletRequest.getHeader("Authorization")).thenReturn(null);
        String actual = jwtTokenProvider.resolveToken(httpServletRequest);
        assertNull(actual);
    }

    @Test
    void validateToken_Ok() {
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_empty_NotOk() {
        assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(""));
    }

    @Test
    void validateToken_null_NotOk() {
        assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(null));
    }
}

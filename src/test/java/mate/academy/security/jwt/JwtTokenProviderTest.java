package mate.academy.security.jwt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider,"validityInMilliseconds",
                3600000L);
    }

    @Test
    void createToken_ok() {
        String actual = jwtTokenProvider.createToken("bob@i.ua", List.of("USER"));
        Assertions.assertNotNull(actual);
    }

    @Test
    void getAuthentication_ok() {
        String token = jwtTokenProvider.createToken("bob@i.ua", List.of("USER"));
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("USER"));
        User user = new User("bob@i.ua", "1234", authorities);
        UserDetails details = new User(user.getUsername(), user.getPassword(), user.isEnabled(),
                user.isAccountNonExpired(), user.isCredentialsNonExpired(),
                user.isAccountNonLocked(), user.getAuthorities());
        Mockito.when(userDetailsService.loadUserByUsername("bob@i.ua")).thenReturn(details);
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(authorities, actual.getAuthorities());
        Assertions.assertEquals(details.getUsername(), actual.getName());
    }

    @Test
    void getUsername_ok() {
        String token = jwtTokenProvider.createToken("bob@i.ua", List.of("USER"));
        String actual = jwtTokenProvider.getUsername(token);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals("bob@i.ua", actual);
    }

    @Test
    void resolveToken_ok() {
        String token = jwtTokenProvider.createToken("bob@i.ua", List.of("USER"));
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNotNull(actual);
    }

    @Test
    void resolveToken_nullToken_notOk() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn(null);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNull(actual);
    }

    @Test
    void resolveToken_invalidToken_notOk() {
        String token = jwtTokenProvider.createToken("bob@i.ua", List.of("USER"));
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn(token);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNull(actual);
    }

    @Test
    void validateToken_ok() {
        String token = jwtTokenProvider.createToken("bob@i.ua", List.of("USER"));
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_nullToken_notOk() {
        try {
            jwtTokenProvider.validateToken(null);
        } catch (RuntimeException e) {
            return;
        }
        Assertions.fail("Excepted to receive RuntimeException");
    }

    @Test
    void validateToken_invalidToken_notOk() {
        String token = jwtTokenProvider.createToken("bob@i.ua", List.of("USER"));
        try {
            jwtTokenProvider.validateToken(token + "k");
        } catch (RuntimeException e) {
            return;
        }
        Assertions.fail("Excepted to receive RuntimeException");
    }
}

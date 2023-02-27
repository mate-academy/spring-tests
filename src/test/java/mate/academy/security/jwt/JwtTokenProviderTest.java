package mate.academy.security.jwt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import mate.academy.security.CustomUserDetailsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(CustomUserDetailsService.class);
        httpServletRequest = Mockito.mock(HttpServletRequest.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider,"secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider,"validityInMilliseconds", 3600000);
    }

    @Test
    void createToken_Ok() {
        String login = "bchupika@mate.academy";
        List<String> roles = List.of("USER");
        String token = jwtTokenProvider.createToken(login, roles);
        System.out.println(token);
        Assertions.assertNotNull(token);
    }

    @Test
    void getAuthentication_Ok() {
        String login = "bchupika@mate.academy";
        List<String> roles = List.of("USER");
        String token = jwtTokenProvider.createToken(login, roles);
        UserDetails userDetails = User.withUsername("bchupika@mate.academy")
                        .password("12345678").roles(Role.RoleName.USER.name()).build();
        Mockito.when(userDetailsService.loadUserByUsername("bchupika@mate.academy"))
                .thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        Authentication expected = new UsernamePasswordAuthenticationToken(userDetails,
                "", userDetails.getAuthorities());
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void getUsername_Ok() {
        String login = "bchupika@mate.academy";
        List<String> roles = List.of("USER");
        String token = jwtTokenProvider.createToken(login, roles);
        Assertions.assertEquals("bchupika@mate.academy", jwtTokenProvider.getUsername(token));
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn("token");
        String actual = jwtTokenProvider.resolveToken(req);
        Assertions.assertNull(actual);
    }

    @Test
    void validateToken_Ok() {
        String login = "bchupika@mate.academy";
        List<String> roles = List.of("USER");
        String token = jwtTokenProvider.createToken(login, roles);
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }
}

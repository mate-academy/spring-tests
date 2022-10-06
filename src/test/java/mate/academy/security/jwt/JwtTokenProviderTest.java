package mate.academy.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import mate.academy.model.Role.RoleName;
import mate.academy.security.CustomUserDetailsService;
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
    private static final String LOGIN = "actual@mail.ua";
    private static final String PASSWORD = "1234";
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;
    private String token;
    private List<String> roles;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(CustomUserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider,
                "secretKey", "secret", String.class);
        ReflectionTestUtils.setField(jwtTokenProvider,
                "validityInMilliseconds", 3600000L, long.class);
        roles = List.of(RoleName.ADMIN.name());
        token = jwtTokenProvider.createToken(LOGIN, roles);
    }

    @Test
    void createToken_ok() {
        int expected = token.length();

        String actual = jwtTokenProvider.createToken(LOGIN, roles);

        System.out.println(actual);
        System.out.println(token);

        assertEquals(expected, actual.length());
    }

    @Test
    void getAuthentication_ok() {
        UserDetails userDetails = User.withUsername(LOGIN)
                .password(PASSWORD)
                .roles(Role.RoleName.ADMIN.name())
                .build();
        Mockito.when(userDetailsService.loadUserByUsername(LOGIN)).thenReturn(userDetails);
        Authentication expected = new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());

        Authentication actual = jwtTokenProvider.getAuthentication(token);

        assertEquals(expected, actual);
    }

    @Test
    void getUsername_ok() {
        String actual = jwtTokenProvider.getUsername(token);

        assertEquals(LOGIN, actual);
    }

    @Test
    void validateToken_ok() {
        boolean expected = true;

        boolean actual = jwtTokenProvider.validateToken(token);

        assertEquals(expected, actual);
    }

    @Test
    void validateToken_tokenNull_notOk() {
        assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(null));
    }

    @Test
    void resolveToken_ok() {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(httpServletRequest.getHeader("Authorization"))
                .thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(httpServletRequest);

        assertEquals(token, actual);
    }
}

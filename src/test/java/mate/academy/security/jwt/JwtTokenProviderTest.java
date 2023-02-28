package mate.academy.security.jwt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private String login;
    private String password;
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() throws Exception {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        login = "vvv@i.ua";
        password = "12341234";
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey",
                "secret", String.class);
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                3600000L, Long.class);
    }

    @Test
    void createToken_Ok() {
        List<String> roles = List.of("ADMIN");
        String currentToken = jwtTokenProvider.createToken(login, roles);
        Assertions.assertNotNull(currentToken);
    }

    @Test
    void getAuthentication_Ok() {
        UserBuilder userBuilder = User.withUsername(login)
                .password(password)
                .roles("ADMIN");
        String token = jwtTokenProvider.createToken(login, List.of("ADMIN"));
        Mockito.when(userDetailsService.loadUserByUsername(login)).thenReturn(userBuilder.build());
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(login, actual.getName());
        Assertions.assertEquals("ROLE_ADMIN", actual.getAuthorities().stream()
                .filter(a -> a.getAuthority().equals("ROLE_ADMIN"))
                . findAny().get().toString());
    }

    @Test
    void getUsername_Ok() {
        String token = jwtTokenProvider.createToken(login, List.of("ADMIN"));
        String actual = jwtTokenProvider.getUsername(token);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(login, actual);
    }

    @Test
    void resolveToken_Ok() {
        String token = jwtTokenProvider.createToken(login, List.of("ADMIN"));
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(token, actual);
    }

    @Test
    void validateToken_Ok() {
        String token = jwtTokenProvider.createToken(login, List.of("ADMIN"));
        boolean actual = jwtTokenProvider.validateToken(token);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(true, actual);
    }

    @Test
    void validateToken_ExpiredOrInvalidToken_NotOk() {
        String token = "12345";
        try {
            boolean actual = jwtTokenProvider.validateToken(token);
        } catch (RuntimeException e) {
            Assertions.assertEquals("Expired or invalid JWT token", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive RuntimeException");
    }
}

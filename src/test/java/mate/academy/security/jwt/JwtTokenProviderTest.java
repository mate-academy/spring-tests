package mate.academy.security.jwt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

class JwtTokenProviderTest {
    private static final String SECRET_KEY = "secret";
    private static final long VALIDITY_IN_MILLISECONDS = 3600000;
    private static final String LOGIN = "bob@i.ua";
    private static final String PASSWORD = "12345678";
    private static final List<String> ROLES = List.of("USER");

    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                VALIDITY_IN_MILLISECONDS);
        jwtTokenProvider.init();
    }

    @Test
    void createToken_ok() {
        String tokenActual = jwtTokenProvider.createToken(LOGIN, ROLES);
        Assertions.assertNotNull(tokenActual);
        Assertions.assertNotEquals("", tokenActual);
        Assertions.assertEquals(3, tokenActual.split("\\.").length);
    }

    @Test
    void getAuthentication_ok() {
        User.UserBuilder builder;
        builder = org.springframework.security.core.userdetails.User.withUsername(LOGIN);
        builder.password(PASSWORD);
        builder.roles("USER");
        UserDetails userDetails = builder.build();
        Mockito.when(userDetailsService.loadUserByUsername(Mockito.any())).thenReturn(userDetails);
        String tokenActual = jwtTokenProvider.createToken(LOGIN, ROLES);
        Authentication authenticationActual = jwtTokenProvider.getAuthentication(tokenActual);
        Assertions.assertNotNull(authenticationActual);
        Assertions.assertEquals(LOGIN, authenticationActual.getName());
        Assertions.assertTrue(authenticationActual.isAuthenticated());
    }

    @Test
    void getUsername_ok() {
        String tokenActual = jwtTokenProvider.createToken(LOGIN, ROLES);
        String actual = jwtTokenProvider.getUsername(tokenActual);
        Assertions.assertEquals(LOGIN, actual);
    }

    @Test
    void resolveToken_ok() {
        String expectedToken = jwtTokenProvider.createToken(LOGIN, ROLES);
        String bearerToken = "Bearer " + expectedToken;
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(httpServletRequest.getHeader("Authorization")).thenReturn(bearerToken);
        String actualToken = jwtTokenProvider.resolveToken(httpServletRequest);
        Assertions.assertNotNull(actualToken);
        Assertions.assertEquals(expectedToken, actualToken);
    }

    @Test
    void validateToken_ok() {
        String validToken = jwtTokenProvider.createToken(LOGIN, ROLES);
        Assertions.assertTrue(jwtTokenProvider.validateToken(validToken));
    }

    @Test
    void validateToken_notValidToken_notOk() {
        try {
            jwtTokenProvider.validateToken("a1.b2.c3");
        } catch (RuntimeException e) {
            Assertions.assertEquals("Expired or invalid JWT token", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive JwtException | IllegalArgumentException");
    }
}
package mate.academy.security.jwt;

import io.jsonwebtoken.Jwts;
import java.util.Arrays;
import java.util.List;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

class JwtTokenProviderTest {
    private static final long VALIDITY_IN_MILLISECONDS = 3600000L;
    private static final String SECRET = "secret";
    private static final String EMAIL = "bob@gmail.com";
    private static final String PASSWORD = "1234";
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private String token;

    {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        jwtTokenProvider.setSecretKey(SECRET);
        jwtTokenProvider.setValidityInMilliseconds(VALIDITY_IN_MILLISECONDS);
        List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
        token = jwtTokenProvider.createToken(EMAIL, roles);
    }

    @Test
    void createToken_Ok() {
        String actual = Jwts.parser().setSigningKey(SECRET)
                .parseClaimsJws(token).getBody().getSubject();
        Assertions.assertFalse(token.isEmpty());
        Assertions.assertEquals(EMAIL, actual);
    }

    @Test
    void getAuthentication_Ok() {
        UserDetails userDetails = User.withUsername(EMAIL).password(PASSWORD)
                .roles(Role.RoleName.USER.name()).build();
        Mockito.when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);

        Authentication actual = jwtTokenProvider.getAuthentication(token);
        Assertions.assertEquals(userDetails, actual.getPrincipal());
        Assertions.assertEquals("", actual.getCredentials());
        Assertions.assertTrue(actual.getPrincipal().toString().contains(EMAIL));
        Assertions.assertEquals(UsernamePasswordAuthenticationToken.class, actual.getClass());
    }

    @Test
    void getUsername_Ok() {
        String actual = jwtTokenProvider.getUsername(token);
        Assertions.assertEquals(EMAIL, actual);
    }

    @Test
    void resolveToken_Ok() {
        String bearerToken = "Bearer " + token;
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", bearerToken);

        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(token, actual);
    }

    @Test
    void resolveToken_invalidToken_notOk() {
        String bearerToken = "InvalidToken ";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", bearerToken);

        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNull(actual);
    }

    @Test
    void validateToken_ok() {
        boolean actual = jwtTokenProvider.validateToken(token);
        Assertions.assertTrue(actual);
    }

    @Test
    void validateToken_invalidToken_notOk() {
        String invalidToken = "InvalidToken";
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(invalidToken));
    }
}

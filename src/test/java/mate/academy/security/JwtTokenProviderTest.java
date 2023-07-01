package mate.academy.security;

import java.util.ArrayList;
import java.util.List;
import io.jsonwebtoken.Jwts;
import mate.academy.model.Role;
import mate.academy.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import static org.mockito.Mockito.when;

public class JwtTokenProviderTest {
    private static final String EMAIL = "test@mail.com";
    private static final String PASSWORD = "12346457";
    private static final long VALIDITY_IN_MILLISECONDS = 3600000L;
    private static final String SECRET = "secret";
    private JwtTokenProvider tokenProvider;
    private UserDetailsService userDetailsService;
    private String token;
    private List<String> roles;

    @BeforeEach
    void setUp() throws ReflectiveOperationException {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        tokenProvider = new JwtTokenProvider(userDetailsService);
        tokenProvider.setValidityInMilliseconds(VALIDITY_IN_MILLISECONDS);
        tokenProvider.setSecretKey(SECRET);
        roles = new ArrayList<>();
        roles.add(Role.RoleName.USER.name());
        roles.add(Role.RoleName.ADMIN.name());
        token = tokenProvider.createToken(EMAIL, roles);
    }

    @Test
    public void createToken_ok() {
        String expected = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        Assertions.assertEquals(EMAIL, expected);
    }

    @Test
    public void getUserName_ok() {
        String expected = tokenProvider.getUsername(token);
        Assertions.assertEquals(EMAIL, expected);
    }

    @Test
    public void getAuthentication_ok() {
        UserDetails userDetails = User.withUsername(EMAIL)
                .password(PASSWORD)
                .roles(Role.RoleName.USER.name())
                .build();
        when(userDetailsService.loadUserByUsername(EMAIL))
                .thenReturn(userDetails);
        Authentication authentication = tokenProvider.getAuthentication(token);
        Assertions.assertNotNull(authentication);
        Assertions.assertEquals(EMAIL, authentication.getName());
    }

    @Test
    public void resolveToken_ok() {
        String bearerToken = "Bearer " + token;
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", bearerToken);
        String actual = tokenProvider.resolveToken(request);
        Assertions.assertEquals(actual, token);
    }

    @Test
    public void resolveToken_null() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String actual = tokenProvider.resolveToken(request);
        Assertions.assertNull(actual);
    }

    @Test
    public void resolveToken_withoutBearer() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", token);
        String actual = tokenProvider.resolveToken(request);
        Assertions.assertNull(actual);
    }

    @Test
    public void resolveToken_empty() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "");
        String actual = tokenProvider.resolveToken(request);
        Assertions.assertNull(actual);
    }

    @Test
    public void validateToken_ok() {
        Assertions.assertTrue(tokenProvider.validateToken(token));
    }

    @Test
    public void validateToken_invalid() {
        String invalidToken = token + "invalid";
        Assertions.assertThrows(RuntimeException.class,
                () -> tokenProvider.validateToken(invalidToken));
    }
}

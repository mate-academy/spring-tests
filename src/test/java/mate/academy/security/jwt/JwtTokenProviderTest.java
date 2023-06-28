package mate.academy.security.jwt;

import io.jsonwebtoken.Jwts;
import java.util.ArrayList;
import java.util.List;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class JwtTokenProviderTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "1234";
    private static final String SECRET_KEY = "secret_key";
    private static final long VALIDITY_IN_MILLY_SECONDS = 3600000L;
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;
    private String token;
    private List<String> roles;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        jwtTokenProvider.setSecretKey(SECRET_KEY);
        jwtTokenProvider.setValidityInMilliseconds(VALIDITY_IN_MILLY_SECONDS);
        roles = new ArrayList<>();
        roles.add(Role.RoleName.ADMIN.name());
        roles.add(Role.RoleName.USER.name());
        token = jwtTokenProvider.createToken(EMAIL, roles);
    }

    @Test
    void createToken_ok() {
        assertNotNull(token);
        String actual = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        assertEquals(EMAIL, actual);
    }

    @Test
    void getUsername_ok() {
        String actual = jwtTokenProvider.getUsername(token);
        assertEquals(EMAIL, actual);
    }

    @Test
    void getAuthentication_ok() {
        UserDetails userDetails = User.withUsername(EMAIL)
                .password(PASSWORD)
                .roles(Role.RoleName.USER.name())
                .build();
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        assertNotNull(actual);
        assertTrue(actual.getPrincipal().toString().contains(EMAIL));
    }

    @Test
    void resolveToken_ok() {
        String bearerToken = "Bearer " + token;
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader("Authorization", bearerToken);
        String actual = jwtTokenProvider.resolveToken(req);
        assertNotNull(actual);
        assertEquals(actual, token);
    }

    @Test
    void validateToken_ok() {
        boolean actual = jwtTokenProvider.validateToken(token);
        assertTrue(actual);
    }
}

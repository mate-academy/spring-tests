package mate.academy.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

class JwtTokenProviderTest {
    private static final String VALID_USERNAME = "test";
    private static final List<String> VALID_ROLES = List.of("USER");
    private static final String INVALID_TOKEN = "asdas.as.das";

    private static String VALID_TOKEN;

    private final UserDetailsService userDetailsService = mock(UserDetailsService.class);
    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(userDetailsService);

    @BeforeEach
    void setUp() {
        setField(jwtTokenProvider, "secretKey", "secret");
        setField(jwtTokenProvider, "validityInMilliseconds", 3600000);
        jwtTokenProvider.init();
        VALID_TOKEN = jwtTokenProvider.createToken(VALID_USERNAME, VALID_ROLES);
    }

    @Test
    void createToken_ok() {
        String token = jwtTokenProvider.createToken(VALID_USERNAME, VALID_ROLES);
        assertNotNull(token);

        Claims claims = getClaims(token);
        assertEquals(VALID_USERNAME, claims.getSubject());
        assertNotNull(claims.get("roles", List.class));
        assertTrue(claims.getExpiration().after(new Date()));
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey((String) getField(jwtTokenProvider, "secretKey"))
                .parseClaimsJws(token)
                .getBody();
    }

    @Test
    void getAuthentication_validToken_ok() {
        UserDetails userDetails = User.builder()
                .username(VALID_USERNAME)
                .password("123")
                .roles(VALID_ROLES.toArray(String[]::new)).build();
        when(userDetailsService.loadUserByUsername(VALID_USERNAME)).thenReturn(userDetails);

        Authentication authentication = jwtTokenProvider.getAuthentication(VALID_TOKEN);
        assertEquals(VALID_USERNAME, authentication.getName());
        assertEquals("", authentication.getCredentials());

        List<SimpleGrantedAuthority> authorities = VALID_ROLES.stream()
                .map(s -> new SimpleGrantedAuthority("ROLE_" + s))
                .toList();
        assertIterableEquals(authorities, authentication.getAuthorities());
    }

    @Test
    void getUsername_validToken_ok() {
        String username = jwtTokenProvider.getUsername(VALID_TOKEN);
        assertEquals(VALID_USERNAME, username);
    }

    @Test
    void getUsername_invalidToken_notOk() {
        assertThrows(Exception.class, () -> jwtTokenProvider.getUsername(INVALID_TOKEN));
    }

    @Test
    void getUsername_tokenIsNull_notOk() {
        assertThrows(Exception.class, () -> jwtTokenProvider.getUsername(null));
    }

    @Test
    void resolveToken_validHeader_ok() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer sada.asdas.asd");

        String token = jwtTokenProvider.resolveToken(request);
        assertNotNull(token);
    }

    @Test
    void resolveToken_invalidHeader_ok() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("asd");

        String token = jwtTokenProvider.resolveToken(request);
        assertNull(token);
    }

    @Test
    void resolveToken_headerIsMissing_ok() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        String token = jwtTokenProvider.resolveToken(request);
        assertNull(token);
    }

    @Test
    void validateToken_validToken_ok() {
        assertTrue(jwtTokenProvider.validateToken(VALID_TOKEN));
    }

    @Test
    void validateToken_tokenIsExpired_ok() {
        setField(jwtTokenProvider, "validityInMilliseconds", 0);
        String token = jwtTokenProvider.createToken(VALID_USERNAME, VALID_ROLES);
        assertThrows(RuntimeException.class, () -> jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_invalidToken_ok() {
        assertThrows(Exception.class, () -> jwtTokenProvider.validateToken(INVALID_TOKEN));
    }

    @Test
    void validateToken_tokenIsNull_ok() {
        assertThrows(Exception.class, () -> jwtTokenProvider.validateToken(null));
    }
}

package mate.academy.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.springframework.security.core.userdetails.User.UserBuilder;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import mate.academy.exception.InvalidJwtAuthenticationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String SECRET_KEY = "secret";
    private static final long VALIDITY_IN_MILLISECONDS = 3600000L;
    private UserDetailsService userDetailsServiceMock;
    private JwtTokenProvider jwtTokenProvider;
    private String email;
    private List<String> roles;

    @BeforeEach
    void setUp() {
        userDetailsServiceMock = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsServiceMock);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                VALIDITY_IN_MILLISECONDS);
        email = "user@gmail.com";
        roles = List.of("ADMIN");
    }

    @Test
    void createToken_Ok() {

        String actual = jwtTokenProvider.createToken(email, roles);
        assertNotNull(actual);

        String email = Jwts.parser().setSigningKey(SECRET_KEY)
                .parseClaimsJws(actual).getBody().getSubject();
        assertEquals(email, email);
    }

    @Test
    void createToken_LoginIsNull_NotOk() {
        assertThrows(RuntimeException.class, () -> {
            jwtTokenProvider.createToken(null, Collections.emptyList());
        });
    }

    @Test
    void createToken_RolesAreNull_NotOk() {
        assertThrows(RuntimeException.class, () -> {
            jwtTokenProvider.createToken(email, null);
        });
    }

    @Test
    void getAuthentication_Ok() {
        String token = createValidToken();
        String password = "password";
        jwtTokenProvider = Mockito.spy(jwtTokenProvider);
        Mockito.when(jwtTokenProvider.getUsername(token)).thenReturn(email);
        UserBuilder builder = User.withUsername(email);
        builder.password(password);
        builder.roles("ADMIN");
        UserDetails userDetails = builder.build();
        Mockito.when(userDetailsServiceMock.loadUserByUsername(any())).thenReturn(userDetails);
        Authentication expected = new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void getAuthentication_TokenIsNull_Ok() {
        assertThrows(RuntimeException.class, () -> {
            jwtTokenProvider.getAuthentication(null);
        });
    }

    @Test
    void getUsername_Ok() {
        String token = createValidToken();
        String actual = jwtTokenProvider.getUsername(token);
        assertNotNull(actual);
        assertEquals(email, actual);
    }

    @Test
    void getUsername_TokenIsNull_NotOk() {
        assertThrows(RuntimeException.class, () -> {
            jwtTokenProvider.getUsername(null);
        });
    }

    @Test
    void resolveToken_Ok() {
        String validToken = "token";
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.addHeader("Authorization", "Bearer " + validToken);
        String actual = jwtTokenProvider.resolveToken(mockHttpServletRequest);
        assertNotNull(actual);
        assertEquals(validToken, actual);
    }

    @Test
    void resolveToken_AuthorizationIsBasic_Ok() {
        String validToken = "token";
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.addHeader("Authorization", "Basic " + validToken);
        String actual = jwtTokenProvider.resolveToken(mockHttpServletRequest);
        assertNull(actual);
    }

    @Test
    void resolveToken_AuthorizationIsNull_Ok() {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        String actual = jwtTokenProvider.resolveToken(mockHttpServletRequest);
        assertNull(actual);
    }

    @Test
    void validateToken_Ok() {
        String token = createValidToken();
        boolean actual = jwtTokenProvider.validateToken(token);
        assertNotNull(actual);
        assertTrue(actual);
    }

    @Test
    void validateToken_ExpiredToken_NotOk() {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles);
        Date now = new Date();
        Date validity = new Date(now.getTime());
        String expiredToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        assertThrows(InvalidJwtAuthenticationException.class, () -> {
            jwtTokenProvider.validateToken(expiredToken);
        });
    }

    @Test
    void validateToken_TokenIsNull_NotOk() {
        assertThrows(InvalidJwtAuthenticationException.class, () -> {
            jwtTokenProvider.validateToken(null);
        });
    }

    private String createValidToken() {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles);
        Date now = new Date();
        Date validity = new Date(now.getTime() + VALIDITY_IN_MILLISECONDS);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}

package mate.academy.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.xml.crypto.Data;
import mate.academy.exception.InvalidJwtAuthenticationException;
import mate.academy.model.Role;
import mate.academy.security.CustomUserDetailsService;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final int STANDARD_DEVIATION = 2000;
    private static final String LOGIN = "denis@mail.ru";
    private final String secretKey = Base64.getEncoder().encodeToString("secret".getBytes());
    private final long validityInMilliseconds = 3600000L;
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() throws Exception {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey",
                Base64.getEncoder().encodeToString("secret".getBytes()), String.class);
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000L, long.class);
    }

    @Test
    void createToken_ok() {
        List<String> expectedRoles = List.of(Role.RoleName.USER.name());
        Date expectedNow = new Date();
        Date expectedValidity = new Date(expectedNow.getTime() + validityInMilliseconds);
        String token = jwtTokenProvider.createToken(LOGIN, expectedRoles);
        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        String actualLogin = claims.getBody().getSubject();
        Date actualNow = claims.getBody().getIssuedAt();
        Date actualValidity = claims.getBody().getExpiration();
        List<String> actualRoles = (List<String>) claims.getBody().get("roles");
        assertEquals(LOGIN, actualLogin);
        assertEquals(expectedRoles, actualRoles);
        assertTrue(new Date(expectedNow.getTime() - STANDARD_DEVIATION).before(actualNow) &&
                new Date(expectedNow.getTime() + STANDARD_DEVIATION).after(actualNow));
        assertTrue(new Date(expectedValidity.getTime() - STANDARD_DEVIATION).before(actualValidity) &&
                new Date(expectedValidity.getTime() + STANDARD_DEVIATION).after(actualValidity));
    }

    @Test
    void getAuthentication_ok() {
        String token = createToken();
        Mockito.when(userDetailsService.loadUserByUsername(jwtTokenProvider.getUsername(token)))
                .thenReturn(User.withUsername(LOGIN)
                        .password("12345678")
                        .roles(new String[] {Role.RoleName.USER.name()})
                        .build());
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtTokenProvider.getUsername(token));
        Authentication expected = new UsernamePasswordAuthenticationToken(
                userDetails, "", userDetails.getAuthorities());
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        assertEquals(expected, actual);
    }

    @Test
    void resolveToken_ok() {
        String expected = createToken();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + expected);
        String actual = jwtTokenProvider.resolveToken(request);
        assertEquals(expected, actual);
    }

    @Test
    void validateToken_ok() {
        assertTrue(jwtTokenProvider.validateToken(createToken()));
    }

    @Test
    void validateExpiredToken_ok() {
        List<String> roles = List.of(Role.RoleName.USER.name());
        Claims claims = Jwts.claims().setSubject(LOGIN);
        claims.put("roles", roles);
        long twoHoursInMilli = 2000 * 60 * 60;
        Date now = new Date(new Date().getTime() - twoHoursInMilli);
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        assertThrows(InvalidJwtAuthenticationException.class,
                () -> jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_notOk() {
        String invalidToken = "asdasdasd.fafdssfs.weqeqds";
        assertThrows(InvalidJwtAuthenticationException.class,
                () -> jwtTokenProvider.validateToken(invalidToken));
    }

    private String createToken() {
        List<String> roles = List.of(Role.RoleName.USER.name());
        Claims claims = Jwts.claims().setSubject(LOGIN);
        claims.put("roles", roles);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
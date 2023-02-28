package mate.academy.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final long EXP_TIME = 3600000;
    private static final String SECRET_KEY = "secret";
    private static String correctToken;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeAll
    static void beforeAll() {
        Claims claims = Jwts.claims().setSubject("user@com.ua");
        claims.put("roles", List.of(new Role(Role.RoleName.USER)));
        Date now = new Date();
        Date validity = new Date(now.getTime() + EXP_TIME);
        correctToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    @BeforeEach
    void setUp() {
        UserDetailsService userDetailsService = Mockito.mock(UserDetailsService.class);
        Mockito.when(userDetailsService.loadUserByUsername(Mockito.any(String.class)))
                .thenReturn(getDefaultUserDetails());

        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider,"secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtTokenProvider,
                "validityInMilliseconds", EXP_TIME);
    }

    @Test
    public void createToken_ok() {
        Assertions.assertFalse(jwtTokenProvider.createToken("user@com.ua",
                List.of(Role.RoleName.USER.name())).isEmpty());
    }

    @Test
    public void getAuthentication_ok() {
        Authentication authentication = jwtTokenProvider.getAuthentication(correctToken);
        Assertions.assertNotNull(authentication);
    }

    @Test
    public void getAuthentication_invalidJwt_ok() {
        Assertions.assertThrows(MalformedJwtException.class, () ->
                jwtTokenProvider.getAuthentication("sdf" + correctToken));
    }

    @Test
    public void getUsername_ok() {
        Assertions.assertEquals("user@com.ua", jwtTokenProvider.getUsername(correctToken));
    }

    @Test
    public void resolveToken_ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + correctToken);
        Assertions.assertEquals(correctToken, jwtTokenProvider.resolveToken(request));
    }

    @Test
    public void resolveToken_badRequestHeader_ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization"))
                .thenReturn("Basic wrong " + correctToken);
        Assertions.assertNull(jwtTokenProvider.resolveToken(request));
    }

    @Test
    public void validateToken_ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(correctToken));
    }

    @Test
    public void validateToken_oldToken_notOk() {
        Claims claims = Jwts.claims().setSubject("user@com.ua");
        claims.put("roles", List.of(new Role(Role.RoleName.USER)));
        Date now = new Date(new Date().getTime() - EXP_TIME - 1);
        Date validity = new Date(now.getTime() + EXP_TIME);
        String oldToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        Assertions.assertThrows(RuntimeException.class, () ->
                jwtTokenProvider.validateToken(oldToken));
    }

    private UserDetails getDefaultUserDetails() {
        User.UserBuilder builder;
        builder = org.springframework.security.core.userdetails.User.withUsername("user@com.ua");
        builder.password("password");
        builder.roles("USER");
        return builder.build();
    }
}

package mate.academy.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String USER_MAIL = "mail@mail.com";
    private static final String SECRET_KEY = "secret";
    private static final Long VALIDITY_MILLISECONDS = 3600000L;
    private static final String ROLE_USER = "USER";
    private static final String ROLE_ADMIN = "ADMIN";
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;
    private List<String> listRoles;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider,
                "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                VALIDITY_MILLISECONDS);
        listRoles = List.of(ROLE_USER, ROLE_ADMIN);
    }

    @Test
    void createToken_OK() {
        String token = jwtTokenProvider.createToken(USER_MAIL, listRoles);
        Assertions.assertNotNull(token);

        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY)
                .parseClaimsJws(token).getBody();
        Assertions.assertEquals(USER_MAIL, claims.getSubject());
        Assertions.assertEquals(listRoles, claims.get("roles"));

        Date now = new Date();
        Date expiration = claims.getExpiration();
        Assertions.assertNotNull(expiration);
        Assertions.assertTrue(expiration.after(now));
        Assertions.assertTrue(expiration.before(new Date(now.getTime() + VALIDITY_MILLISECONDS)));
    }

    @Test
    void authentication_Ok() {
        String actualToken = jwtTokenProvider.createToken(USER_MAIL,listRoles);
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetailsService.loadUserByUsername(Mockito.anyString()))
                .thenReturn(userDetails);

        Authentication authentication = jwtTokenProvider.getAuthentication(actualToken);
        Assertions.assertNotNull(authentication);
        Assertions.assertEquals(UsernamePasswordAuthenticationToken.class,
                authentication.getClass());
        Assertions.assertEquals(userDetails, authentication.getPrincipal());
        Assertions.assertEquals("", authentication.getCredentials());
        Assertions.assertEquals(userDetails.getAuthorities(), authentication.getAuthorities());
        Mockito.verify(userDetailsService, Mockito.times(1))
                .loadUserByUsername(Mockito.anyString());
    }

    @Test
    void getUserName_Ok() {
        String actualToken = jwtTokenProvider.createToken(USER_MAIL,listRoles);
        String userName = jwtTokenProvider.getUsername(actualToken);
        Assertions.assertEquals(USER_MAIL, userName);
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String actualToken = jwtTokenProvider.createToken(USER_MAIL,listRoles);
        Mockito.when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + actualToken);
        String resolveToken = jwtTokenProvider.resolveToken(request);
        Assertions.assertEquals(actualToken, resolveToken);
    }

    @Test
    void resolveToken_invalidToken_notOk() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String actualToken = jwtTokenProvider.createToken(USER_MAIL,listRoles);
        Mockito.when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + "invalidToken");
        String invalidResolveToken = jwtTokenProvider.resolveToken(request);
        Assertions.assertNotEquals(actualToken, invalidResolveToken);
    }

    @Test
    void validateToken_invalidToken_notOk() {
        RuntimeException runtimeException = Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken("invalidToken"));
        Assertions.assertEquals("Expired or invalid JWT token",
                runtimeException.getMessage());
    }
}

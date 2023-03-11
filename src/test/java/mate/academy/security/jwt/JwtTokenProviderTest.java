package mate.academy.security.jwt;

import static mate.academy.model.Role.RoleName.ADMIN;
import static mate.academy.model.Role.RoleName.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JwtTokenProviderTest {
    private static final long VALIDITY_IN_MILLISECONDS = 6000000;
    private static final String SECRET_KEY = "secret_key";
    private static final String LOGIN = "valid@i.ua";
    private static final String PASSWORD = "1234";
    private static final List<String> ROLES = List.of(USER.name(), ADMIN.name());
    private static final String ENCODED_SECRET_KEY = Base64.getEncoder()
            .encodeToString(SECRET_KEY.getBytes());
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        setField(jwtTokenProvider, "secretKey", ENCODED_SECRET_KEY);
        setField(jwtTokenProvider, "validityInMilliseconds", VALIDITY_IN_MILLISECONDS);
    }

    @Test
    @Order(1)
    void createToken_validInput_ok() {
        String token = jwtTokenProvider.createToken(LOGIN, ROLES);
        assertNotNull(token,
                "Method should return valid token for login '%s' and roles '%s'"
                        .formatted(LOGIN, ROLES));

        String actualLogin = Jwts.parser()
                .setSigningKey(ENCODED_SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        assertEquals(LOGIN, actualLogin,
                "Method should return token that contains login '%s' but contains '%s'"
                        .formatted(LOGIN, actualLogin));
    }

    @Test
    @Order(2)
    void getAuthentication_validToken_ok() {
        String token = "token";
        jwtTokenProvider = spy(jwtTokenProvider);
        doReturn(LOGIN).when(jwtTokenProvider).getUsername(token);

        UserDetails userDetails = User.withUsername(LOGIN)
                .password(PASSWORD)
                .roles(USER.name())
                .build();
        when(userDetailsService.loadUserByUsername(LOGIN)).thenReturn(userDetails);

        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        assertNotNull(authentication,
                "Method should return authentication from passed token");

        String actualLogin = authentication.getName();
        assertEquals(LOGIN, actualLogin,
                "Method should return authentication with principal name '%s' but returned '%s'"
                        .formatted(LOGIN, actualLogin));

        boolean ifRoleUserExist = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority()
                        .equals("ROLE_" + USER.name()));
        assertTrue(ifRoleUserExist,
                "Method should return authentication with authority '%s'"
                        .formatted("ROLE_" + USER.name()));
    }

    @Test
    @Order(3)
    void getUsername_validInput_ok() {
        String token = Jwts.builder()
                .setClaims(Jwts.claims().setSubject(LOGIN))
                .signWith(SignatureAlgorithm.HS256, ENCODED_SECRET_KEY)
                .compact();
        String actualLogin = jwtTokenProvider.getUsername(token);
        assertNotNull(actualLogin,
                "Method should return username '%s' for token '%s'"
                        .formatted(LOGIN, token));
        assertEquals(LOGIN, actualLogin,
                "Method should return username '%s' but returned '%s'"
                        .formatted(LOGIN, actualLogin));
    }

    @Test
    @Order(4)
    void resolveToken_validRequest_ok() {
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        String token = jwtTokenProvider.resolveToken(request);
        assertNotNull(token);
        assertEquals("token", token);
    }

    @Test
    @Order(5)
    void resolveToken_notBearerToken_notOk() {
        when(request.getHeader(any())).thenReturn("NotBearer token");
        String token = jwtTokenProvider.resolveToken(request);
        assertNull(token);
    }

    @Test
    @Order(6)
    void validateToken_validToken_ok() {
        Claims claims = Jwts.claims().setSubject(LOGIN);
        claims.put("roles", ROLES);
        Date now = new Date();
        Date validity = new Date(now.getTime() + VALIDITY_IN_MILLISECONDS);
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, ENCODED_SECRET_KEY)
                .compact();
        setField(jwtTokenProvider, "secretKey", ENCODED_SECRET_KEY);
        boolean validToken = jwtTokenProvider.validateToken(token);
        assertTrue(validToken,
                ("Method should return true for token with login '%s',"
                        + " roles '%s', created at '%s' and validity '%s'")
                        .formatted(LOGIN, ROLES, now, validity));
    }

    @Test
    @Order(7)
    void validateToken_expiredToken_notOk() {
        Date now = new Date();
        Date validity = new Date(now.getTime());
        String token = Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, ENCODED_SECRET_KEY)
                .compact();
        setField(jwtTokenProvider, "secretKey", ENCODED_SECRET_KEY);
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(token),
                "Method should throw '%s' for token expired at '%s'"
                        .formatted(RuntimeException.class, validity));
        assertEquals("Expired or invalid JWT token", exception.getMessage());
    }
}

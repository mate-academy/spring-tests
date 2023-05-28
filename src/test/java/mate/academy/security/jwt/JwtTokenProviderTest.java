package mate.academy.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

class JwtTokenProviderTest {
    public static final List<String> ROLES = List.of(Role.RoleName.ADMIN.name(),
            Role.RoleName.USER.name());
    private static final String SECRET_WORD =
            Base64.getEncoder().encodeToString("secret".getBytes());
    private static final int VALIDITY = 36000000;
    private static final String EMAIL = "user@gmail.com";
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void beforeAll() throws Exception {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        Field secretKeyField = JwtTokenProvider.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        secretKeyField.set(jwtTokenProvider, SECRET_WORD);
        Field validityField = JwtTokenProvider.class.getDeclaredField("validityInMilliseconds");
        validityField.setAccessible(true);
        validityField.set(jwtTokenProvider, VALIDITY);
    }

    @Test
    void createToken_validAttributes_ok() {
        String token = jwtTokenProvider.createToken(EMAIL, ROLES);
        Claims body = Jwts.parser().setSigningKey(SECRET_WORD).parseClaimsJws(token).getBody();

        Assertions.assertTrue(body.getExpiration().after(new Date()));
        Assertions.assertEquals(EMAIL, body.getSubject());
        Assertions.assertEquals(ROLES, body.get("roles", List.class));
    }

    @Test
    void getAuthentication_validToken_ok() {
        Mockito.when(userDetailsService.loadUserByUsername(EMAIL))
                .thenReturn(Mockito.mock(UserDetails.class));
        String token = jwtTokenProvider.createToken(EMAIL, ROLES);
        Authentication authentication = jwtTokenProvider.getAuthentication(token);

        Assertions.assertNotNull(authentication);
    }

    @Test
    void getUsername_validToken_ok() {
        String token = jwtTokenProvider.createToken(EMAIL, ROLES);
        String username = jwtTokenProvider.getUsername(token);

        Assertions.assertNotNull(username);
        Assertions.assertEquals(EMAIL, username);
    }

    @Test
    void resolveToken_validToken_ok() {
        String token = jwtTokenProvider.createToken(EMAIL, ROLES);
        HttpServletRequest mockedReq = Mockito.mock(HttpServletRequest.class);
        Mockito.when(mockedReq.getHeader(Mockito.any())).thenReturn("Bearer " + token);
        String resolvedToken = jwtTokenProvider.resolveToken(mockedReq);

        Assertions.assertNotNull(resolvedToken);
        Assertions.assertEquals(token, resolvedToken);
    }

    @Test
    void resolveToken_invalidToken_ok() {
        String firstInvalidToken = jwtTokenProvider.createToken(EMAIL, ROLES);
        String secondInvalidToken = "wrong token";
        String thirdInvalidToken = "Bearer" + jwtTokenProvider.createToken(EMAIL, ROLES);
        HttpServletRequest mockedReq = Mockito.mock(HttpServletRequest.class);
        Mockito.when(mockedReq.getHeader(Mockito.anyString())).thenReturn(firstInvalidToken)
                .thenReturn(secondInvalidToken).thenReturn(thirdInvalidToken).thenReturn(null);

        Assertions.assertNull(jwtTokenProvider.resolveToken(mockedReq));
        Assertions.assertNull(jwtTokenProvider.resolveToken(mockedReq));
        Assertions.assertNull(jwtTokenProvider.resolveToken(mockedReq));
        Assertions.assertNull(jwtTokenProvider.resolveToken(mockedReq));
    }

    @Test
    void validateToken_validToken_ok() {
        String token = jwtTokenProvider.createToken(EMAIL, ROLES);
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_invalidToken_notOk() throws Exception {
        Field validityField = JwtTokenProvider.class.getDeclaredField("validityInMilliseconds");
        validityField.setAccessible(true);
        validityField.set(jwtTokenProvider, 0);
        String expiredToken = jwtTokenProvider.createToken(EMAIL, ROLES);

        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(null));
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(expiredToken));
    }
}

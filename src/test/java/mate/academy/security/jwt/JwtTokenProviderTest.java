package mate.academy.security.jwt;

import io.jsonwebtoken.Jwts;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String SECRET_KEY = "secret";
    private static final long VALIDITY_IN_MILLISECONDS = 3_600_000;
    private static final String EMAIL = "bob@gmail.com";
    private static final String PASSWORD = "1234";
    private static final String USER_ROLE = "USER";
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;
    private String token;
    
    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                VALIDITY_IN_MILLISECONDS);
        token = jwtTokenProvider.createToken(EMAIL, List.of(USER_ROLE));
    }
    
    @Test
    void createToken_ok() {
        String actual = token;
        Assertions.assertNotNull(actual);
        String actualLogin = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(actual)
                .getBody()
                .getSubject();
        Assertions.assertEquals(EMAIL, actualLogin);
    }
    
    @Test
    void getAuthentication_ok() {
        UserDetails userDetails = User.withUsername(EMAIL)
                .password(PASSWORD)
                .roles(USER_ROLE)
                .build();
        Mockito.when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.getPrincipal().toString().contains(EMAIL));
    }
    
    @Test
    void getUsername_ok() {
        String actual = jwtTokenProvider.getUsername(token);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual);
    }
    
    @Test
    void resolveToken_ok() {
        String bearerToken = "Bearer " + token;
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", bearerToken);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(token, actual);
    }
    
    @Test
    void validateToken_ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }
    
    @Test
    void validateToken_invalidToken_notOk() {
        String unknownToken = "unknown token";
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(unknownToken));
    }
    
    @Test
    void validateToken_tokenIsNullOrEmpty_notOk() {
        String emptyToken = "";
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(emptyToken));
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(null));
    }
}

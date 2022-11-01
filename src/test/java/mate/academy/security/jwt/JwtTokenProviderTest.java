package mate.academy.security.jwt;

import io.jsonwebtoken.MalformedJwtException;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String LOGIN = "bob@i.ua";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIi"
            + "OiJib2JAaS51YSIsInJvbGVzIjpbIlVTRVIiLCJBRE1JTiJdLCJpYXQiOjE2NjczMT"
            + "g1NzgsImV4cCI6MTY2NzMyMjE3OH0.mu-wgmPpTx7i8ftriEWrnhNP7jS3eLnaNy6883PdgRQ";
    private static final List<String> ROLES = List.of("USER", "ADMIN");
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey",
                "secret", String.class);
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                3600000L, Long.class);
    }

    @Test
    void createToken_Ok() {
        String token = jwtTokenProvider.createToken(LOGIN, ROLES);
        Assertions.assertNotNull(token);
        Assertions.assertEquals(168, token.length());
    }

    @Test
    void getUsername_Ok() {
        String actualLogin = jwtTokenProvider.getUsername(VALID_TOKEN);

        Assertions.assertNotNull(VALID_TOKEN);
        Assertions.assertEquals(LOGIN, actualLogin);
    }

    @Test
    void getUserName_NotOk() {
        String invalidToken = new StringBuilder(VALID_TOKEN).reverse().toString();
        Assertions.assertThrows(MalformedJwtException.class, ()
                -> jwtTokenProvider.getUsername(invalidToken));
    }

    @Test
    void validateToken_Ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(VALID_TOKEN));
    }

    @Test
    void validateToken_NotOk() {
        String invalidToken = new StringBuilder(VALID_TOKEN).reverse().toString();
        try {
            jwtTokenProvider.validateToken(invalidToken);
        } catch (Exception e) {
            Assertions.assertEquals("Expired or invalid JWT token", e.getMessage());
        }
    }
}

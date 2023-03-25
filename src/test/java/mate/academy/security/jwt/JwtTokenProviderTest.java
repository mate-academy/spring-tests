package mate.academy.security.jwt;

import java.util.List;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                360000L);
    }

    @Test
    void createToken_defaultUser_Ok() {
        String token = jwtTokenProvider.createToken("bob@mail.ua", List.of("USER"));
        Assertions.assertNotNull(token);
    }

    @Test
    void createToken_emptyUser_Ok() {
        String token = jwtTokenProvider.createToken("", List.of(""));
        Assertions.assertNotNull(token);
    }

    @Test
    void validateToken_Ok() {
        String token = jwtTokenProvider.createToken("bob@mail.ua", List.of("USER"));
        boolean isValidToken = jwtTokenProvider.validateToken(token);
        Assertions.assertTrue(isValidToken);
    }

    @Test
    void validateToken_emptyToken_notOk() {
        Assertions.assertThrows(RuntimeException.class, () -> jwtTokenProvider.validateToken(""));
    }
}

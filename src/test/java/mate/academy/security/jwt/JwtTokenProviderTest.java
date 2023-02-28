package mate.academy.security.jwt;

import io.jsonwebtoken.MalformedJwtException;
import java.util.List;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkZW5pa0BnbWFpbC5"
            + "jb20iLCJyb2xlcyI6WyJVU0VSIl0sImlhdCI6MTY2ODAwNDM2MywiZXhwIjoxNjY4MDA3OTYzfQ.4dbo"
            + "7XNZu7p2uuhs_x8DqcLaXj22AsmiWbIVsMk_HlQ";
    private static final String LOGIN = "denik@gmail.com";
    private static final String PASSWORD = "password";
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() throws Exception {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret", String.class);
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                3600000L, long.class);
    }

    @Test
    void createToken_validData_shouldCreateValidToken() {
        String actual = jwtTokenProvider.createToken(LOGIN, List.of(Role.RoleName.USER.name()));
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(VALID_TOKEN.length(), actual.length());
    }

    @Test
    void getUsername_validToken_shouldReturnValidLogin() {
        String actual = jwtTokenProvider.getUsername(VALID_TOKEN);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(LOGIN, actual);
    }

    @Test
    void getUsername_notValidToken_shouldThrowMalformedJwtException() {
        Assertions.assertThrows(MalformedJwtException.class,
                () -> jwtTokenProvider.getUsername("someUN.VALID.TOKENffgjdfk"));
    }

    @Test
    void validateToken_validToken_shouldReturnTrue() {
        String validToken = jwtTokenProvider.createToken("denik@gmail.com", List.of("USER"));
        Assertions.assertTrue(jwtTokenProvider.validateToken(validToken));
    }

    @Test
    void validateToken_notValidToken_notOk() {
        String unValidToken = new StringBuilder(VALID_TOKEN).reverse().toString();
        try {
            jwtTokenProvider.validateToken(VALID_TOKEN);
        } catch (Exception e) {
            Assertions.assertEquals("Expired or invalid JWT token", e.getMessage());
        }
    }
}

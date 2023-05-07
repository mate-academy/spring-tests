package mate.academy.security.jwt;

import java.lang.reflect.Field;
import java.util.List;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

class JwtTokenProviderTest {
    private static final String EMAIL = "alice@i.ua";
    private static final String EXPECTED_SECRET_KEY = "eyJhbGciOiJIUzI1NiJ9";
    private static final Role ROLE = new Role(Role.RoleName.USER);
    private static JwtTokenProvider jwtTokenProvider;
    private static UserDetailsService userDetailsService;
    private static UserDetails userDetails;

    @BeforeAll
    static void beforeAll() {
        userDetails = Mockito.mock(UserDetails.class);
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);

        try {
            Field secretKey = jwtTokenProvider.getClass().getDeclaredField("secretKey");
            secretKey.setAccessible(true);
            secretKey.set(jwtTokenProvider, "secret key");
            Field validityTime = jwtTokenProvider.getClass()
                    .getDeclaredField("validityInMilliseconds");
            validityTime.setAccessible(true);
            validityTime.set(jwtTokenProvider, 3600000L);
        } catch (Exception e) {
            throw new RuntimeException("Can`t create secret key", e);
        }
    }

    @Test
    void createToken_Ok() {
        String actual = jwtTokenProvider.createToken(EMAIL, List.of(ROLE.getRoleName().name()));

        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.startsWith(EXPECTED_SECRET_KEY));
    }

    @Test
    void getUsername_Ok() {
        String username =
                jwtTokenProvider.getUsername(jwtTokenProvider.createToken(EMAIL,
                        List.of(ROLE.getRoleName().name())));

        Assertions.assertEquals(EMAIL, username);
    }

    @Test
    void validateToken_Ok() {
        String token = jwtTokenProvider.createToken(EMAIL, List.of(ROLE.getRoleName().name()));
        boolean actual = jwtTokenProvider.validateToken(token);

        Assertions.assertTrue(actual);
    }

    @Test
    void validateToken_NotOk() {
        String token = "some string";

        try {
            jwtTokenProvider.validateToken(token);
        } catch (RuntimeException e) {
            Assertions.assertEquals("Expired or invalid JWT token", e.getMessage());
        }
    }
}

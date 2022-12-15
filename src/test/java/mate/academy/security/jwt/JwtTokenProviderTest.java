package mate.academy.security.jwt;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;

class JwtTokenProviderTest {
    private static final String EMAIL = "someemail@gmail.com";
    private static final String PASSWORD = "password";
    private static final Role.RoleName ROLE = Role.RoleName.USER;
    private static final String JWT_CORRECT_KEY_HEADER = "eyJhbGciOiJIUzI1NiJ9";
    private static final String JWT_CORRECT_REGEX = "(^[\\w-]*\\.[\\w-]*\\.[\\w-]*$)";
    private static JwtTokenProvider jwtTokenProvider;
    private static UserDetailsService userDetailsService;
    private static User user;
    private static Role role;

    @BeforeAll
    static void beforeAll() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        try {
            Field secretKey = jwtTokenProvider.getClass().getDeclaredField("secretKey");
            secretKey.setAccessible(true);
            secretKey.set(jwtTokenProvider, "secret");
            Field validityTime = jwtTokenProvider.getClass()
                    .getDeclaredField("validityInMilliseconds");
            validityTime.setAccessible(true);
            validityTime.set(jwtTokenProvider, 3600000L);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        user = new User();
        role = new Role();
        role.setRoleName(ROLE);
    }

    @BeforeEach
    void setUp() {
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(role));
    }

    @Test
    void createToken_Ok() {
        String actual = jwtTokenProvider.createToken(EMAIL, List.of(ROLE.name()));
        String[] splitActual = actual.split("\\.");
        Assertions.assertEquals(JWT_CORRECT_KEY_HEADER, splitActual[0],
                "Expected " + JWT_CORRECT_KEY_HEADER + ", but was " + splitActual[0]);
        Assertions.assertTrue(actual.matches(JWT_CORRECT_REGEX),
                "Expected true value, but was false");
    }

    @Test
    void getUsername_Ok() {
        String actual = jwtTokenProvider.getUsername(jwtTokenProvider
                .createToken(EMAIL, List.of(ROLE.name())));
        Assertions.assertEquals(EMAIL, actual,
                "Expected " + EMAIL + ", but was " + actual);
    }

    @Test
    void validateToken_Ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(jwtTokenProvider
                        .createToken(EMAIL, List.of(ROLE.name()))),
                "Expected true, but was false");
    }

    @Test
    void validateToken_NotOk() {
        Assertions.assertThrows(RuntimeException.class, () ->
                        jwtTokenProvider.validateToken(jwtTokenProvider
                                .createToken(EMAIL, List.of(ROLE.name())) + 1),
                "Expected throws RuntimeException, but nothing was throws");
    }
}

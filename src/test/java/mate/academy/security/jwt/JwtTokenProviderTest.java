package mate.academy.security.jwt;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;

public class JwtTokenProviderTest {
    private static final String BOB_VALID_EMAIL = "bob@mail.com";
    private static final String SIMPLE_NUMBER_PASSWORD = "1234";
    private static final String USER_ROLE_STRING = "USER";
    private static final String SECRET_KEY = "secret";
    private static final String JWT_CORRECT_KEY_HEADER = "eyJhbGciOiJIUzI1NiJ9";
    private static final String JWT_CORRECT_REGEX = "(^[\\w-]*\\.[\\w-]*\\.[\\w-]*$)";
    private static User bob;
    private static Role userRole;
    private static JwtTokenProvider jwtTokenProvider;
    private static UserDetailsService userDetailsService;

    @BeforeAll
    static void setUp() {
        userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        bob = new User();
        bob.setEmail(BOB_VALID_EMAIL);
        bob.setPassword(SIMPLE_NUMBER_PASSWORD);
        bob.setRoles(Set.of(userRole));

        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        try {
            Field secretKey = jwtTokenProvider.getClass().getDeclaredField("secretKey");
            secretKey.setAccessible(true);
            secretKey.set(jwtTokenProvider, SECRET_KEY);
            Field validityTime = jwtTokenProvider.getClass()
                    .getDeclaredField("validityInMilliseconds");
            validityTime.setAccessible(true);
            validityTime.set(jwtTokenProvider, 3600000L);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException("Can't mock jwtTokenProvider", e);
        }
    }

    @Test
    void createValidJwtString_Ok() {
        String actual = jwtTokenProvider.createToken(BOB_VALID_EMAIL, List.of(USER_ROLE_STRING));
        System.out.println(actual);
        String[] splitActual = actual.split("\\.");
        Assertions.assertEquals(JWT_CORRECT_KEY_HEADER, splitActual[0],
                "You return incorrect header");
        Assertions.assertTrue(actual.matches(JWT_CORRECT_REGEX),
                "You should made correct format of jwt, like xxxxxx.yyyyyyy.zzzzzz");
    }

    @Test
    void getUsername_Ok() {
        String actual = jwtTokenProvider.getUsername(jwtTokenProvider
                .createToken(BOB_VALID_EMAIL, List.of(USER_ROLE_STRING)));
        Assertions.assertEquals(BOB_VALID_EMAIL, actual, "You should return correct username");
    }

    @Test
    void validateToken_Ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(jwtTokenProvider
                        .createToken(BOB_VALID_EMAIL, List.of(USER_ROLE_STRING))),
                "You need correctly validate token");
    }

    @Test
    void validateToken_NotOk() {
        Assertions.assertThrows(RuntimeException.class, () ->
                        jwtTokenProvider.validateToken(jwtTokenProvider
                                .createToken(BOB_VALID_EMAIL, List.of(USER_ROLE_STRING)) + 1),
                "You need to throw some runtime exception when you gen incorrect token");
    }
}

package mate.academy.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Pattern;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;

class JwtTokenProviderTest {
    private static final String FIELD_SECRET_NAME = "secretKey";
    private static final String FIELD_VALIDITY_NAME = "validityInMilliseconds";
    private static final String SIGNATURE_SECRET_FOR_TEST = "secret";
    private static final String TOKEN_SECRET_PART = "eyJhbGciOiJIUzI1NiJ9.";
    private static final long DEFAULT_VALIDITY_TIME = 3600000L;
    private static final String USERNAME = "testUser";
    private static final List<String> ROLES_LIST = List.of(Role.RoleName.USER.name());
    private static final Pattern JWT_PARTS_SPLIT_REGEX = Pattern.compile("\\.");
    private static final int JWT_PARTS_LENGTH = 3;
    private static JwtTokenProvider jwtTokenProvider;

    @BeforeAll
    static void beforeAll() {
        jwtTokenProvider = new JwtTokenProvider(Mockito.mock(UserDetailsService.class));
        try {
            Field secretKey = jwtTokenProvider.getClass().getDeclaredField(FIELD_SECRET_NAME);
            Field validity = jwtTokenProvider.getClass().getDeclaredField(FIELD_VALIDITY_NAME);
            secretKey.trySetAccessible();
            validity.trySetAccessible();
            secretKey.set(jwtTokenProvider, SIGNATURE_SECRET_FOR_TEST);
            validity.setLong(jwtTokenProvider, DEFAULT_VALIDITY_TIME);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Unable to use reflection to set values for "
                            + "simple " + FIELD_SECRET_NAME + " and "
                            + "default " + FIELD_VALIDITY_NAME + " fields for tests", e);
        }
    }

    @Test
    void createToken_nullValue_notOk() {
        String token = jwtTokenProvider.createToken(USERNAME, ROLES_LIST);

        assertNotNull(token, "JWT must be not null");
    }

    @Test
    void createToken_format_ok() {
        String token = jwtTokenProvider.createToken(USERNAME, ROLES_LIST);

        assertTrue(token.startsWith(TOKEN_SECRET_PART),
                "JWT signature part does not match the encrypted secret key");

        String[] actualPartsLength = JWT_PARTS_SPLIT_REGEX.split(token);
        assertEquals(JWT_PARTS_LENGTH, actualPartsLength.length,
                "JWT format mismatch.");
    }

    @Test
    void getUsername_parsedSame_ok() {
        String token = jwtTokenProvider.createToken(USERNAME, ROLES_LIST);

        String actualUsername = jwtTokenProvider.getUsername(token);
        assertEquals(USERNAME, actualUsername,
                "Parsed username from JWT does not match the username passed to args");
    }

    @Test
    void validateToken_validDate_ok() {
        String token = jwtTokenProvider.createToken(USERNAME, ROLES_LIST);

        assertTrue(jwtTokenProvider.validateToken(token),
                "Just created JWT method must be valid");
    }
}

package mate.academy.security.jwt;

import java.lang.reflect.Field;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import mate.academy.util.UserTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

class JwtTokenProviderTest {
    private static final String CORRECT_TOKEN = "eyJhbGciOiJIUzI1NiJ9"
            + ".eyJzdWIiOiJib2JAZ21haWwuY29tIiwicm9sZXMiOlsiVVNFUiJdLCJpYXQ"
            + "iOjE2NTE2NjMwNTcsImV4cCI6OTQ0NTEwMjQyNjU3fQ"
            + ".mk_l0poZNHTAqbunh6BQ22KQfoIowdbs4MsfR9ixKMw";
    private final static String INCORRECT_TOKEN = "11111111111"
            + ".2222222222222"
            + ".3333333333333";
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() throws RuntimeException {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        Class<JwtTokenProvider> jwtTokenProviderClass = JwtTokenProvider.class;
        try {
            jwtTokenProvider = jwtTokenProviderClass
                    .getConstructor(UserDetailsService.class)
                    .newInstance(userDetailsService);

            Field secretKeyFiled = jwtTokenProviderClass
                    .getDeclaredField("secretKey");

            Field validityInMilliseconds = jwtTokenProviderClass
                    .getDeclaredField("validityInMilliseconds");
            secretKeyFiled.setAccessible(true);
            validityInMilliseconds.setAccessible(true);
            secretKeyFiled.set(jwtTokenProvider, "secret");
            validityInMilliseconds.set(jwtTokenProvider, 3600000L);
        } catch (Exception e) {
            throw new RuntimeException("Can't create JwtTokenProvider", e);
        }
        jwtTokenProvider.init();
    }

    @Test
    void createToken_Ok() {
        String token = jwtTokenProvider.createToken(UserTestUtil.EMAIL,
                List.of(Role.RoleName.USER.name()));
        Assertions.assertNotNull(token);
        Assertions.assertEquals(3, token.split("\\.").length);
    }

    @Test
    void validateToken_Ok() {
        boolean actual = jwtTokenProvider.validateToken(CORRECT_TOKEN);
        Assertions.assertTrue(actual);
    }

    @Test
    void validateToken_NotOk() {
        try {
            boolean actual = jwtTokenProvider.validateToken(INCORRECT_TOKEN);
            Assertions.fail("Expected to receive Exception "
                    + "when validate incorrect token");
        } catch (Exception e) {
            Assertions.assertEquals("Expired or invalid JWT token",
                    e.getMessage());
        }
    }

    @Test
    void getUsername_Ok() {
        String actual = jwtTokenProvider.getUsername(CORRECT_TOKEN);
        Assertions.assertEquals(UserTestUtil.EMAIL, actual);
    }

    @Test
    void getAuthentication_Ok() {
        User.UserBuilder userBuilder = User.withUsername(UserTestUtil.EMAIL);
        userBuilder.password(UserTestUtil.PASSWORD);
        userBuilder.roles(Role.RoleName.USER.name());
        Mockito.when(userDetailsService.loadUserByUsername(UserTestUtil.EMAIL))
                .thenReturn(userBuilder.build());
        Authentication actual = jwtTokenProvider.getAuthentication(CORRECT_TOKEN);
        Assertions.assertEquals(UserTestUtil.EMAIL, actual.getName());
        Assertions.assertTrue(actual.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + Role.RoleName.USER.name())));
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn("Bearer " + CORRECT_TOKEN);
        String actual = jwtTokenProvider.resolveToken(req);
        Assertions.assertEquals(CORRECT_TOKEN, actual);
    }

}
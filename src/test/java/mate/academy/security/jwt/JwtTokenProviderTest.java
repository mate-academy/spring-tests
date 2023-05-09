package mate.academy.security.jwt;

import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String SECRET_KEY = "secret";
    private static final long EXPIRE_LENGTH = 3600000;
    private static final String USER_EMAIL = "john@me.com";
    private static final String USER_PASSWORD = "12345678";
    private static final List<String> USER_ROLES = List.of("USER");
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9."
                    + "eyJzdWIiOiJqb2huQG1lLmNvbSIsInJvbGVzIjpbIlVTRV"
                    + "IiXSwiaWF0IjoxNjgzNTQxNTg4LCJleHAiOjE2ODM1NDUxODh9."
                    + "o8lfs7A_MgRqAczP_7PUmjpkGJh4h0Z7LTVqtjDdlpo";
    private User user;
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider,
                "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtTokenProvider,
                "validityInMilliseconds", EXPIRE_LENGTH);
        jwtTokenProvider.init();

        user = new User();
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void createToken_Ok() {
        List<String> roles = user.getRoles().stream()
                .map(role -> role.getRoleName().name())
                .toList();
        String actual = jwtTokenProvider.createToken(USER_EMAIL, roles);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(3, actual.split("\\.").length);
        Assertions.assertNotEquals("", actual);
    }

    @Test
    void getAuthentication_Ok() {
        UserBuilder builder = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail());
        builder.password(user.getPassword());
        builder.roles(user.getRoles().stream()
                .map(role -> role.getRoleName().name())
                .toArray(String[]::new));
        UserDetails userDetails = builder.build();
        Mockito.when(userDetailsService.loadUserByUsername(USER_EMAIL))
                                        .thenReturn(userDetails);
        String actualToken = jwtTokenProvider.createToken(USER_EMAIL, USER_ROLES);
        Authentication actualAuth = jwtTokenProvider.getAuthentication(actualToken);
        org.springframework.security.core.userdetails.User actual =
                (org.springframework.security.core.userdetails.User) actualAuth.getPrincipal();
        Assertions.assertNotNull(actualAuth);
        Assertions.assertEquals(user.getEmail(), actual.getUsername());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
        Assertions.assertNotEquals("", actual.getUsername());
        Assertions.assertNotEquals("", actual.getPassword());
        Assertions.assertNotEquals(Set.of(), actual.getAuthorities());
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization"))
                            .thenReturn("Bearer " + TOKEN);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNotNull(actual);
        Assertions.assertNotEquals("", actual);
        Assertions.assertEquals(3, actual.split("\\.").length);
        Assertions.assertEquals(TOKEN, actual);
    }

    @Test
    void resolveToken_notOk() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization"))
                .thenReturn(TOKEN);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNull(actual);
    }

    @Test
    void validateToken_Ok() {
        String actualToken = jwtTokenProvider.createToken(USER_EMAIL, USER_ROLES);
        boolean actual = jwtTokenProvider.validateToken(actualToken);
        Assertions.assertTrue(actual);
    }

    @Test
    void validateToken_notOk() {
        String invalidToken = TOKEN.substring(22);
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(invalidToken));
    }
}

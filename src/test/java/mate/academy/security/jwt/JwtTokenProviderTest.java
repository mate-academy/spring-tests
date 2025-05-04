package mate.academy.security.jwt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String VALID_USERNAME = "bob";
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "12345";
    private static final List<String> ROLES = List.of(Role.RoleName.USER.name());
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private String token;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider,
                "secretKey", "google");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                3600000L);
        token = jwtTokenProvider.createToken(VALID_USERNAME, ROLES);

    }

    @Test
    void createToken_NotNull() {
        Assertions.assertNotNull(token);
        Assertions.assertTrue(token.length() > 0);
    }

    @Test
    void getAuthentication_Ok() {
        UserDetails userDetails = User.withUsername(VALID_USERNAME)
                .password(PASSWORD)
                .roles(String.valueOf(ROLES))
                .build();
        Mockito.when(userDetailsService.loadUserByUsername(VALID_USERNAME))
                .thenReturn(userDetails);
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        String actualUsername = ((UserDetails) authentication.getPrincipal()).getUsername();
        Assertions.assertNotNull(authentication);
        Assertions.assertEquals(VALID_USERNAME, actualUsername);
    }

    @Test
    void resolveToken() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization"))
                .thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(req);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(token, actual);
    }

    @Test
    void getUserName_Ok() {
        String username = jwtTokenProvider.getUsername(token);
        Assertions.assertNotNull(username);
        Assertions.assertEquals(VALID_USERNAME, username);
    }

    @Test
    void validateToken_Ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_tokenIsNull_notOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(null));
    }
}

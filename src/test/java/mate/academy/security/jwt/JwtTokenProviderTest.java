package mate.academy.security.jwt;

import static org.junit.jupiter.api.Assertions.assertThrows;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.List;

class JwtTokenProviderTest {
    private static final String VALID_EMAIL = "user@email.com";
    private static final String VALID_PASSWORD = "user1234";
    private static final String VALID_USER_ROLE = "USER";
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.ey"
            + "JzdWIiOiJ1c2VyQGVtYWlsLmNvbSIsInJvbGVzIjpbIlVTRVIiXSwi"
            + "aWF0IjoxNjY2Njc1NzEzLCJleHAiOjE2NjY2NzkzMTN9.rfrpVqkb"
            + "EfgU-IaAxH3l5ZO_1C8IFzoqGyLcN0KyoHM";
    private static JwtTokenProvider jwtTokenProvider;
    private static UserDetailsService userDetailsService;

    @BeforeAll
    static void beforeAll() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider,
                "secretKey", "SECRET");
        ReflectionTestUtils.setField(jwtTokenProvider,
                "validityInMilliseconds", 3600000L);
    }

    @Test
    public void createToken_ValidCredential_Ok() {
        List<String> roles = List.of(VALID_USER_ROLE);
        String token = jwtTokenProvider.createToken(VALID_EMAIL, roles);
        Assertions.assertNotNull(token);
    }

    @Test
    public void getAuthentication_ValidCredential_Ok() {
        User.UserBuilder builder = User.withUsername(VALID_EMAIL);
        UserDetails userDetails = builder.password(VALID_PASSWORD).roles(VALID_USER_ROLE).build();
        Mockito.when(userDetailsService.loadUserByUsername(Mockito.any()))
                .thenReturn(userDetails);
        Authentication retrievedAuth = jwtTokenProvider.getAuthentication(VALID_TOKEN);
        User retrievedUser = (User) retrievedAuth.getPrincipal();
        Assertions.assertNotNull(retrievedAuth);
        Assertions.assertNotNull(retrievedUser);
        Assertions.assertEquals(VALID_EMAIL, retrievedUser.getUsername());
        Assertions.assertEquals(VALID_PASSWORD, retrievedUser.getPassword());
    }

    @Test
    public void getUsername_ValidToken_Ok() {
        String actual = jwtTokenProvider.getUsername(VALID_TOKEN);
        Assertions.assertEquals(VALID_EMAIL, actual);
    }

    @Test
    public void resolveToken_ValidToken_Ok() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn("Bearer " + VALID_TOKEN);
        String actual = jwtTokenProvider.resolveToken(req);
        Assertions.assertEquals(VALID_TOKEN, actual);
    }

    @Test
    public void validateToken_ValidToken_Ok() {
        boolean actual = jwtTokenProvider.validateToken(VALID_TOKEN);
        Assertions.assertTrue(actual);
    }

    @Test
    public void validateToken_InvalidToken_NotOk() {
        assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken("Invalid"),
                "RuntimeException to be thrown, but nothing was thrown");
    }
}

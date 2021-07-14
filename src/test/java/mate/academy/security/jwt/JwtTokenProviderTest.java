package mate.academy.security.jwt;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
    private static final String USER_TOKEN_MADE_BEFORE_TESTCLASS = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ"
            + "sdWN5MTIzNEBnbWFpbC5jb20iLCJyb2xlcyI6WyJSb2xle2lkPW51bGwsIH"
            + "JvbGVOYW1lPVVTRVJ9Il0sImlhdCI6MTYyNjI2Mzg1MywiZXhwIjoxNjI2M"
            + "jY3NDUzfQ.0Zy9qCg88tbH6DiLwQpwzaTfC7j7Em9tzWI42aSblJo";
    private static final String RANDOM_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3"
            + "MiOiJPbmxpbmUgSldUIEJ1aWxkZXIiLCJpYXQiOjE2MjYyNjYzMDQsI"
            + "mV4cCI6MTY1NzgwMjMxNiwiYXVkIjoid3d3LmV4YW1wbGUuY29tIiwic"
            + "3ViIjoianJvY2tldEBleGFtcGxlLmNvbSIsIkdpdmVuTmFtZSI6Ikpva"
            + "G5ueSIsIlN1cm5hbWUiOiJSb2NrZXQiLCJFbWFpbCI6Impyb2NrZXRAZX"
            + "hhbXBsZS5jb20iLCJSb2xlIjpbIk1hbmFnZXIiLCJQcm9qZWN0IEFkbWl"
            + "uaXN0cmF0b3IiXX0.qvTytnF6eMLqFjz5LPk72PmkHN2bhfl192H5zA4CMc4";
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private User user;
    private List<String> roles;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000L);
        jwtTokenProvider.init();
        user = new User();
        user.setId(1L);
        user.setEmail("lucy1234@gmail.com");
        user.setPassword("12345tyr");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        roles = user.getRoles().stream()
                .map(x -> x.getRoleName().name())
                .collect(Collectors.toList());
    }

    @Test
    void createToken_ok() {
        String token = jwtTokenProvider.createToken(user.getEmail(), roles);
        Assertions.assertNotNull(token);
        Assertions.assertNotEquals(USER_TOKEN_MADE_BEFORE_TESTCLASS, token);
        Assertions.assertNotEquals(RANDOM_TOKEN, token);
    }

    @Test
    void getAuthentication_validToken_ok() {
        UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(user.getEmail());
        builder.password(user.getPassword());
        builder.roles(user.getRoles().stream()
                .map(x -> x.getRoleName().name())
                .toArray(String[]::new));
        UserDetails userDetails = builder.build();
        Mockito.when(userDetailsService.loadUserByUsername("lucy1234@gmail.com")).thenReturn(userDetails);
        String token = jwtTokenProvider.createToken(user.getEmail(), roles);
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        Assertions.assertNotNull(authentication);
        Assertions.assertEquals(user.getEmail(), authentication.getName());
    }

    @Test
    void getAuthentication_invalidToken_notOk() {
        Assertions.assertThrows(Exception.class, () -> jwtTokenProvider.getAuthentication(RANDOM_TOKEN));
    }

    @Test
    void getUsername_validToken_ok() {
        String token = jwtTokenProvider.createToken(user.getEmail(), roles);
        String username = jwtTokenProvider.getUsername(token);
        Assertions.assertEquals(user.getEmail(), username);
    }

    @Test
    void getUsername_invalidToken_notOk() {
        Assertions.assertThrows(Exception.class, () -> jwtTokenProvider.getUsername(RANDOM_TOKEN));
    }

    @Test
    void resolveToken_validRequest_ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer request with token");
        Assertions.assertEquals("request with token", jwtTokenProvider.resolveToken(request));
    }

    @Test
    void resolveToken_invalidRequest_ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("111");
        Assertions.assertNull(jwtTokenProvider.resolveToken(request));
    }

    @Test
    void validateToken_validToken_ok() {
        String token = jwtTokenProvider.createToken(user.getEmail(), roles);
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_invalidToken_notOk() {
        Assertions.assertThrows(Exception.class, () -> jwtTokenProvider.validateToken(RANDOM_TOKEN));
    }
}
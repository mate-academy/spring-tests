package mate.academy.security.jwt;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
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
    private JwtTokenProvider jwtTokenProvider;
    UserDetailsService userDetailsService;
    private List<String> ROLES;
    private String login;
    private String token;
    String bchupikaToken;

    @BeforeEach
    void setUp() {
        token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib2IiLCJyb2xlcyI6WyJBRE1JTiJdLCJpYXQiO"
                + "jE2NDg5MDg0MTgsImV4cCI6MTY0ODkxMjAxOH0.lwWzWbvnE4wzvV40JXhM5eXirTw9DproWztDu7WocGw";
        login = "bchupika@mate.academy";
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        jwtTokenProvider = Mockito.spy(jwtTokenProvider);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000L);
        jwtTokenProvider.init();
        bchupikaToken = jwtTokenProvider.createToken(login, List.of("ADMIN"));
        ROLES = List.of("ADMIN");
    }

    @Test
    void createToken_Ok() {
        String bchupikaToken = jwtTokenProvider.createToken(login, List.of("ADMIN"));
        Assertions.assertNotNull(bchupikaToken);
    }

    @Test
    void getUserName_Ok() {
        String actual = jwtTokenProvider.getUsername(bchupikaToken);
        Assertions.assertEquals(login, actual);
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(req);
        Assertions.assertEquals(token, actual);
    }

    @Test
    void validateToken_Ok() {
        boolean actual = jwtTokenProvider.validateToken(bchupikaToken);
        Assertions.assertTrue(actual);
    }

    @Test
    void getAuthentication_Ok() {
        User.UserBuilder builder;
        builder = org.springframework.security.core.userdetails.User.withUsername("bchupika@mate.academy");
        builder.password("12345678");
        builder.roles(ROLES.toArray(String[]::new));
        UserDetails userDetails = builder.build();
        Mockito.when(userDetailsService.loadUserByUsername(Mockito.any())).thenReturn(userDetails);

        Authentication actual = jwtTokenProvider.getAuthentication(bchupikaToken);
        User actualUser = (User) actual.getPrincipal();
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals("bchupika@mate.academy", actualUser.getUsername());
    }
}
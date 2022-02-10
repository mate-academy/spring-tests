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
    private String password;
    private String token;
    private String bobToken;

    @BeforeEach
    void setUp() {
        token = "eyJhbGciOiJIUzI1NiJ9"
                + ".eyJzdWIiOiJib2JAaS51YSIsInJvbGVzIjpbIlVTRVIiXSwiaWF0IjoxNjQ0MzU2MTAyLCJleHAiOjE"
                + "2NDQzNTk3MDJ9.9Audbu7yJul9ZTk9Bze_muV31IJZJwfZheW_HRc4bSo";
        login = "bob@i.ua";
        password = "1234";
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        jwtTokenProvider = Mockito.spy(jwtTokenProvider);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000L);
        jwtTokenProvider.init();
        bobToken = jwtTokenProvider.createToken(login, List.of("USER"));
        ROLES = List.of("USER");
    }

    @Test
    void createToken_Ok() {
        String bobToken = jwtTokenProvider.createToken(login, List.of("USER"));
        Assertions.assertNotNull(bobToken);
    }

    @Test
    void getUserName_Ok() {
        String actual = jwtTokenProvider.getUsername(bobToken);
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
        boolean actual = jwtTokenProvider.validateToken(bobToken);
        Assertions.assertTrue(actual);
    }

    @Test
    void getAuthentication_Ok() {
        User.UserBuilder builder;
        builder = org.springframework.security.core.userdetails.User.withUsername(login);
        builder.password(password);
        builder.roles(ROLES.toArray(String[]::new));
        UserDetails userDetails = builder.build();
        Mockito.when(userDetailsService.loadUserByUsername(Mockito.any())).thenReturn(userDetails);

        Authentication actual = jwtTokenProvider.getAuthentication(bobToken);
        User actualUser = (User) actual.getPrincipal();
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(login, actualUser.getUsername());
    }
}

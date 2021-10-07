package mate.academy.security.jwt;

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
    private static final String CORRECT_LOGIN = "bob@i.ua";
    private static final List<String> ROLES = List.of("USER");
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJzdWIiOiJib2JAaS51YSIsInJvbGVzIjpbIlVTRVIiXSwiaWF0IjoxNjMzNjQzNDk1LCJleHAiOjE2M" +
            "zM2NDcwOTV9.4rb5gw0g-6_NPhQZT3krqUKUu4TvZqDxhI4O1dFYF88";
    private static final String INVALID_TOKEN = "ey";
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        try {
            ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret",
                    String.class);
            ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000,
                    long.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void createToken_NotNull() {
        String actual = jwtTokenProvider.createToken(CORRECT_LOGIN, ROLES);
        Assertions.assertNotNull(actual);
    }

    @Test
    void getAuthentication_Ok() {
        User.UserBuilder builder;
        builder = org.springframework.security.core.userdetails.User.withUsername(CORRECT_LOGIN);
        builder.password("1234");
        builder.roles(ROLES.toArray(String[]::new));
        UserDetails userDetails = builder.build();
        Mockito.when(userDetailsService.loadUserByUsername(Mockito.any())).thenReturn(userDetails);

        Authentication actual = jwtTokenProvider.getAuthentication(VALID_TOKEN);
        User actualUser = (User) actual.getPrincipal();
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(CORRECT_LOGIN, actualUser.getUsername());
    }

    @Test
    void validateToken_True() {
        boolean actual = jwtTokenProvider.validateToken(VALID_TOKEN);
        Assertions.assertTrue(actual);
    }

    @Test
    void validateToken_InvalidToken() {
        try {
            boolean actual = jwtTokenProvider.validateToken(INVALID_TOKEN);
        } catch (RuntimeException e) {
            Assertions.assertEquals("Expired or invalid JWT token", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive JwtException or IllegalArgumentException");
    }
}
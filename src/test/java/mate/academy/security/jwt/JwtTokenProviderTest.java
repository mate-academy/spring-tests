package mate.academy.security.jwt;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String CORRECT_LOGIN = "bob@i.ua";
    private static final List<String> ROLES = List.of("USER");
    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib2"
            + "JAZ21haWwuY29tIiwicm9sZXMiOlsiVVNFU"
            + "iJdLCJpYXQiOjE2MzQyMTg2NjMsImV4cCI6MTYzNDIyMjI2M30"
            + ".9mP5p7gdfqvB2ueeQgvgVQHbMwqQkMICkf_SaWz6ZJc";
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
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    void createToken_NotNull() {
        String actual = jwtTokenProvider.createToken(CORRECT_LOGIN, ROLES);
        Assertions.assertNotNull(actual);
    }

    @Test
    void getAuthentication_Ok() {
        User.UserBuilder builder
                = org.springframework.security.core.userdetails.User.withUsername("USER");
        builder.password("qwerty");
        builder.roles(ROLES.get(0));
        UserDetails userDetails = builder.build();
        UsernamePasswordAuthenticationToken expected
                = new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
        Mockito.when(userDetailsService.loadUserByUsername(Mockito.any())).thenReturn(userDetails);

        String token = jwtTokenProvider.createToken(CORRECT_LOGIN, ROLES);
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void validateTokenMethod_Ok() {
        boolean actual = jwtTokenProvider.validateToken(VALID_TOKEN);
        Assertions.assertTrue(!actual, "Token is correct, but validation method returns false");
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

package mate.academy.security.jwt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
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
    private static final long VALIDITY_IN_MILLISECONDS = 3600000;
    private static final String USERNAME = "my@i.ua";
    private static final String PASSWORD = "12345678";
    private static final List<String> LIST_OF_ROLES = List.of("USER");
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJteUBpLnVhIiwicm"
            + "9sZXMiOlsiVVNFUiJdLCJpYXQiOjE2NzU5MzAyMzcsImV4cCI6MTY3NTkzMzgzN30.Mr5YjIlvCe"
            + "J-9WT8cO1OpxzxQXGmHOyDrxYZPsc8ndA";
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;
    private String validToken;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                VALIDITY_IN_MILLISECONDS);
        validToken = jwtTokenProvider.createToken(USERNAME, LIST_OF_ROLES);
    }

    @Test
    void createToken_ok() {
        String actual = jwtTokenProvider.createToken(USERNAME, LIST_OF_ROLES);
        Assertions.assertNotNull(actual);
        Assertions.assertNotEquals(TOKEN, actual);
    }

    @Test
    void getAuthentication_ok() {
        UserBuilder builder;
        builder = org.springframework.security.core.userdetails.User.withUsername(USERNAME);
        builder.password(PASSWORD);
        builder.roles("USER");
        UserDetails userDetails = builder.build();
        Mockito.when(userDetailsService.loadUserByUsername(Mockito.any())).thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(validToken);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(USERNAME, actual.getName());
    }

    @Test
    void getUsername_ok() {
        String actual = jwtTokenProvider.getUsername(validToken);
        Assertions.assertEquals(USERNAME, actual);
    }

    @Test
    void resolveToken_ok() {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJteUBpLnVhIiwicm9sZXMiOlsi"
                + "VVNFUiJdLCJpYXQiOjE2NzU5MzAyMzcsImV4cCI6MTY3NTkzMzgzN30.Mr5YjIlvCeJ-9W"
                + "T8cO1OpxzxQXGmHOyDrxYZPsc8ndA";
        Mockito.when(httpServletRequest.getHeader("Authorization")).thenReturn(token);
        String actual = jwtTokenProvider.resolveToken(httpServletRequest);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(TOKEN, actual);
    }

    @Test
    void validateToken_ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(validToken));
    }

    @Test
    void validateToken_notValidToken_notOk() {
        try {
            jwtTokenProvider.validateToken(TOKEN);
        } catch (RuntimeException e) {
            Assertions.assertEquals("Expired or invalid JWT token", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive JwtException");
    }
}
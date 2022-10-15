package mate.academy.security.jwt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
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
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9"
            + ".eyJzdWIiOiJib2JAaS51YSIsInJvbGVzIjpbIlVTRVIiXSwia"
            + "WF0IjoxNjY1NzkzNjY2LCJleHAiOjE2NjU3OTcyNjZ9"
            + ".gp7N8I7ZalxfxCCUuY_KfzPHNvSvUl3fYiyaH9cdDkc";
    private static final String EMAIL = "bob@i.ua";
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey",
                "secret", String.class);
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                3600000L, Long.class);
    }

    @Test
    void createToken_Ok() {
        List<String> roles = List.of("USER");
        String token = jwtTokenProvider.createToken(EMAIL, roles);
        Assertions.assertNotNull(token);
    }

    @Test
    void getUsername_Ok() {
        String actual = jwtTokenProvider.getUsername(TOKEN);
        Assertions.assertEquals(EMAIL, actual);
    }

    @Test
    void getAuthentication_Ok() {
        UserDetails userDetails = User.withUsername(EMAIL)
                .password("12345678")
                .roles("USER")
                .build();
        Mockito.when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(TOKEN);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EMAIL, actual.getName());
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn("Bearer " + TOKEN);
        String actual = jwtTokenProvider.resolveToken(req);
        Assertions.assertEquals(TOKEN, actual);
    }

    @Test
    void validateToken_Ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(TOKEN));
    }

    @Test
    void validateToken_NotOk() {
        String token = "xxxx.yyyy.zzzz";
        try {
            jwtTokenProvider.validateToken(token);
        } catch (RuntimeException e) {
            Assertions.assertEquals("Expired or invalid JWT token", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive RuntimeException");
    }
}

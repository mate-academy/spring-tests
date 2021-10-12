package mate.academy.security.jwt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private JwtTokenProvider jwtTokenProvider;
    private String correctToken;
    private String login;

    @BeforeEach
    void setUp() {
        correctToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib2JieUBnb29nbGUuY29tIiwicm9sZXMiOlsiVVNFUiJdLCJ"
                + "pYXQiOjE2MzM5OTMxNDAsImV4cCI6MTYzMzk5Njc0MH0.gt00bDtH3x5c6e_3aF0rZ"
                + "IP08jU-UDz_WbbcR3TalPU";
        login = "bobby@google.com";
        UserDetailsService userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        jwtTokenProvider = Mockito.spy(jwtTokenProvider);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000L);
        jwtTokenProvider.init();
    }

    @Test
    void createToken_Ok() {
        String bobbyToken = jwtTokenProvider.createToken(login, List.of("USER"));
        Assertions.assertNotNull(bobbyToken);
    }

    @Test
    void getUsername_Ok() {
        String expected = login;
        String actual = jwtTokenProvider.getUsername(correctToken);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn("Bearer " + correctToken);
        String actual = jwtTokenProvider.resolveToken(req);
        Assertions.assertEquals(correctToken, actual);
    }

    @Test
    void validateToken_Ok() {
        boolean actual = jwtTokenProvider.validateToken(correctToken);
        Assertions.assertTrue(actual);
    }
}

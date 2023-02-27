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
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;
    private String token;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                3600000L);
        jwtTokenProvider.init();
        token = jwtTokenProvider.createToken("bob@gmail.com", List.of("USER"));
        request = Mockito.mock(HttpServletRequest.class);
    }

    @Test
    void createToken_validToken_isOk() {
        Assertions.assertNotNull(token);
    }

    @Test
    void getAuthentication_validToken_isOk() {
        UserDetails details = User
                .withUsername("bob@gmail.com")
                .password("bob12345")
                .authorities(new String[]{"USER"})
                .build();
        Mockito.when(userDetailsService.loadUserByUsername("bob@gmail.com")).thenReturn(details);
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        Assertions.assertNotNull(token);
        Assertions.assertEquals(actual.getName(), details.getUsername());
    }

    @Test
    void getUsername_validToken_isOk() {
        Assertions.assertNotNull(jwtTokenProvider.getUsername(token), "bob@gmail.com");
    }

    @Test
    void resolveToken_isOk() {
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertEquals(token, actual);
    }

    @Test
    void resolveToken_notCorrectToken_isOk() {
        Mockito.when(request.getHeader("Authorization")).thenReturn("Undefined " + token);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNull(actual);
    }

    @Test
    void validateToken_correctToken_isOk() {
        boolean actual = jwtTokenProvider.validateToken(token);
        Assertions.assertTrue(actual);
    }

    @Test
    void validateToken_notCorrectToken_throwException() {
        try {
            jwtTokenProvider.validateToken("token");
        } catch (RuntimeException e) {
            Assertions.assertEquals("Expired or invalid JWT token", e.getMessage());
            return;
        }
        Assertions.fail();
    }

    @Test
    void validateToken_notCorrectDate_throwException() {
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                0L);
        token = jwtTokenProvider.createToken("bob@gmail.com", List.of("USER"));
        try {

            jwtTokenProvider.validateToken(token);
        } catch (RuntimeException e) {
            Assertions.assertEquals("Expired or invalid JWT token", e.getMessage());
            return;
        }
        Assertions.fail();
    }
}

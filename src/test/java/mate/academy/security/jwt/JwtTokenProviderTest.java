package mate.academy.security.jwt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
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
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private List<String> roleNames = List.of("USER");
    private static final String CORRECT_LOGIN = "lisa.simpson@gmail.com";
    private static final String PASSWORD = "123456";
    private static final String RESOLVE_STRING = "q1w2e3r4";
    private static final String CORRECT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsaXNhLnNpbXBzb25A"
            + "Z21haWwuY29tIiwicm9sZXMiOlsiVVNFUiJdLCJpYXQiOjE2MzE3MDAwMjQsImV4cCI6MTYzMTcwMzYyNH0"
            + ".MbDQmCDF3A_FG_UJRxsapiFroKtOfDueUT5xadEVwlA";

    @BeforeEach
    void setUp_Ok() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        jwtTokenProvider = Mockito.spy(jwtTokenProvider);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000L);
        jwtTokenProvider.init();
    }

    @Test
    void createToken_Ok() {
        String actual = jwtTokenProvider.createToken(CORRECT_LOGIN, roleNames);
        Assertions.assertNotNull(actual);
    }

    @Test
    void getAuthentication_Ok() {
        User.UserBuilder builder = User.withUsername(CORRECT_LOGIN);
        builder.password(PASSWORD);
        builder.roles("USER");
        UserDetails userDetails = builder.build();
        UsernamePasswordAuthenticationToken expected = new UsernamePasswordAuthenticationToken(
                userDetails, "", userDetails.getAuthorities());

        Mockito.when(userDetailsService.loadUserByUsername(Mockito.any())).thenReturn(userDetails);

        Authentication actual = jwtTokenProvider.getAuthentication(CORRECT_TOKEN);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getUsername_Ok() {
        String expected = CORRECT_LOGIN;
        String actual = jwtTokenProvider.getUsername(CORRECT_TOKEN);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + RESOLVE_STRING);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertEquals(RESOLVE_STRING, actual);
    }

    @Test
    void validateToken_Ok() {
        boolean actual = jwtTokenProvider.validateToken(CORRECT_TOKEN);
        Assertions.assertTrue(actual);
    }
}

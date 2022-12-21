package mate.academy.security;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String LOGIN = "user@mail.com";
    private static final List<String> ROLES = List.of("USER", "ADMIN");
    private static final String PASSWORD = "qwerty";
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQG1haWwuY29tIiwicm9"
            + "sZXMiOlsiVVNFUiIsIkFETUlOIl0sImlhdCI6MTYzNDM3NTM4MiwiZXhwIjoxNjM0M"
            + "zc4OTgyfQ.H9QvQ0o7SVAvCPuo2cM0eLSg3L24IKJar0meheYBARE";
    private static JwtTokenProvider jwtTokenProvider;
    private static UserDetailsService userDetailsService;

    @BeforeAll
    static void name() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000L);
        jwtTokenProvider.init();
    }

    @Test
    void createToken_Ok() {
        String actual = jwtTokenProvider.createToken(LOGIN, ROLES);
        Assertions.assertNotNull(actual);
        Assertions.assertNotEquals(TOKEN, actual);
    }

    @Test
    void getAuthentication_Ok() {
        User.UserBuilder builder = User.withUsername("USER");
        builder.password(PASSWORD);
        builder.roles(ROLES.get(0));
        UserDetails userDetails = builder.build();
        UsernamePasswordAuthenticationToken expected = new UsernamePasswordAuthenticationToken(
                userDetails, "",
                userDetails.getAuthorities());
        Mockito.when(userDetailsService.loadUserByUsername(Mockito.any())).thenReturn(userDetails);

        String token = jwtTokenProvider.createToken(LOGIN, ROLES);
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getUserName_Ok() {
        String token = jwtTokenProvider.createToken(LOGIN, ROLES);
        String actual = jwtTokenProvider.getUsername(token);
        Assertions.assertEquals(LOGIN, actual);
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + TOKEN);
        Assertions.assertEquals(TOKEN, jwtTokenProvider.resolveToken(request));
    }

    @Test
    void validateToken() {
        String token = jwtTokenProvider.createToken(LOGIN, ROLES);
        boolean actual = jwtTokenProvider.validateToken(token);
        Assertions.assertTrue(actual);
    }
}

package mate.academy.security.jwt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
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
    private static final String EMAIL = "bchupika@mate.academy";
    private static final String PASSWORD = "12345678";
    private static final String ROLE_USER = Role.RoleName.USER.name();

    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiY2h1cGlrYUBtYXR"
            + "lLmFjYWRlbXkiLCJyb2xlcyI6WyJVU0VSIl0sImlhdCI6MTY2NDc0NTE2NywiZXhwIjoxNjY1M"
            + "TA1MTY3fQ.pNkjYYvNDbLf1sSyPT3bPPnsCg9beOK8xkEQ4zAwUcg";
    private static final String FALSE_TOKEN = "XXXXXGciOiJIUzI1NiJ9.eyJzdWIiOiJiY2h1cGlrYUBtYX"
            + "YWRlbXkiLCJyb2xlcyI6WyJVU0VSIl0sImlhdCI6MTY2NDcyNDY0NCwiZXhwIjoxNjY0NzI4MjQ0fQ."
            + "ikQ2ItFB3qjEeHdY7ctLpKsnhP49vPFq9Zrm78";
    private static final int TOKEN_SIZE = 175;
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;
    private List<String> roles;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey","secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 360000000L);
        jwtTokenProvider.init();
        roles = List.of(ROLE_USER);
    }

    @Test
    void createToken() {
        String actual = jwtTokenProvider.createToken(EMAIL, roles);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(TOKEN_SIZE, actual.length());
    }

    @Test
    void getAuthentication() {
        UserDetails userDetails = User.withUsername(EMAIL)
                .password(PASSWORD)
                .roles(ROLE_USER)
                .build();

        Mockito.when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);
        Authentication expected =
                new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());

        Authentication actual = jwtTokenProvider.getAuthentication(TOKEN);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + TOKEN);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(TOKEN, actual);
    }

    @Test
    void validateToken_Ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(TOKEN));
    }

    @Test
    void validateToken_notOk() {
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(FALSE_TOKEN));
        Assertions.assertEquals("Expired or invalid JWT token", thrown.getMessage());
    }

    @Test
    void getUsername_Ok() {
        Assertions.assertEquals(EMAIL, jwtTokenProvider.getUsername(TOKEN));
    }
}

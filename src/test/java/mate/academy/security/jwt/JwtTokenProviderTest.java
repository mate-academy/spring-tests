package mate.academy.security.jwt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
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
    private static final String EMAIL = "johny@i.ua";
    private static final List LIST_ROLES = List.of(Role.RoleName.USER, Role.RoleName.ADMIN);
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;
    private String token;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000);
        token = jwtTokenProvider.createToken(EMAIL, LIST_ROLES);
    }

    @Test
    void createToken_Ok() {
        String actual = jwtTokenProvider.createToken(EMAIL, LIST_ROLES);
        Assertions.assertNotNull(actual,
                "Token for email:"
                        + EMAIL + "can't be empty ");
    }

    @Test
    void getAuthentication_Ok() {
        UserDetails userDetails = User.builder()
                .username(EMAIL)
                .password("1234")
                .roles("USER")
                .build();
        Mockito.when(this.userDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        Assertions.assertNotNull(actual.toString(),
                "Method should create Authentication object, but was: " + actual);
    }

    @Test
    void getUsername_Ok() {
        String actual = jwtTokenProvider.getUsername(token);
        Assertions.assertEquals(EMAIL, actual,
                "Method should return email: "
                        + EMAIL + " but was: " + actual);
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(httpServletRequest.getHeader("Authorization"))
                .thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(httpServletRequest);
        Assertions.assertEquals(token, actual,
                "Method should return token: "
                        + token + " , but was: " + actual);
    }

    @Test
    void validateToken_Ok() {
        boolean actual = jwtTokenProvider.validateToken(token);
        Assertions.assertTrue(actual, "Should return true for valid token");
    }
}

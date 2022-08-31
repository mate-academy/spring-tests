package mate.academy.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    private static final String LOGIN = "bob@gmail.com";
    private static final String PASSWORD = "1234";
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;
    private String token;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000L);
        jwtTokenProvider.init();
        token = jwtTokenProvider.createToken(LOGIN, List.of(Role.RoleName.USER.name()));
    }

    @Test
    void createToken_ok() {
        String actual = jwtTokenProvider.createToken(LOGIN, List.of(Role.RoleName.USER.name()));
        Assertions.assertNotNull(actual);
        int lengthPartsOfToken = actual.split("[.]").length;
        assertEquals(3, lengthPartsOfToken);
    }

    @Test
    void getAuthentication_ok() {
        UserDetails userDetails = User.withUsername(LOGIN).password(PASSWORD)
                .roles(Role.RoleName.USER.name()).build();
        Mockito.when(userDetailsService.loadUserByUsername(LOGIN)).thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        Assertions.assertNotNull(actual);
        User actualUser = (User) actual.getPrincipal();
        Assertions.assertEquals(LOGIN, actual.getName());
        Assertions.assertEquals(PASSWORD, actualUser.getPassword());
    }

    @Test
    void getUserName_ok() {
        String actual = jwtTokenProvider.getUsername(token);
        Assertions.assertEquals(LOGIN, actual);
    }

    @Test
    void resolveToken_ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertEquals(token, actual);
    }

    @Test
    void validateToken_ok() {
        boolean actual = jwtTokenProvider.validateToken(token);
        Assertions.assertTrue(actual);
    }
}

package mate.academy.security.jwt;

import static org.mockito.Mockito.mock;

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
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "12345";
    private static final String ROLE_USER = Role.RoleName.USER.name();
    private static final String TOKEN = "token";
    private static final String SECRET_KEY = "secret";
    private static final long VALIDITY_IN_MILLISECONDS = 3600000L;

    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        userDetailsService = mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                VALIDITY_IN_MILLISECONDS);
    }

    @Test
    void createToken_validData_ok() {
        String actual = jwtTokenProvider.createToken(EMAIL, List.of(ROLE_USER));
        Assertions.assertNotNull(actual);
    }

    @Test
    void getAuthentication_validData_ok() {
        User.UserBuilder builder = User.withUsername(EMAIL);
        builder.password(PASSWORD);
        builder.roles(ROLE_USER);
        UserDetails userDetails = builder.build();
        Mockito.when(userDetailsService.loadUserByUsername(EMAIL))
                .thenReturn(userDetails);
        Authentication actual
                = jwtTokenProvider.getAuthentication(
                        jwtTokenProvider.createToken(EMAIL, List.of(ROLE_USER)));
        Assertions.assertNotNull(actual);
        Authentication expected = new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getAuthentication_notValidData_notOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.getAuthentication(""));
    }

    @Test
    void getAuthentication_nullValidData_notOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.getAuthentication(null));
    }

    @Test
    void getUsername_validData_ok() {
        String expected = EMAIL;
        String actual
                = jwtTokenProvider.getUsername(
                        jwtTokenProvider.createToken(EMAIL, List.of(ROLE_USER)));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getUsername_notValidData_notOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.getUsername(""));
    }

    @Test
    void getUsername_nullData_notOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.getUsername(null));
    }

    @Test
    void resolveToken_validData_ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + jwtTokenProvider.createToken(EMAIL, List.of(ROLE_USER)));
        String actual = jwtTokenProvider.resolveToken(request);
        String expected = jwtTokenProvider.createToken(EMAIL, List.of(ROLE_USER));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void resolveToken_notValidData_notOk() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization"))
                .thenReturn("Not valid data"
                        + jwtTokenProvider.createToken(EMAIL, List.of(ROLE_USER)));
        Assertions.assertNull(jwtTokenProvider.resolveToken(request));
    }

    @Test
    void validateToken_validData_ok() {
        Assertions.assertTrue(
                jwtTokenProvider.validateToken(
                        jwtTokenProvider.createToken(EMAIL, List.of(ROLE_USER))));
    }

    @Test
    void validateToken_notValidData_notOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken("Not valid data"));
    }
}

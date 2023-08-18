package mate.academy.security;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

public class JwtTokenProviderTest {
    private static final String TEST_EMAIL = "test@gmail.com";
    private static final String TEST_PASSWORD = "test1234";
    private static final Long VALIDITY = 3600000L;
    private static final String SECRET_KEY = "secretKey";
    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";
    private static final String USER_ROLE = "USER";
    private static UserDetailsService userDetailsService;
    private static JwtTokenProvider jwtTokenProvider;
    private static String token;
    private static HttpServletRequest request;
    private UserDetails userDetails;
    private Authentication authentication;

    @BeforeEach
    void setUp() {

        request = Mockito.mock(HttpServletRequest.class);
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, SECRET_KEY, SECRET_KEY);
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", VALIDITY);
        List<String> roles = List.of(USER_ROLE);
        token = jwtTokenProvider.createToken(TEST_EMAIL, roles);
    }

    @Test
    void createToken_TokenCreated_Ok() {
        Assertions.assertNotNull(token);
    }

    @Test
    void getAuthentication_AuthenticationWithValidData_Ok() {
        userDetails = User.withUsername(TEST_EMAIL)
                .password(TEST_PASSWORD).roles(USER_ROLE).build();
        Mockito.when(userDetailsService.loadUserByUsername(TEST_EMAIL)).thenReturn(userDetails);
        authentication = jwtTokenProvider.getAuthentication(token);
        UserDetails actualDetails = (UserDetails) authentication.getPrincipal();
        Assertions.assertEquals(actualDetails.getUsername(),userDetails.getUsername(),
                "The username doesn't match the expected data");
        Assertions.assertEquals(actualDetails.getPassword(),userDetails.getPassword(),
                "The password doesn't match the expected data");
        Assertions.assertEquals(actualDetails.getAuthorities(),userDetails.getAuthorities(),
                "The authorization doesn't match the expected data");
    }

    @Test
    void getUsername_GetValidUsername_Ok() {
        Assertions.assertEquals(jwtTokenProvider.getUsername(token),TEST_EMAIL,
                "The username doesn't match the expected data");
    }

    @Test
    void getUsername_GetEmptyUsername_NotOk() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                        jwtTokenProvider.getUsername(""),
                "IllegalArgumentException expected");
    }

    @Test
    void getUsername_GetNullUsername_NotOk() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                        jwtTokenProvider.getUsername(null),
                "IllegalArgumentException expected");
    }

    @Test
    void resolveToken_TokenResolved_Ok() {
        Mockito.when(request.getHeader(HEADER)).thenReturn(PREFIX + token);
        String actualToken = jwtTokenProvider.resolveToken(request);
        Assertions.assertEquals(actualToken,token,
                "The actual token doesn't match the expected token");
        Assertions.assertTrue(jwtTokenProvider.validateToken(token),
                "The actual token didn't validate");
        Assertions.assertEquals(TEST_EMAIL,jwtTokenProvider.getUsername(actualToken),
                "The actual email doesn't match the expected data");
    }

    @Test
    void resolveToken_ResolveTokenWithNullPrefix_NotOk() {
        Mockito.when(request.getHeader(HEADER)).thenReturn(null);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertEquals(actual,null);
    }

    @Test
    void resolveToken_ResolveTokenWithNullHeader_NotOk() {
        Mockito.when(request.getHeader(null)).thenReturn(PREFIX);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertEquals(actual,null);
    }

    @Test
    void validateToken_TokenValidated_Ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_ValidateEmptyToken_NotOk() {
        Assertions.assertThrows(RuntimeException.class,() ->
                        jwtTokenProvider.validateToken(""),
                "RuntimeException expected");
    }

    @Test
    void validateToken_ValidateWrongToken_NotOk() {
        Assertions.assertThrows(RuntimeException.class,() ->
                        jwtTokenProvider.validateToken("wrong"),
                "RuntimeException expected");
    }

    @Test
    void validateToken_ValidateNullToken_NotOk() {
        Assertions.assertThrows(RuntimeException.class,() ->
                        jwtTokenProvider.validateToken(null),
                "RuntimeException expected");
    }
}

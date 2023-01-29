package mate.academy.security.jwt;

import static org.springframework.security.core.userdetails.User.UserBuilder;
import static org.springframework.security.core.userdetails.User.withUsername;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.security.CustomUserDetailsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String EXPIRED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGVtYWlsLmNv"
            + "bSIsInJvbGVzIjpbIlVTRVIiXSwiaWF0IjoxNjc0ODU5MzIzLCJleHAiOjE2NzQ4NjI5MjN9.1ppgErGXbj"
            + "xzhoc9KkREaDuIdLMkgQsrwXh5meL-X8w";
    private static final String INVALID_TOKEN = "fjslkdfajlskdfjlaskdf/asdfkasdfasdfjklasjldkfjasl";
    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIi"
            + "OiJ1c2VyQGVtYWlsLmNvbSIsInJvbGVzIjpbIlVTRVIiXSwiaWF0IjoxNjc0ODU5MzIzLCJleHAiOjE2NzQ"
            + "4NjI5MjN9.1ppgErGXbjxzhoc9KkREaDuIdLMkgQsrwXh5meL-X8w";
    private static final int INDEX_TOKEN_START_POSITION = 7;
    private static JwtTokenProvider jwtTokenProvider;
    private static UserDetailsService userDetailsService;
    private static HttpServletRequest httpServletRequest;
    private final String login = "user@email.com";
    private final List<String> roles = List.of("USER");

    @BeforeAll
    static void beforeAll() {
        httpServletRequest = Mockito.mock(HttpServletRequest.class);
        userDetailsService = Mockito.mock(CustomUserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider,"secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider,"validityInMilliseconds", 3600000);
    }

    @Test
    void createToken_Ok() {
        String token = jwtTokenProvider.createToken(login, roles);
        System.out.println(token);
        Assertions.assertNotNull(token);
    }

    @Test
    void getAuthentication_Ok() {
        String token = jwtTokenProvider.createToken(login, roles);
        UserBuilder builder = withUsername(login)
                .password("password")
                .roles(roles.toArray(String[]::new));
        UserDetails userDetails = builder.build();
        Mockito.when(userDetailsService.loadUserByUsername(login))
                .thenReturn(userDetails);
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        Assertions.assertNotNull(authentication);
        Assertions.assertEquals(login, authentication.getName());
    }

    @Test
    void getUsername_Ok() {
        String token = jwtTokenProvider.createToken(login, roles);
        String username = jwtTokenProvider.getUsername(token);
        Assertions.assertNotNull(username);
        Assertions.assertEquals(login, username);
    }

    @Test
    void resolveToken_correctToken_Ok() {
        Mockito.when(httpServletRequest.getHeader(AUTHORIZATION_HEADER_NAME))
                        .thenReturn(AUTHORIZATION_HEADER_VALUE);
        String resolvedToken = jwtTokenProvider.resolveToken(httpServletRequest);
        Assertions.assertNotNull(resolvedToken);
        Assertions.assertEquals(AUTHORIZATION_HEADER_VALUE.substring(INDEX_TOKEN_START_POSITION),
                resolvedToken);
    }

    @Test
    void resolveToken_nullToken_ReturnNullNotOk() {
        Mockito.when(httpServletRequest.getHeader(AUTHORIZATION_HEADER_NAME))
                        .thenReturn(null);
        Assertions.assertNull(jwtTokenProvider.resolveToken(httpServletRequest));
    }

    @Test
    void validateToken_validToken_Ok() {
        String xtoken = jwtTokenProvider.createToken(login, roles);
        Assertions.assertTrue(jwtTokenProvider.validateToken(xtoken));
    }

    @Test
    void validateToken_expiredToken_NotOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(EXPIRED_TOKEN));

    }

    @Test
    void validateToken_invalidToken_runtimeException_NotOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(INVALID_TOKEN));
    }
}

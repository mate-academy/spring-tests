package mate.academy.security.jwt;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import mate.academy.security.CustomUserDetailsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;

public class JwtTokenProviderTest {
    private static final String SECRET_KEY = "secret";
    private static final long VALIDITY_IN_MILLISECONDS = 3600000L;
    private static final String HEADER_NAME = "Authorization";
    private static final String JWT_PREFIX = "Bearer ";
    private UserDetailsService userDetailsService = Mockito.mock(CustomUserDetailsService.class);
    private JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(userDetailsService);

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        initJwtTokenProvider();
    }

    @Test
    void createToken_Ok() {
        String login = "bob@i.ua";
        List<String> roles = List.of("USER", "ADMIN");
        String actualToken = jwtTokenProvider.createToken(login, roles);
        Assertions.assertNotNull(actualToken);
        Assertions.assertNotEquals(0, actualToken.length());
    }

    @Test
    void resolveToken_Ok() {
        String mockJwt = "Any String";
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(httpServletRequest.getHeader(HEADER_NAME)).thenReturn(JWT_PREFIX + mockJwt);
        String actual = jwtTokenProvider.resolveToken(httpServletRequest);
        assertNotNull(actual);
        assertEquals(mockJwt, actual);
    }

    @Test
    void resolveToken_notOk() {
        String mockJwt = "Any String";
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(httpServletRequest.getHeader(HEADER_NAME)).thenReturn(null);
        String actual = jwtTokenProvider.resolveToken(httpServletRequest);
        assertNull(actual, "Header '" + HEADER_NAME + "' isn't exists");
        Mockito.when(httpServletRequest.getHeader(HEADER_NAME)).thenReturn(mockJwt);
        actual = jwtTokenProvider.resolveToken(httpServletRequest);
        assertNull(actual, "Header '" + HEADER_NAME + "' isn't starts with '" + JWT_PREFIX + "'");
    }

    @Test
    void validateToken_Ok() {
        String login = "bob@i.ua";
        List<String> roles = List.of("USER", "ADMIN");
        String jwtToken = jwtTokenProvider.createToken(login, roles);
        assertTrue(jwtTokenProvider.validateToken(jwtToken));
    }

    @Test
    void validateToken_notOk() throws NoSuchFieldException, IllegalAccessException {
        String login = "bob@i.ua";
        List<String> roles = List.of("USER", "ADMIN");
        String invalidToken = "Any string";
        initValidityInMilliseconds(1);
        String expiredToken = jwtTokenProvider.createToken(login, roles);
        assertThrows(RuntimeException.class, () -> jwtTokenProvider.validateToken(invalidToken),
                "Invalid token");
        assertThrows(RuntimeException.class, () -> jwtTokenProvider.validateToken(expiredToken),
                "Expired token");
    }

    @Test
    void getUserName_Ok() {
        String login = "bob@i.ua";
        List<String> roles = List.of("USER", "ADMIN");
        String token = jwtTokenProvider.createToken(login, roles);
        String actual = jwtTokenProvider.getUsername(token);
        assertNotNull(actual);
        assertEquals(login, actual);
        login = "alice@i.ua";
        token = jwtTokenProvider.createToken(login, roles);
        actual = jwtTokenProvider.getUsername(token);
        assertNotNull(actual);
        assertEquals(login, actual);
    }

    @Test
    void getAuthentication_Ok() {
        String login = "bob@i.ua";
        List<String> roles = List.of("USER", "ADMIN");
        Collection<? extends GrantedAuthority> expectedAuthorities =
                convertStringsToAuthorities(roles);
        String token = jwtTokenProvider.createToken(login, roles);
        UserDetails userDetails = mockUserDetails(login, expectedAuthorities);
        Mockito.when(userDetailsService.loadUserByUsername(login))
                .thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        assertNotNull(actual);
        assertEquals(login, actual.getName());
        assertEquals(roles.size(), actual.getAuthorities().size());
        assertTrue(actual.getAuthorities().containsAll(expectedAuthorities));
    }

    private Collection<? extends GrantedAuthority> convertStringsToAuthorities(List<String> roles)
    {
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    private UserDetails mockUserDetails(String login,
                                        Collection<? extends GrantedAuthority> authorities) {
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getUsername()).thenReturn(login);
        Mockito.doReturn(authorities).when(userDetails).getAuthorities();
        return userDetails;
    }

    private void initJwtTokenProvider() throws NoSuchFieldException, IllegalAccessException {
        initSecretKey(SECRET_KEY);
        initValidityInMilliseconds(VALIDITY_IN_MILLISECONDS);
    }

    private void initSecretKey(String value) throws NoSuchFieldException, IllegalAccessException {
        Field secretKeyField = jwtTokenProvider.getClass().getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        value = Base64.getEncoder().encodeToString(value.getBytes());
        secretKeyField.set(jwtTokenProvider, value);
    }

    private void initValidityInMilliseconds(long value) throws NoSuchFieldException,
            IllegalAccessException {
        Field field = jwtTokenProvider.getClass().getDeclaredField("validityInMilliseconds");
        field.setAccessible(true);
        field.set(jwtTokenProvider, value);
    }
}

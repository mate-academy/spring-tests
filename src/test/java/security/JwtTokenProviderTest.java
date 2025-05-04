package security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;
import mate.academy.model.Role;
import mate.academy.security.CustomUserDetailsService;
import mate.academy.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static JwtTokenProvider jwtTokenProvider;
    private static UserDetailsService userDetailsService;
    private static final String LOGIN = "alice@mail.com";

    @BeforeAll
    static void beforeAll() {
        userDetailsService = Mockito.mock(CustomUserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                3600000L);
    }

    @Test
    void createToken_Ok() {
        String actual = jwtTokenProvider.createToken(LOGIN, new ArrayList<>());
        assertNotNull(actual, "Token was not created");
    }

    @Test
    void resolveToken_Ok() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer jwtToken");
        String actual = jwtTokenProvider.resolveToken(request);
        assertFalse(actual.contains("Bearer "),
                "Returned JWT must not contains Bearer prefix");
    }

    @Test
    void validateToken_Ok() {
        String token = jwtTokenProvider.createToken(LOGIN, new ArrayList<>());
        boolean actual = jwtTokenProvider.validateToken(token);
        assertTrue(actual, "Token is expired");
    }

    @Test
    void validateToken_incorrectToken_NotOk() {
        assertThrows(RuntimeException.class, () ->
                jwtTokenProvider.validateToken("incorrect-token"),
                "Method should throw RuntimeException if the token is incorrect");
    }

    @Test
    void getUsername_Ok() {
        String token = jwtTokenProvider.createToken(LOGIN, new ArrayList<>());
        String actual = jwtTokenProvider.getUsername(token);
        assertEquals(LOGIN, actual);
    }

    @Test
    void getAuthentication_Ok() {
        String token = jwtTokenProvider.createToken(LOGIN, new ArrayList<>());
        Role role = new Role(Role.RoleName.USER);
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(role.getRoleName().name()));
        UserDetails userDetails = new User(LOGIN, "password", authorities);
        Mockito.when(userDetailsService.loadUserByUsername(any())).thenReturn(userDetails);
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        assertEquals(LOGIN, authentication.getName());
        assertEquals(authorities, authentication.getAuthorities());
    }
}

package mate.academy.security.jwt;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

public class JwtTokenProviderTest {
    private static final String SECRET_KEY = "secret";
    private static final String EMAIL = "modernboy349@gmail.com";
    private static final String PASSWORD = "Hello123";
    private static final Long VALIDITY_IN_MILLISECONDS = 3600000L;
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtTokenProvider,"validityInMilliseconds",
                VALIDITY_IN_MILLISECONDS);
    }

    @Test
    void createToken_Ok() {
        String actual = jwtTokenProvider.createToken(EMAIL, new ArrayList<>());
        Assertions.assertNotNull(actual);
    }

    @Test
    void getAuthentication_Ok() {
        String token = jwtTokenProvider.createToken(EMAIL, new ArrayList<>());
        Role role = new Role(Role.RoleName.USER);
        List<GrantedAuthority> grantedAuthorityList = List.of(
                new SimpleGrantedAuthority(role.getRoleName().name()));
        User user = new User(EMAIL, PASSWORD, grantedAuthorityList);
        UserDetails userDetails = new User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isAccountNonLocked(),
                user.getAuthorities());
        Mockito.when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(grantedAuthorityList, actual.getAuthorities());
        Assertions.assertEquals(userDetails.getUsername(), actual.getName());
    }

    @Test
    void getUsername_Ok() {
        String token = jwtTokenProvider.createToken(EMAIL, new ArrayList<>());
        String username = jwtTokenProvider.getUsername(token);
        Assertions.assertNotNull(token);
        Assertions.assertEquals(username, EMAIL);
    }

    @Test
    void resolveToken_Ok() {
        String token = jwtTokenProvider.createToken(EMAIL, new ArrayList<>());
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(httpServletRequest.getHeader("Authorization"))
                .thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(httpServletRequest);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(token, actual);
    }

    @Test
    void validateToken_Ok() {
        String token = jwtTokenProvider.createToken(EMAIL, new ArrayList<>());
        Assertions.assertNotNull(token);
    }

    @Test
    void validateToken_expiredToken_notOk() {
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds",
                -VALIDITY_IN_MILLISECONDS);
        String token = jwtTokenProvider.createToken(EMAIL, new ArrayList<>());
        Throwable exception = Assertions.assertThrows(RuntimeException.class, () -> {
            jwtTokenProvider.validateToken(token);
        }, "RuntimeException was expected");
        Assertions.assertEquals("Expired or invalid JWT token",
                exception.getLocalizedMessage());
    }
}

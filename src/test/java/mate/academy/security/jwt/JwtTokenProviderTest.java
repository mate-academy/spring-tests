package mate.academy.security.jwt;

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

class JwtTokenProviderTest {
    private static final String USER_LOGIN = "bchupika@mate.academy";
    private static final String USER_PASSWORD = "12345678";
    private static final String SECRET_KEY_VALUE = "secret";
    private static final String SECRET_KEY_FIELD_NAME = "secretKey";
    private static final String VALIDITY_VALUE_FIELD_NAME = "validityInMilliseconds";
    private static final Long VALIDITY_VALUE_IN_MSEC = 3600000L;
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = Mockito.spy(new JwtTokenProvider(userDetailsService));
        ReflectionTestUtils.setField(jwtTokenProvider,
                SECRET_KEY_FIELD_NAME, SECRET_KEY_VALUE);
        ReflectionTestUtils.setField(jwtTokenProvider,
                VALIDITY_VALUE_FIELD_NAME, VALIDITY_VALUE_IN_MSEC);
    }

    @Test
    void createToken_ValidLoginAndRoles_Ok() {
        Assertions.assertNotNull(jwtTokenProvider.createToken(USER_LOGIN,
                List.of(Role.RoleName.USER.name())));
    }

    @Test
    void getAuthentication_ValidToken_Ok() {
        String token = jwtTokenProvider.createToken(USER_LOGIN,
                List.of(Role.RoleName.USER.name()));
        List<GrantedAuthority> grantedAuthorityList = List.of(new SimpleGrantedAuthority(
                Role.RoleName.USER.name()));
        User user = new User(USER_LOGIN, USER_PASSWORD, grantedAuthorityList);
        UserDetails userDetails = new User(user.getUsername(), user.getPassword(),
                user.isAccountNonExpired(), user.isAccountNonLocked(),
                user.isCredentialsNonExpired(), user.isEnabled(),
                user.getAuthorities());
        Mockito.when(userDetailsService.loadUserByUsername(USER_LOGIN)).thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(grantedAuthorityList, actual.getAuthorities());
        Assertions.assertEquals(userDetails.getUsername(), actual.getName());
    }

    @Test
    void getUsername_ValidToken_Ok() {
        String token = jwtTokenProvider.createToken(USER_LOGIN,
                List.of(Role.RoleName.USER.name()));
        String actual = jwtTokenProvider.getUsername(token);
        Assertions.assertEquals(USER_LOGIN, actual);
    }

    @Test
    void resolveToken_ValidToken_Ok() {
        String token = jwtTokenProvider.createToken(USER_LOGIN,
                List.of(Role.RoleName.USER.name()));
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(token, actual);
    }

    @Test
    void validateToken_ValidToken_Ok() {
        String token = jwtTokenProvider.createToken(USER_LOGIN,
                List.of(Role.RoleName.USER.name()));
        boolean actual = jwtTokenProvider.validateToken(token);
        Assertions.assertTrue(actual);
    }

    @Test
    void validateToken_ExpiredToken_NotOk() {
        ReflectionTestUtils.setField(jwtTokenProvider,
                VALIDITY_VALUE_FIELD_NAME, 5);
        String token = jwtTokenProvider.createToken(USER_LOGIN,
                List.of(Role.RoleName.USER.name()));
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(token));
        Assertions.assertEquals("Expired or invalid JWT token", exception.getMessage());
    }
}

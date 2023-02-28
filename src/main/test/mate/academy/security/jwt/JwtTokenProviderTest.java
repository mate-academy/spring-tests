package mate.academy.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {
    private static final String SECRET_FIELD_NAME = "secretKey";
    private static final String SECRET_FIELD_VALUE = "secret";
    private static final String VALIDITY_TIME_FIELD_NAME = "validityInMilliseconds";
    private static final long VALIDITY_TIME_FIELD_VALUE = 3600000;
    @Mock
    private UserDetailsService userDetailsService;
    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtTokenProvider, SECRET_FIELD_NAME, SECRET_FIELD_VALUE);
        ReflectionTestUtils.setField(jwtTokenProvider, VALIDITY_TIME_FIELD_NAME,
                VALIDITY_TIME_FIELD_VALUE);
        jwtTokenProvider.init();
    }

    @Test
    void createToken_validLoginAndRoles_Ok() {
        String login = "Smith";
        List<String> roles = new ArrayList<>();
        roles.add(Role.RoleName.USER.name());

        String token = jwtTokenProvider.createToken(login, roles);

        assertNotNull(token);
        assertTrue(token.matches(".+\\..+\\..+"));
    }

    @Test
    void getAuthentication_validToken_Ok() {
        String login = "Smith";
        String password = "Password";
        List<String> roles = new ArrayList<>();
        roles.add(Role.RoleName.USER.name());
        UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(login);
        builder.password(password);
        builder.roles(roles.toArray(String[]::new));
        String token = jwtTokenProvider.createToken(login, roles);
        when(userDetailsService.loadUserByUsername(login)).thenReturn(builder.build());

        Authentication authentication = jwtTokenProvider.getAuthentication(token);

        assertNotNull(authentication);
        assertEquals(login, authentication.getName());
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            assertEquals("ROLE_" + roles.get(0), authority.getAuthority());
        }
    }

    @Test
    void getUsername_validToken_Ok() {
        String expectedLogin = "Smith";
        String password = "Password";
        List<String> roles = new ArrayList<>();
        roles.add(Role.RoleName.USER.name());
        String token = jwtTokenProvider.createToken(expectedLogin, roles);

        String actualLogin = jwtTokenProvider.getUsername(token);

        assertNotNull(actualLogin);
        assertEquals(expectedLogin, actualLogin);
    }

    @Test
    void validateToken_validToken_Ok() {
        String expectedLogin = "Smith";
        List<String> roles = new ArrayList<>();
        roles.add(Role.RoleName.USER.name());
        String token = jwtTokenProvider.createToken(expectedLogin, roles);

        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_expiredToken_Exception() {
        String expectedLogin = "Smith";
        List<String> roles = new ArrayList<>();
        roles.add(Role.RoleName.USER.name());
        ReflectionTestUtils.setField(jwtTokenProvider, VALIDITY_TIME_FIELD_NAME, 0);
        String token = jwtTokenProvider.createToken(expectedLogin, roles);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(token));

        assertEquals("Expired or invalid JWT token", exception.getMessage());
    }
}

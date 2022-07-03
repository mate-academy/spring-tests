package mate.academy.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIi"
            + "OiJhZG1pbkBtYWlsLm5ldCIsInJvbGVzIjpbIlVTRVIiXSwiaWF0IjoxN"
            + "jU2NjE1MTUxLCJleHAiOjE2NTY5NzUxNTF9.5Y7akQEvQrdaZJGZyAnkm"
            + "BTeARTmRD3GDxi8HK6DofA";
    private mate.academy.model.User admin;
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000L);
        jwtTokenProvider.init();
        admin = new mate.academy.model.User();
        admin.setEmail("admin@mail.net");
        admin.setPassword("super1234");
        admin.setRoles(Set.of((new Role(Role.RoleName.ADMIN))));

    }

    @Test
    void createToken_ok() {
        List<String> roles = admin.getRoles().stream()
                .map(r -> r.getRoleName().name())
                .collect(Collectors.toList());
        String actual = jwtTokenProvider.createToken(admin.getEmail(), roles);
        assertNotNull(actual);
    }

    @Test
    void getUsername_ok() {
        String actual = jwtTokenProvider.getUsername(TOKEN);
        assertNotNull(actual);
        assertEquals(admin.getEmail(), actual);
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(httpServletRequest.getHeader("Authorization"))
                .thenReturn("Bearer tokien_body");
        String actual = jwtTokenProvider.resolveToken(httpServletRequest);
        assertNotNull(actual);
        assertEquals("tokien_body", actual);
    }

    @Test
    void resolveToken_Not_Ok() {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer ");
        String actual = jwtTokenProvider.resolveToken(httpServletRequest);
        assertNotNull(actual);
        assertEquals("", actual);
    }

    @Test
    void validateToken_Ok() {
        List<String> roles = admin.getRoles().stream()
                .map(r -> r.getRoleName().name())
                .collect(Collectors.toList());
        String actual = jwtTokenProvider.createToken(admin.getEmail(), roles);
        assertNotNull(actual);
        assertTrue(jwtTokenProvider.validateToken(actual));
    }

    @Test
    void getAuthentication_Ok() {
        User.UserBuilder builder = User.withUsername(admin.getEmail());
        UserDetails userDetails = builder.password(admin.getPassword()).roles("ADMIN").build();
        Mockito.when(userDetailsService.loadUserByUsername(Mockito.anyString()))
                .thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(TOKEN);
        User actualUser = (User) actual.getPrincipal();
        assertNotNull(actual);
        assertEquals(actualUser.getUsername(), admin.getEmail());
        assertEquals(actualUser.getPassword(), admin.getPassword());
    }
}

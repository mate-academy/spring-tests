package mate.academy.security.jwt;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0VXNlckBtYWl"
            + "sLmNvbSIsInJvbGVzIjpbIlVTRVIiXSwiaWF0IjoxNjUzNjg5NzIzLCJleHAiOjE2NTM2OTMzMjN9"
            + ".yiS0t3o2tp2ik8Dbip4BkN2W2iL-jhvWoXCprKKZdMY";
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;
    private mate.academy.model.User testUser;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000L);
        jwtTokenProvider.init();

        testUser = new mate.academy.model.User();
        testUser.setEmail("testUser@mail.com");
        testUser.setPassword("testUser_1234");
        testUser.setRoles(Set.of((new Role(Role.RoleName.USER))));
    }

    @Test
    void createToken_Ok() {
        List<String> roles = testUser.getRoles().stream()
                .map(r -> r.getRoleName().name())
                .collect(Collectors.toList());
        String actual = jwtTokenProvider.createToken(testUser.getEmail(), roles);
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.split("\\.").length == 3);
        Assertions.assertNotEquals("", actual);
    }

    @Test
    void getAuthentication_ok() {
        UserBuilder builder = User.withUsername(testUser.getEmail());
        builder.password(testUser.getPassword());
        builder.roles(testUser.getRoles().stream()
                .map(r -> r.getRoleName().name())
                .toArray(String[]::new));
        UserDetails userDetails = builder.build();
        Mockito.when(userDetailsService.loadUserByUsername(Mockito.anyString()))
                .thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(TOKEN);
        User actualUser = (User) actual.getPrincipal();
        Assertions.assertNotNull(actual);
        Assertions.assertNotEquals(actualUser.getUsername(), "");
        Assertions.assertNotEquals(actualUser.getPassword(), "");
        Assertions.assertNotEquals(actualUser.getAuthorities(), Set.of());
        Assertions.assertEquals(actualUser.getUsername(), testUser.getEmail());
        Assertions.assertEquals(actualUser.getPassword(), testUser.getPassword());
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest mock = Mockito.mock(HttpServletRequest.class);
        Mockito.when(mock.getHeader("Authorization")).thenReturn("Bearer " + TOKEN);
        String actual = jwtTokenProvider.resolveToken(mock);
        Assertions.assertNotNull(actual);
        Assertions.assertNotEquals(actual, "");
        Assertions.assertEquals(3, actual.split("\\.").length);
        Assertions.assertEquals(actual, TOKEN);
    }

    @Test
    void resolveToken_notOk() {
        HttpServletRequest mock = Mockito.mock(HttpServletRequest.class);
        Mockito.when(mock.getHeader("Authorization")).thenReturn("" + TOKEN);
        String actual = jwtTokenProvider.resolveToken(mock);
        Assertions.assertNull(actual);
    }

    @Test
    void validateToken_Ok() {
        boolean actual = jwtTokenProvider.validateToken(TOKEN);
        Assertions.assertTrue(actual);
    }

    @Test
    void validateToken_NotOk() {
        String notValid = "notValidToken";
        assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(notValid));
    }
}

package mate.academy.security.jwt;

import java.lang.reflect.Field;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.security.CustomUserDetailsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

class JwtTokenProviderTest {
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private UserDetails userDetails;
    private List<String> roles;
    private String login;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(CustomUserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        try {
            Field secretKeyField = jwtTokenProvider.getClass().getDeclaredField("secretKey");
            secretKeyField.setAccessible(true);
            secretKeyField.set(jwtTokenProvider, "secret");
            Field validityField = jwtTokenProvider.getClass()
                    .getDeclaredField("validityInMilliseconds");
            validityField.setAccessible(true);
            validityField.set(jwtTokenProvider, 3600000L);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        jwtTokenProvider.init();
        User.UserBuilder builderBob = org.springframework.security.core.userdetails
                .User.withUsername("bob");
        builderBob.password("1234");
        builderBob.authorities("ADMIN","USER");
        userDetails = builderBob.build();
        login = userDetails.getUsername();
        roles = userDetails.getAuthorities().stream()
                .map(Object::toString)
                .toList();
    }

    @Test
    void creatToken_Ok() {
        String actual = jwtTokenProvider.createToken(login, roles);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(3, actual.split("\\.").length);
    }

    @Test
    void getAuthentication_Ok() {
        JwtTokenProvider spyJwtTokenProvider = Mockito.spy(jwtTokenProvider);
        Mockito.doReturn(userDetails.getUsername()).when(spyJwtTokenProvider)
                .getUsername("<token>");
        Mockito.when(userDetailsService.loadUserByUsername(userDetails.getUsername()))
                .thenReturn(userDetails);
        Authentication actual = spyJwtTokenProvider.getAuthentication("<token>");
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(userDetails, actual.getPrincipal());
    }

    @Test
    void getUsername_Ok() {
        String token = jwtTokenProvider.createToken(login, roles);
        String actual = jwtTokenProvider.getUsername(token);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(login, actual);
    }

    @Test
    void resolveToken_Ok() {
        String bearerToken = "Bearer <token>";
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn(bearerToken);
        String actual = jwtTokenProvider.resolveToken(req);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals("<token>", actual);
    }

    @Test
    void resolveToken_IsNull_NotOk() {
        String bearerToken = "<token>";
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getHeader("Authorization")).thenReturn(bearerToken);
        String actual = jwtTokenProvider.resolveToken(req);
        Assertions.assertNull(actual);
    }

    @Test
    void validateToken_Ok() {
        String token = jwtTokenProvider.createToken(login, roles);
        boolean actual = jwtTokenProvider.validateToken(token);
        Assertions.assertTrue(actual);
    }

    @Test
    void validateToken_WrongSecretKey_NotOk() {
        String token = jwtTokenProvider.createToken(login, roles);
        try {
            Field secretKeyField = jwtTokenProvider.getClass().getDeclaredField("secretKey");
            secretKeyField.setAccessible(true);
            secretKeyField.set(jwtTokenProvider, "key");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertThrows(RuntimeException.class, () -> {
            jwtTokenProvider.validateToken(token);
        });
    }

    @Test
    void validateToken_TokenNoMoreValid_NotOk() {
        try {
            Field validityField = jwtTokenProvider.getClass()
                    .getDeclaredField("validityInMilliseconds");
            validityField.setAccessible(true);
            validityField.set(jwtTokenProvider, 1L);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        String token = jwtTokenProvider.createToken(login, roles);
        Assertions.assertThrows(RuntimeException.class, () -> {
            jwtTokenProvider.validateToken(token);
        });
    }
}

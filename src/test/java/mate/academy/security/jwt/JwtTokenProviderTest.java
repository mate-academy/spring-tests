package mate.academy.security.jwt;

import java.lang.reflect.Field;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
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
    private static final String VALID_USERNAME = "bob@i.ua";
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private String token;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(CustomUserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        try {
            Field secretKey = jwtTokenProvider.getClass().getDeclaredField("secretKey");
            secretKey.setAccessible(true);
            secretKey.set(jwtTokenProvider, "secret");
            Field validityInMilliseconds =
                    jwtTokenProvider.getClass().getDeclaredField("validityInMilliseconds");
            validityInMilliseconds.setAccessible(true);
            validityInMilliseconds.set(jwtTokenProvider, 3600000L);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Can't find required fields", e);
        }
        token = jwtTokenProvider.createToken(VALID_USERNAME, List.of(Role.RoleName.USER.name()));
    }

    @Test
    void createToken_Ok() {
        String actual = jwtTokenProvider.createToken(
                  VALID_USERNAME, List.of(Role.RoleName.USER.name()));

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(token, actual);
    }

    @Test
    void getAuthentication_Ok() {
        UserDetails userDetails = User.withUsername(VALID_USERNAME)
                .password("123456")
                .roles(Role.RoleName.USER.name())
                .build();

        JwtTokenProvider spy = Mockito.spy(jwtTokenProvider);
        Mockito.doReturn(userDetails.getUsername())
              .when(spy).getUsername(token);

        Mockito.when(userDetailsService
                .loadUserByUsername(VALID_USERNAME))
                .thenReturn(userDetails);

        Authentication actual = spy.getAuthentication(token);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(userDetails, actual.getPrincipal());
    }

    @Test
    void getUsername_Ok() {
        String actual = jwtTokenProvider.getUsername(token);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(VALID_USERNAME, actual);
    }

    @Test
    void resolveToken_Ok() {
        HttpServletRequest servletRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(servletRequest.getHeader("Authorization"))
                .thenReturn("Bearer " + token);

        String actual = jwtTokenProvider.resolveToken(servletRequest);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(token, actual);
    }

    @Test
    void validateToken_Ok() {
        boolean actual = jwtTokenProvider.validateToken(token);

        Assertions.assertTrue(actual);
    }

    @Test
    void validateToken_wrongKey_notOk() {
        try {
            Field validityInMilliseconds = jwtTokenProvider.getClass()
                    .getDeclaredField("secretKey");
            validityInMilliseconds.setAccessible(true);
            validityInMilliseconds.set(jwtTokenProvider, "asd");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        Assertions.assertThrows(RuntimeException.class, () -> {
            jwtTokenProvider.validateToken(token);
        });
    }
}

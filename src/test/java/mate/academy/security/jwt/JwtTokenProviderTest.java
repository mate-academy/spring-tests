package mate.academy.security.jwt;

import static org.mockito.ArgumentMatchers.any;

import java.lang.reflect.Field;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

class JwtTokenProviderTest {
    private static final String SECRET_KEY = "turtle";
    private static final Long DELAY = 3600000L;
    private static final String EMAIL = "email@test.test";
    private static UserDetailsService userDetailsService;
    private static JwtTokenProvider jwtTokenProvider;
    private String token;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        Field fieldSK = JwtTokenProvider.class.getDeclaredField("secretKey");
        fieldSK.setAccessible(true);
        fieldSK.set(jwtTokenProvider, SECRET_KEY);
        Field fieldViM = JwtTokenProvider.class.getDeclaredField("validityInMilliseconds");
        fieldViM.setAccessible(true);
        fieldViM.set(jwtTokenProvider, DELAY);
        token = jwtTokenProvider.createToken(EMAIL, List.of(Role.RoleName.USER.name()));
    }

    @Test
    void createToken_ok() {
        Assertions.assertNotNull(token);
    }

    @Test
    void getAuthentication_ok() {
        Mockito.when(userDetailsService.loadUserByUsername(any()))
                .thenReturn(Mockito.mock(UserDetails.class));
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        Assertions.assertNotNull(actual.getPrincipal());
    }

    @Test
    void getAuthentication_notOk() {
        Assertions.assertThrows(Exception.class,
                () -> jwtTokenProvider.getAuthentication("parrot"));
    }

    @Test
    void getUsername_ok() {
        Assertions.assertEquals(EMAIL, jwtTokenProvider.getUsername(token));
    }

    @Test
    void resolveToken_ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization"))
                .thenReturn("token");
        Assertions.assertNotEquals(token, jwtTokenProvider.resolveToken(request));
    }

    @Test
    void validateToken_ok() {
        Assertions.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_tokenIsNull_notOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(null));
    }

    @Test
    void validateToken_tokenIsIncorrect_notOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken("incorrect_token"));
    }
}

package mate.academy.security.jwt;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import static org.mockito.ArgumentMatchers.any;

public class JwtTokenProviderTest {
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;
    private final String SECRET_KEY = "secret";
    private final long MILLISECONDS = 3600000L;
    private final String EMAIL = "bobik@g.com";
    private final String PASSWORD = "1234567890";
    private List<String> roleList = new ArrayList<>();

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
        fieldViM.setLong(jwtTokenProvider, MILLISECONDS);
        roleList.add(Role.RoleName.USER.name());
        roleList.add(Role.RoleName.ADMIN.name());
        token = jwtTokenProvider.createToken(EMAIL, roleList);
    }

    @Test
    void createToken_Ok() {
        Assertions.assertNotNull(token);
        Assertions.assertTrue(token.length() > 128);
    }

    @Test
    public void getAuthentication_Ok() {
        UserDetails bobik = new User(EMAIL, PASSWORD, Collections.emptySet());
        Mockito.when(userDetailsService.loadUserByUsername(any())).thenReturn(bobik);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                (UsernamePasswordAuthenticationToken) this.jwtTokenProvider.getAuthentication(token);
                Assertions.assertEquals(EMAIL,
                        ((User)usernamePasswordAuthenticationToken.getPrincipal()).getUsername());
                Assertions.assertEquals(PASSWORD,
                        ((User)usernamePasswordAuthenticationToken.getPrincipal()).getPassword());
    }

    @Test
    public void getAuthentication_NullToken_NotOk() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> jwtTokenProvider.getAuthentication(null));
    }

    @Test
    public void resolveToken_Ok() {
        HttpServletRequest mock = Mockito.mock(HttpServletRequest.class);
        Mockito.when(mock.getHeader(any())).thenReturn("Bearer " + EMAIL);
        Assertions.assertEquals(EMAIL, jwtTokenProvider.resolveToken(mock));
    }

    @Test
    void getUsername_Ok() {
        Assertions.assertTrue(EMAIL.equals(jwtTokenProvider.getUsername(token)));
    }

    @Test
    void getUsername_NullToken_Ok() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> jwtTokenProvider.getUsername(null));
    }

    @Test
    public void resolveToken_WrongToken_NotOk() {
        HttpServletRequest mock = Mockito.mock(HttpServletRequest.class);
        Mockito.when(mock.getHeader(any())).thenReturn(EMAIL);
        Assertions.assertNotEquals(EMAIL, jwtTokenProvider.resolveToken(mock));
    }

    @Test
    public void validateToken_NotOK() {
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken("1111.2222.4444"));
    }
}
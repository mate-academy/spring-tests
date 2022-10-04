package mate.academy.security.jwt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import mate.academy.model.Role.RoleName;
import mate.academy.security.CustomUserDetailsService;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhY3R1YWxAbWFpbC51YSIsInJvb"
            + "GVzIjpbIkFETUlOIl0sImlhdCI6MTY2NDc5OTE4MiwiZXhwIjoxNjY0ODAyNzgyfQ.xVXWPnDS2RrSS9BF36"
            + "Tm13OV0RaQ3KWTe0XLR0iGNaU";
    private static final String LOGIN = "actual@mail.ua";
    private static final String PASSWORD = "1234";
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(CustomUserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider,
                "secretKey", "secret", String.class);
        ReflectionTestUtils.setField(jwtTokenProvider,
                "validityInMilliseconds", 3600000L, long.class);
    }

    @Test
    void createToken_ok() {
        int expected = TOKEN.length();

        List<String> roles = List.of(RoleName.ADMIN.name());
        String actual = jwtTokenProvider.createToken(LOGIN, roles);

        System.out.println(actual);
        System.out.println(TOKEN);

        assertEquals(expected, actual.length());
    }

    @Test
    void getAuthentication_ok() {
        UserDetails userDetails = User.withUsername(LOGIN)
                .password(PASSWORD)
                .roles(Role.RoleName.ADMIN.name())
                .build();
        Mockito.when(userDetailsService.loadUserByUsername(LOGIN)).thenReturn(userDetails);
        Authentication expected = new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());

        Authentication actual = jwtTokenProvider.getAuthentication(TOKEN);

        assertEquals(expected, actual);
    }

    @Test
    void getUsername_ok() {
        String actual = jwtTokenProvider.getUsername(TOKEN);

        assertEquals(LOGIN, actual);
    }

    @Test
    void validateToken_ok() {
        boolean expected = true;

        boolean actual = jwtTokenProvider.validateToken(TOKEN);

        assertEquals(expected, actual);
    }

    @Test
    void validateToken_tokenNull_notOk() {
        assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(null));
    }

    @Test
    void resolveToken_ok() {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(httpServletRequest.getHeader("Authorization"))
                .thenReturn("Bearer " + TOKEN);
        String actual = jwtTokenProvider.resolveToken(httpServletRequest);

        assertEquals(TOKEN, actual);
    }
}
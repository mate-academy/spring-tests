package mate.academy.security.jwt;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {
    @Mock
    private UserDetailsService userDetailsService;
    private JwtTokenProvider tokenProvider;
    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        tokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(tokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(tokenProvider, "validityInMilliseconds", 3600000);
    }

    @Test
    void createToken_Ok() {
        List<String> roles = new ArrayList<>();
        roles.add("ADMIN");
        roles.add("USER");
        String login = "id@hello.ua";
        String actual = tokenProvider.createToken(login, roles);
        Assertions.assertNotNull(actual);
    }

    @Test
    void getUserName_Ok() {
        List<String> roles = new ArrayList<>();
        roles.add("ADMIN");
        roles.add("USER");
        String login = "id@hello.ua";
        String token = tokenProvider.createToken(login, roles);
        String username = tokenProvider.getUsername(token);
        Assertions.assertEquals(login, username);
    }

    @Test
    void getUserName_Null_Login_Not_Ok() {
        List<String> roles = new ArrayList<>();
        roles.add("ADMIN");
        roles.add("USER");
        String nullUserNameToken = tokenProvider.createToken(null, roles);
        String nullUserName = tokenProvider.getUsername(nullUserNameToken);
        Assertions.assertEquals(null, nullUserName);
    }

    @Test
    void getAuthentication_Ok() {
        List<String> roles = new ArrayList<>();
        roles.add("ADMIN");
        roles.add("USER");
        String login = "id@hello.ua";
        String password = "1234";
        String token = tokenProvider.createToken(login, roles);
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ADMIN"),
                new SimpleGrantedAuthority("USER"));
        User.withUsername(login).username(login).password("1234").authorities(authorities).build();
        UserDetails testUserDetails = User.withUsername(login)
                .username(login)
                .password(password)
                .authorities(authorities)
                .build();
        when(userDetailsService.loadUserByUsername(login)).thenReturn(testUserDetails);
        Authentication authentication = tokenProvider.getAuthentication(token);
        Assertions.assertNotNull(authentication);
    }

    @Test
    void resolveToken_Ok() {
        List<String> roles = new ArrayList<>();
        roles.add("ADMIN");
        roles.add("USER");
        String login = "id@hello.ua";
        String token = tokenProvider.createToken(login, roles);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        String result = tokenProvider.resolveToken(request);
        Assertions.assertNotNull(result);
    }

    @Test
    void validateToken_Ok() {
        List<String> roles = new ArrayList<>();
        roles.add("ADMIN");
        roles.add("USER");
        String login = "id@hello.ua";
        String token = tokenProvider.createToken(login, roles);
        boolean validateToken = tokenProvider.validateToken(token);
        Assertions.assertTrue(validateToken);
    }
}

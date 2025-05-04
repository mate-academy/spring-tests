package mate.academy.security.jwt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

public class JwtTokenProviderTest {
    private static final String LOGIN = "john@ttt.com";
    private static final String PASSWORD = "1234";
    private static final List<String> ROLES = List.of(Role.RoleName.USER.name());
    private static String token;
    private static UserDetailsService userDetailsService;
    private static JwtTokenProvider jwtTokenProvider;

    @Before
    public void setup() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey",
                "secret");
        ReflectionTestUtils.setField(jwtTokenProvider,
                "validityInMilliseconds",
                3600000L);
        token = jwtTokenProvider.createToken(LOGIN, ROLES);
    }

    @Test
    public void testCreateToken_Ok() {
        System.out.println(token);
        Assertions.assertNotNull(token);
    }

    @Test
    public void testGetAuthentication_OK() {
        UserDetails userDetails = User.withUsername(LOGIN).password(PASSWORD)
                .roles(String.valueOf(ROLES))
                .build();
        Mockito.when(userDetailsService.loadUserByUsername(LOGIN))
                .thenReturn(userDetails);
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        Assertions.assertNotNull(authentication);
        Assertions.assertEquals(LOGIN, authentication.getName());
    }

    @Test
    public void testGetUserName_OK() {
        String result = jwtTokenProvider.getUsername(token);
        Assertions.assertEquals(LOGIN,result);
    }

    @Test
    public void testResolveToken_Ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);
        String result = jwtTokenProvider.resolveToken(request);
        Assertions.assertEquals(token, token);
    }

    @Test
    public void testValidateToken_Ok() {
        boolean isValid = jwtTokenProvider.validateToken(token);
        Assertions.assertTrue(isValid);
    }
}

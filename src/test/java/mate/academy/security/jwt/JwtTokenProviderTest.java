package mate.academy.security.jwt;

import io.jsonwebtoken.Jwts;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

class JwtTokenProviderTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "12345678";
    private static final long VALIDITY_IN_MILLISECONDS = 3600000L;
    private static final String SECRET = "secret";
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;
    private String token;
    private List<String> roleList;

    @BeforeEach
    void setUp() throws ReflectiveOperationException {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);

        Field secretKey = JwtTokenProvider.class.getDeclaredField("secretKey");
        secretKey.setAccessible(true);
        secretKey.set(jwtTokenProvider, SECRET);

        Field validityInMilliseconds =
                JwtTokenProvider.class.getDeclaredField("validityInMilliseconds");
        validityInMilliseconds.setAccessible(true);
        validityInMilliseconds.setLong(jwtTokenProvider, VALIDITY_IN_MILLISECONDS);

        roleList = new ArrayList<>();
        roleList.add(Role.RoleName.ADMIN.name());
        roleList.add(Role.RoleName.USER.name());

        token = jwtTokenProvider.createToken(EMAIL, roleList);
    }

    @Test
    void createToken_ok() {
        Assertions.assertNotNull(token);
        String actual = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        Assertions.assertEquals(actual, EMAIL);
    }

    @Test
    void getUsername_ok() {
        String actual = jwtTokenProvider.getUsername(token);
        Assertions.assertEquals(actual, EMAIL);
    }

    @Test
    void getAuthentication_ok() {
        UserDetails userDetails = User.withUsername(EMAIL)
                .password(PASSWORD)
                .roles(Role.RoleName.USER.name())
                .build();
        Mockito.when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.getPrincipal().toString().contains(EMAIL));
    }

    @Test
    void resolveToken_ok() {
        String bearerToken = "Bearer " + token;
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", bearerToken);
        String actual = jwtTokenProvider.resolveToken(request);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, token);
    }

    @Test
    void validateToken_ok() {
        boolean actual = jwtTokenProvider.validateToken(token);
        Assertions.assertTrue(actual);
    }
}

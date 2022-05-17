package mate.academy.security.jwt;

import java.lang.reflect.Field;
import java.util.List;
import mate.academy.model.User;
import mate.academy.util.UserUtilForTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.User.UserBuilder;
import javax.servlet.http.HttpServletRequest;

class JwtTokenProviderTest {
    private String validToken ;
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;
    private UserUtilForTest userUtil;
    private String invalidToken;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        userUtil = new UserUtilForTest();
        Class<JwtTokenProvider> JTPClass = JwtTokenProvider.class;
        try {
            Field secretKey = JTPClass.getDeclaredField("secretKey");
            Field validityInMilliseconds = JTPClass.getDeclaredField("validityInMilliseconds");
            secretKey.setAccessible(true);
            validityInMilliseconds.setAccessible(true);
            secretKey.set(jwtTokenProvider, "secret");
            validityInMilliseconds.set(jwtTokenProvider, 3600000L);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Unable to insert values into fields", e);
        }
        invalidToken = "122341";
        validToken = jwtTokenProvider.createToken(userUtil.getBorisEmail(),
                List.of(userUtil.getUserRole().getRoleName().name()));
    }

    @Test
    void createToken_ok(){
        User user = userUtil.getUserBoris();
        String actualToken = jwtTokenProvider.createToken(user.getEmail(),
                List.of(userUtil.getUserRole().getRoleName().name()));
        Assertions.assertNotNull(actualToken);
        Assertions.assertTrue(actualToken.split("\\.").length == 3);
    }

    @Test
    void getAuthentication_ok() {
        UserBuilder builder = org.springframework.security.core.userdetails
                .User.withUsername(userUtil.getBorisEmail());
        builder.password(userUtil.getBorisPassword());
        builder.roles(userUtil.getUserRole().getRoleName().name());
        Mockito.when(userDetailsService.loadUserByUsername(Mockito.any()))
                .thenReturn(builder.build());
        Authentication action = jwtTokenProvider.getAuthentication(validToken);
        Assertions.assertNotNull(action);
        Assertions.assertNotNull(userUtil.getBorisEmail() , action.getName());
        Assertions.assertEquals("[ROLE_USER]", action.getAuthorities().toString());
    }

    @Test
    void getUsername_ok() {
        String actual = jwtTokenProvider.getUsername(validToken);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(userUtil.getBorisEmail(), actual);
    }

    @Test
    void resolveToken_ok() {
        HttpServletRequest httpMock = Mockito.mock(HttpServletRequest.class);
        Mockito.when(httpMock.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        String action = jwtTokenProvider.resolveToken(httpMock);
        Assertions.assertNotNull(action);
        Assertions.assertEquals(validToken, action);
    }

    @Test
    void resolveToken_notBearer_notOk() {
        HttpServletRequest httpMock = Mockito.mock(HttpServletRequest.class);
        Mockito.when(httpMock.getHeader("Authorization")).thenReturn(invalidToken);
        String action = jwtTokenProvider.resolveToken(httpMock);
        Assertions.assertNull(action);
    }

    @Test
    void validateToken_ok() {
        boolean actual = jwtTokenProvider.validateToken(validToken);
        Assertions.assertTrue(actual);
    }

    @Test
    void validateToken_invalidToken_notOk() {
        try {
            jwtTokenProvider.validateToken(invalidToken);
        } catch (RuntimeException e) {
            Assertions.assertEquals("Expired or invalid JWT token", e.getMessage());
            return;
        }
        Assertions.fail("Expected RuntimeException while trying check invalid token");
    }
}

package mate.academy.security.jwt;

import java.lang.reflect.Field;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {
    private final static String EMAIL = "bob@gmail.com";
    @Mock
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private String createdToken;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        try {
            Field secretKey = JwtTokenProvider.class.getDeclaredField("secretKey");
            Field validityInMilliseconds = JwtTokenProvider.class.getDeclaredField("validityInMilliseconds");
            secretKey.setAccessible(true);
            secretKey.set(jwtTokenProvider, "secret");
            validityInMilliseconds.setAccessible(true);
            validityInMilliseconds.set(jwtTokenProvider, 3600000L);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("There are no such fields", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Can't set value to field", e);
        }
        createdToken = jwtTokenProvider.createToken(EMAIL, List.of("USER", "ADMIN"));
    }

    @Test
    void createToken_consistThreePart_ok() {
        String jwt = jwtTokenProvider.createToken(EMAIL, List.of("ADMIN", "USER"));
        String[] result = jwt.split("\\.");
        Assertions.assertNotNull(jwt, "Token must be not null");
        Assertions.assertEquals(3, result.length,
                "Expected length" + 3 + " but actual " + result.length);
    }

    @Test
    void resolveToken_validHeader_ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + createdToken);
        String resolvedToken = jwtTokenProvider.resolveToken(request);
        Assertions.assertNotNull(resolvedToken, "Token must be not null");
    }

    @Test
    void resolveToken_nullValueHeader_notOk() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn(null);
        String resolvedToken = jwtTokenProvider.resolveToken(request);
        Assertions.assertNull(resolvedToken, "Method should returns null when no header 'Authorization'");
    }

    @Test
    void resolveToken_tokenStartWithNotBearer_notOk() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("NotBearer " + createdToken);
        String resolvedToken = jwtTokenProvider.resolveToken(request);
        Assertions.assertNull(resolvedToken,
                "Method should returns null when token starts with not 'Bearer'");
    }

    @Test
    void getAuthentication_validToken() {
        User.UserBuilder builder =
                org.springframework.security.core.userdetails.User.withUsername(EMAIL);
        builder.password("12345678");
        builder.roles("USER", "ADMIN");
        UserDetails userDetails = builder.build();

        Mockito.when(userDetailsService.loadUserByUsername(Mockito.anyString())).thenReturn(userDetails);
        Authentication authentication = jwtTokenProvider.getAuthentication(createdToken);
        Assertions.assertNotNull(authentication, "Authentication must be not null");
        Assertions.assertEquals(authentication.getName(), EMAIL, "Username is not equal");
    }

    @Test
    void validate_validToken_ok() {
        boolean isValid = jwtTokenProvider.validateToken(createdToken);
        Assertions.assertTrue(isValid, "Expect that method return true for token " + createdToken);
    }

    @Test
    void validate_nullValueToken_notOk() {
        Assertions.assertThrows(RuntimeException.class, () -> jwtTokenProvider.validateToken(null),
                "Method should throw exception for null value");
    }

    @Test
    void validate_notValidToken_notOk() {
        Assertions.assertThrows(RuntimeException.class, () -> jwtTokenProvider.validateToken("asdfq12"),
                "Method should throw exception for not valid token");
    }

    @Test
    void validate_emptyToken_notOk() {
        Assertions.assertThrows(RuntimeException.class, () -> jwtTokenProvider.validateToken(""),
                "Method should throw exception for not empty token");
    }

    @Test
    void validate_expiredToken_notOk() throws NoSuchFieldException, IllegalAccessException {
        Field validityInMilliseconds =
                JwtTokenProvider.class.getDeclaredField("validityInMilliseconds");
        validityInMilliseconds.setAccessible(true);
        validityInMilliseconds.set(jwtTokenProvider, -1000L);
        createdToken = jwtTokenProvider.createToken("bob@gmail.com", List.of("USER", "ADMIN"));
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(createdToken),
                "Method should throw exception for expired token");
    }
}
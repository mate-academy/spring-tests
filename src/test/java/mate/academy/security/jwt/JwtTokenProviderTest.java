package mate.academy.security.jwt;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

class JwtTokenProviderTest {
    public static final int VALIDITY_IN_MILLISECONDS = 3600000;
    private static final String STATIC_USERNAME = "test@gmail.com";
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        UserDetailsService userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(
                jwtTokenProvider, "validityInMilliseconds", VALIDITY_IN_MILLISECONDS);
    }

    @Test
    void getUsername() {
        String token = jwtTokenProvider.createToken(STATIC_USERNAME, List.of("USER"));
        String username = jwtTokenProvider.getUsername(token);
        assertNotNull(username, "Username cannot be null");
        assertEquals(STATIC_USERNAME, username);
    }

    @Test
    void resolveToken() {
        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
        String header = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsIn" +
                "JvbGVzIjpbIlVTRVIiXSwiaWF0IjoxNjQxNDY5MzM0LCJleHAiOjE2NDE0NjkzMzR9.Jx" +
                "iUm8p2o0gIdVMVPoyUNE7SNE0YmpFdPqXCgyXE5iM";
        Mockito.when(httpServletRequest.getHeader("Authorization")).thenReturn(header);
        String resolvedToken = jwtTokenProvider.resolveToken(httpServletRequest);
        assertNotNull(resolvedToken);
        assertEquals("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsIn" +
                "JvbGVzIjpbIlVTRVIiXSwiaWF0IjoxNjQxNDY5MzM0LCJleHAiOjE2NDE0NjkzMzR9.Jx" +
                "iUm8p2o0gIdVMVPoyUNE7SNE0YmpFdPqXCgyXE5iM", resolvedToken);
    }

    @Test
    void validateToken_RuntimeException() {
        String expiredToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsIn" +
                "JvbGVzIjpbIlVTRVIiXSwiaWF0IjoxNjQxNDY5MzM0LCJleHAiOjE2NDE0NjkzMzR9.Jx" +
                "iUm8p2o0gIdVMVPoyUNE7SNE0YmpFdPqXCgyXE5iM";
        try {
            jwtTokenProvider.validateToken(expiredToken);
        } catch (RuntimeException e) {
            assertEquals("Expired or invalid JWT token", e.getMessage());
            return;
        }
        fail();
    }
}

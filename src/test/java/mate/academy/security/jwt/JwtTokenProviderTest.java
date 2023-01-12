package mate.academy.security.jwt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class JwtTokenProviderTest {
    private static final String USER_ROLE_NAME = "USER";
    private static final List<String> ROLES = List.of(USER_ROLE_NAME);
    private static final String EMAIL = "user@gmail.com";
    private static UserDetailsService userDetailsService;
    private static JwtTokenProvider jwtTokenProvider;
    private String token;

    @BeforeAll
    static void beforeAll() {
        userDetailsService = mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
    }

    @BeforeEach
    void setUp() {
        token = jwtTokenProvider.createToken(EMAIL, ROLES);
    }

    @Test
    void createToken_Ok() {
        assertNotNull(token);
    }
}
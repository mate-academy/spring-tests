package mate.academy.security.jwt;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;

class JwtTokenProviderTest {
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;

//    @BeforeAll
//    static void beforeAll() {
//        ReflectionTestUtils
//    }

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = Mockito.spy(new JwtTokenProvider(userDetailsService));
    }

    @Test
    void createToken_validLoginAndRoles_Ok() {
        String login = "Smith";
        List<String> roles = new ArrayList<>();
        roles.add(Role.RoleName.USER.name());
        String token = jwtTokenProvider.createToken(login, roles);
        assertNotNull(token);
        assertTrue(token.matches(".+\\..+\\..+"));
    }
}
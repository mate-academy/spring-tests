package mate.academy.security.jwt;

import mate.academy.security.CustomUserDetailsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public class JwtTokenProviderTest {
    private UserDetailsService userDetailsService = Mockito.mock(CustomUserDetailsService.class);
    private JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(userDetailsService);

    @BeforeAll
    static void beforeAll() {

    }

    @Test
    void createToken_Ok() {
        String login = "bob@i.ua";
        List<String> roles = List.of("USER", "ADMIN");
        String actualToken = jwtTokenProvider.createToken(login, roles);
        Assertions.assertNotNull(actualToken);
        Assertions.assertNotEquals(0, actualToken.length());
    }
}

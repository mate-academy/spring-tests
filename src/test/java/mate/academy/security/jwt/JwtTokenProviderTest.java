package mate.academy.security.jwt;

import java.lang.reflect.Field;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;

class JwtTokenProviderTest {
    public static final int INDEX_OF_INITIAL_SYMBOL_FOR_PERMANENT_PART_OF_TOKEN = 0;
    public static final int INDEX_OF_FINAL_SYMBOL_FOR_PERMANENT_PART_OF_TOKEN = 85;
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
    }

    @Test
    void createTokenByLoginAndRoles_Ok() throws NoSuchFieldException, IllegalAccessException {
        String login = "Max@i.ua";
        List<String> roles = List.of("ADMIN", "USER");
        Class<? extends JwtTokenProvider> jwtTokenProviderClass = jwtTokenProvider.getClass();
        Field secretKey = jwtTokenProviderClass.getDeclaredField("secretKey");
        secretKey.setAccessible(true);
        secretKey.set(jwtTokenProvider, "1234");
        secretKey.setAccessible(false);
        String token = jwtTokenProvider.createToken(login, roles);
        String actual = token.substring(INDEX_OF_INITIAL_SYMBOL_FOR_PERMANENT_PART_OF_TOKEN,
                INDEX_OF_FINAL_SYMBOL_FOR_PERMANENT_PART_OF_TOKEN);
        String expected = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNYXhAaS51YSIsInJvbGVzIjpbIkFE"
                + "TUlOIiwiVVNFUiJdLCJpYXQi";
        Assertions.assertNotNull(token);
        Assertions.assertEquals(expected, actual);
    }
}

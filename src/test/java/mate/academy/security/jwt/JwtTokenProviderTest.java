package mate.academy.security.jwt;

import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

class JwtTokenProviderTest {
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;
    @BeforeEach
    void setUp() throws RuntimeException {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        Class<JwtTokenProvider> jwtTokenProviderClass = JwtTokenProvider.class;
        try {
            jwtTokenProvider = jwtTokenProviderClass
                    .getConstructor(UserDetailsService.class)
                    .newInstance(userDetailsService);
            Field secretKeyFiled = jwtTokenProviderClass
                    .getField("secretKey");
            Field validityInMilliseconds = jwtTokenProviderClass
                    .getField("validityInMilliseconds");
            secretKeyFiled.setAccessible(true);
            validityInMilliseconds.setAccessible(true);
            secretKeyFiled.set(jwtTokenProvider, "secret");
            validityInMilliseconds.set(jwtTokenProvider, 3600000L);
        } catch (Exception e) {
            throw new RuntimeException("Can't create JwtTokenProvider", e);
        }
        jwtTokenProvider.init();
    }

    @Test
    void createToken_Ok() {
        String token = jwtTokenProvider.createToken("bob@gmail.com",
                List.of(Role.RoleName.USER.name()));
        Assertions.assertNotNull(token);
        Assertions.assertFalse(token.isEmpty());
        Assertions.assertEquals(3, token.split(".").length);
    }

    @Test
    void getAuthentication() {
    }

    @Test
    void getUsername() {
    }

    @Test
    void resolveToken() {
    }

    @Test
    void validateToken() {
    }
}
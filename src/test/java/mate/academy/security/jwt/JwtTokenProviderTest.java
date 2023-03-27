package mate.academy.security.jwt;

import java.util.List;
import java.util.stream.Stream;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static Role adminRole;
    private static Role userRole;
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    @BeforeAll
    static void init() {
        userRole = new Role();
        userRole.setId(1L);
        userRole.setRoleName(Role.RoleName.USER);
        adminRole = new Role();
        adminRole.setId(2L);
        adminRole.setRoleName(Role.RoleName.ADMIN);
    }

    @BeforeEach
    void setUp() {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "secret");
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", 3600000L);
    }

    @Test
    void createToken_correctValue_ok() {
        String login = "user@gmail.com";
        List<String> roleList = Stream.of(userRole, adminRole)
                .map(a -> a.getRoleName().name())
                .toList();
        String token = jwtTokenProvider.createToken(login, roleList);
        Assertions.assertEquals(3, token.split("\\.").length);
    }

    @Test
    void getUsername_correctValue_ok() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGdtYWlsLmNvbSIsInJvbGVz"
                 + "IjpbIlVTRVIiLCJBRE1JTiJdLCJpYXQiOjE2Nzk4OTMwNjIsImV4cCI6MTY3O"
                 + "Tg5MzA2NX0.8vP-HgNOCIBdekJ6lXkOzJ_QD5xnyyj9VJTQkMCv4tU";
        String actual = jwtTokenProvider.getUsername(token);
        Assertions.assertEquals("user@gmail.com", actual);
    }
    //    @Test
    //    void resolveToken_correctValue_ok() {
    //        HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
    //      //  Mockito.when(httpServletRequest.getHeader(any())).thenReturn()
    //    }
}

package mate.academy.security;

import io.jsonwebtoken.ExpiredJwtException;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private static UserDetailsService userDetailsService;
    private static JwtTokenProvider jwtTokenProvider;
    private static String secretKey;
    private static User user;

    @BeforeAll
    static void beforeAll() {
        secretKey = Base64.getEncoder().encodeToString("secret".getBytes());
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        jwtTokenProvider = Mockito.spy(jwtTokenProvider);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", secretKey);
        user = new User();
        user.setEmail("bob@i.ua");
        user.setPassword("1234");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));

    }

    @Test
    void createToken_Ok() {
        String login = "bob@i.ua";
        List<String> roles = List.of("USER");
        String actual = jwtTokenProvider.createToken(login, roles);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(157,actual.length());
    }

    @Test
    void getAuthentication_Ok() {
        String login = "bob@i.ua";
        List<String> roles = List.of("USER");
        org.springframework.security.core.userdetails.User.UserBuilder builder
                = org.springframework.security.core.userdetails.User.withUsername(login);
        builder.password("1234");
        builder.roles(roles.toArray(String[]::new));
        UserDetails userDetails = builder.build();
        Authentication expected =
                new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
        Mockito.when(userDetailsService
                .loadUserByUsername(Mockito.anyString()))
                .thenReturn(userDetails);
        String token = jwtTokenProvider.createToken(login, roles);
        Mockito.doReturn(token).when(jwtTokenProvider).getUsername(Mockito.anyString());
        Authentication actual = jwtTokenProvider.getAuthentication(token);
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void getUsername_Ok() {
        List<String> roles = List.of("USER");
        String expected = user.getEmail();
        String actual = null;
        String token = jwtTokenProvider.createToken(user.getEmail(), roles);
        try {
            jwtTokenProvider.getUsername(token);
        } catch (ExpiredJwtException e) {
            actual = e.getClaims().getSubject();
        }
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void resolveToken_Ok() {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.addParameter("header","Authorization");
        mockHttpServletRequest.addHeader("Authorization","Bearer someData");
        String actual = jwtTokenProvider.resolveToken(mockHttpServletRequest);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals("someData",actual);
    }

    @Test
    void validateToken_Ok() {
        List<String> roles = List.of("USER");
        String token = jwtTokenProvider.createToken(user.getEmail(), roles);
        boolean actual = false;
        try {
            jwtTokenProvider.getUsername(token);
        } catch (ExpiredJwtException e) {
            actual = true;
        }
        Assertions.assertTrue(actual);
    }

    @Test
    void validateToken_NotOk() {
        List<String> roles = List.of("USER");
        String token = jwtTokenProvider.createToken(user.getEmail(), roles);
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(token));
        String invalidToken = "iAmInvalidToken";
        Assertions.assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(invalidToken));
    }
}

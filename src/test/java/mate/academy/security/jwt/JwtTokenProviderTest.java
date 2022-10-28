package mate.academy.security.jwt;


import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib2IiLCJyb2xlcyI6WyJV" +
            "U0VSIl0sImlhdCI6MTY2Njk2MjgzMywiZXhwIjoxNjY2OTY2NDMzfQ.W0yngX3uwZUFWmV64TopUXWpLS" +
            "EPh7iZfg8SuylF1uw";
    private static final String LOGIN = "bob";
    private static final String PASSWORD = "1234";
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() throws Exception {
        userDetailsService = Mockito.mock(UserDetailsService.class);
        jwtTokenProvider = new JwtTokenProvider(userDetailsService);
        ReflectionTestUtils.setField(jwtTokenProvider,
                "secretKey", "secret", String.class);
        ReflectionTestUtils.setField(jwtTokenProvider,
                "validityInMilliseconds", 3600000L, long.class);
    }

    @Test
    public void createToken_Ok() {
        List<String> roles = List.of(Role.RoleName.USER.name());
        String token = jwtTokenProvider.createToken(LOGIN, roles);
        System.out.println(token);
        assertNotNull(token);
        assertEquals(151, token.length());
    }

    @Test
    public void getAuthentication_Ok() {
        UserDetails userDetails = User.withUsername(LOGIN)
                .password(PASSWORD)
                .roles(Role.RoleName.USER.name())
                .build();
        Mockito.when(userDetailsService.loadUserByUsername(LOGIN)).thenReturn(userDetails);
        UsernamePasswordAuthenticationToken expected =
                new UsernamePasswordAuthenticationToken(userDetails, "",
                        userDetails.getAuthorities());
        Authentication actual = jwtTokenProvider.getAuthentication(TOKEN);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void getUsername_Ok() {
        String actual = jwtTokenProvider.getUsername(TOKEN);
        assertNotNull(actual);
        assertEquals(LOGIN, actual);
    }

    @Test
    public void resolveToken_Ok() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + TOKEN);
        String actual = jwtTokenProvider.resolveToken(request);
        assertNotNull(actual);
        assertEquals(TOKEN, actual);
    }

    @Test
    public void validateToken_Ok() {
        assertTrue(jwtTokenProvider.validateToken(TOKEN));
    }

    @Test
    public void validateToken_NotOk() {
        String notValidToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJib2IiLCJyb2xlcyI6WyJVU" +
                "0VSIl0sImlhdCI6MTY2Njk1NjU3NCwiZXhwIjoxNjY2OTYwMTc0fQ.zVuBvloyE3BfejA" +
                "MourY7hxmQFsYZdmpa6bHS3_TVd4";
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> jwtTokenProvider.validateToken(notValidToken));
        assertEquals("Expired or invalid JWT token", exception.getMessage());
    }
}

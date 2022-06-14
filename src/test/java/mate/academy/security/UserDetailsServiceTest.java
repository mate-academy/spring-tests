package mate.academy.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class UserDetailsServiceTest {
    private static final String ROLE_PREFIX = "ROLE_";
    private static final Role USER = new Role(Role.RoleName.USER);
    private static final Role ADMIN = new Role(Role.RoleName.ADMIN);
    private final UserService userService = Mockito.mock(UserService.class);
    private final UserDetailsService userDetailsService =
            new CustomUserDetailsService(userService);

    @Test
    void loadUserByUsername_Ok() {
        Set<Role> userRoles = Set.of(USER);
        String userPassword = "1234";
        Mockito.when(userService.findByEmail(anyString()))
                .thenAnswer(i -> mockUserByEmail(i.getArgument(0), userPassword, userRoles));
        String email = "user@mail.com";
        UserDetails actual = userDetailsService.loadUserByUsername(email);
        assertNotNull(actual);
        assertEquals(email, actual.getUsername());
        assertEquals(userPassword, actual.getPassword());
        assertEquals(userRoles.size(), actual.getAuthorities().size());
        List<String> authorities = convertAuthoritiesToStrings(actual.getAuthorities());
        List<String> roleList = convertRolesToStrings(userRoles);
        assertTrue(authorities.containsAll(roleList));

        Set<Role> adminRoles = Set.of(USER, ADMIN);
        String adminPassword = "4321";
        email = "admin@mail.com";
        Mockito.when(userService.findByEmail(anyString()))
                .thenAnswer(i -> mockUserByEmail(i.getArgument(0), adminPassword, adminRoles));
        actual = userDetailsService.loadUserByUsername(email);
        assertNotNull(actual);
        assertEquals(email, actual.getUsername());
        assertEquals(adminPassword, actual.getPassword());
        assertEquals(adminRoles.size(), actual.getAuthorities().size());
        authorities = convertAuthoritiesToStrings(actual.getAuthorities());
        roleList = convertRolesToStrings(adminRoles);
        assertTrue(authorities.containsAll(roleList));
    }

    @Test
    void loadUserByUsername_notExists_notOk() {
        Mockito.when(userService.findByEmail(anyString())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("any"));
    }

    private Optional<User> mockUserByEmail(String email, String password, Set<Role> roles) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(roles);
        return Optional.of(user);
    }

    private Optional<User> mockUserByEmail1(String email, Supplier<String> passSupplier,
                                            Supplier<Set<Role>> rolesSupplier) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passSupplier.get());
        user.setRoles(rolesSupplier.get());
        return Optional.of(user);
    }

    private List<String> convertAuthoritiesToStrings
            (Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    private List<String> convertRolesToStrings(Set<Role> roles) {
        return roles.stream()
                .map(Role::getRoleName)
                .map(Role.RoleName::name)
                .map(s -> ROLE_PREFIX + s)
                .collect(Collectors.toList());
    }
}

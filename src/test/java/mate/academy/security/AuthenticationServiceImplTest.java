package mate.academy.security;

import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.RoleService;
import mate.academy.service.UserService;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;

public class AuthenticationServiceImplTest {
    private AuthenticationServiceImpl authenticationService;
    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        roleService = Mockito.mock(RoleService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationService = new AuthenticationServiceImpl(userService,
                roleService, passwordEncoder);
    }

    @Test
    void register_Ok() {
        User user = new User();
        user.setEmail("modernboy349@gmail.com");
        user.setPassword("Hello123");
        user.setRoles(Set.of(new Role(Role.RoleName.ADMIN)));
        Mockito.when(userService.save(user)).thenReturn(user);
        Mockito.when(roleService.getRoleByName(any()))
                .thenReturn(new Role(Role.RoleName.USER));
        User actual = authenticationService.register(user.getEmail(), user.getPassword());

        Assertions.assertEquals(user, actual);
    }

    @Test
    void login_Ok() {
        User user = new User();
        user.setEmail("modernboy349@gmail.com");
        user.setPassword("Hello123");
//        Mockito.when(userService.lo(user)).thenReturn(user);
        User actual = userService.save(user);

        Assertions.assertEquals(actual, user);
    }
}

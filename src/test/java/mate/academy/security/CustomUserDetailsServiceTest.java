package mate.academy.security;

import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    @Mock
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserService userService;
    private CustomUserDetailsService detailsService;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userDao, passwordEncoder);
        detailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_Ok() {
        Role roleUser = new Role();
        roleUser.setRoleName(Role.RoleName.USER);
        User user = new User();
        user.setEmail("id@hello.ua");
        user.setPassword("1234");
        user.setRoles(Set.of(roleUser));
        when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Assertions.assertNotNull(detailsService.loadUserByUsername("id@hello.ua"));
    }

    @Test
    void loadByUsername_Not_Existing_User_Not_Ok() {
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> detailsService.loadUserByUsername("id@hello.ua"));
    }
}

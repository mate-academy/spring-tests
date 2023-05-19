package mate.academy.service;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private UserService userService;
    @Mock
    private UserDao userDao;
    @Mock
    private PasswordEncoder passwordEncoder;
    private User user;
    private Role userRole;

    @BeforeEach
    void setUp() {
        userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");
        user.setPassword("1234");
        user.setRoles(Set.of(userRole));
        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @Test
    void userService_save_Ok() {
        Mockito.when(passwordEncoder.encode(any()))
                .thenReturn(user.getPassword());
        Mockito.when(userDao.save(any())).thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void userService_findById_Ok() {
        Mockito.when(userDao.findById(any())).thenReturn(Optional.of(user));
        User actual = userService.findById(user.getId()).get();
        Assertions.assertEquals(actual, user);
    }

    @Test
    void userService_findByEmail() {
        Mockito.when(userDao.findByEmail(any())).thenReturn(Optional.of(user));
        User actual = userService.findByEmail(user.getEmail()).get();
        Assertions.assertEquals(actual, user);
    }
}

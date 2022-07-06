package mate.academy.service;

import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;

class UserServiceTest {
    private UserService userService;
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @Test
    void save_ok() {
        User expectedUser = createExpectedUser();
        Mockito.when(userDao.save(expectedUser)).thenReturn(expectedUser);
        User actualUser = userService.save(expectedUser);
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(expectedUser.getId(), actualUser.getId());
        Assertions.assertEquals(expectedUser, actualUser);
    }

    @Test
    void findById_ok() {
        User expectedUser = createExpectedUser();
        Mockito.when(userDao.findById(any())).thenReturn(Optional.of(expectedUser));
        Optional<User> actualUserOptional = userService.findById(expectedUser.getId());
        Assertions.assertTrue(actualUserOptional.isPresent());
        User actualUser = actualUserOptional.get();
        Assertions.assertEquals(expectedUser.getId(), actualUser.getId());
        Assertions.assertEquals(expectedUser, actualUser);
    }

    @Test
    void findByEmail_ok() {
        User expectedUser = createExpectedUser();
        Mockito.when(userDao.findByEmail(expectedUser.getEmail()))
                .thenReturn(Optional.of(expectedUser));
        Optional<User> actualUserOptional = userService.findByEmail(expectedUser.getEmail());
        Assertions.assertTrue(actualUserOptional.isPresent());
        User actualUser = actualUserOptional.get();
        Assertions.assertEquals(expectedUser.getId(), actualUser.getId());
        Assertions.assertEquals(expectedUser, actualUser);
    }

    private User createExpectedUser() {
        User expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setEmail("expected@i.ua");
        expectedUser.setPassword("1234");
        Role adminRole = new Role();
        adminRole.setRoleName(Role.RoleName.ADMIN);
        Role userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        expectedUser.setRoles(Set.of(adminRole, userRole));
        return expectedUser;
    }
}
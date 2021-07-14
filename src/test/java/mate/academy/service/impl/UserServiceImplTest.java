package mate.academy.service.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private User expected;

    @BeforeEach
    void setUp() {
        expected = new User();
        expected.setId(1L);
        expected.setEmail("lucy1234@gmail.com");
        expected.setPassword("12345tyr");
        expected.setRoles(Set.of(new Role(Role.RoleName.USER)));
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @Test
    void save_ok() {
        Mockito.when(passwordEncoder.encode(Mockito.any())).thenReturn("encodedPassword");
        User user = new User();
        user.setEmail("lucy1234@gmail.com");
        user.setPassword("12345tyr");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userDao.save(user)).thenReturn(expected);
        User userFromDb = userService.save(user);
        Assertions.assertNotNull(userFromDb);
        Assertions.assertEquals(expected, userFromDb);
    }

    @Test
    void findById_validId_ok() {
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.of(expected));
        Optional<User> userFromDb = userService.findById(1L);
        Assertions.assertEquals(Optional.of(expected), userFromDb);
    }

    @Test
    void findById_invalidId_ok(){
        Mockito.when(userDao.findById(2L)).thenReturn(Optional.ofNullable(null));
        Optional<User> userFromDb = userService.findById(2L);
        Assertions.assertEquals(Optional.ofNullable(null), userFromDb);
    }

    @Test
    void findByEmail_validEmail_ok() {
        Mockito.when(userDao.findByEmail("lucy1234@gmail.com")).thenReturn(Optional.of(expected));
        Optional<User> userFromDb = userService.findByEmail("lucy1234@gmail.com");
        Assertions.assertEquals(Optional.of(expected), userFromDb);
    }

    @Test
    void findByEmail_invalidEmail_ok() {
        Mockito.when(userDao.findByEmail("lucy@gmail.com")).thenReturn(Optional.ofNullable(null));
        Optional<User> userFromDb = userService.findByEmail("lucy@gmail.com");
        Assertions.assertEquals(Optional.ofNullable(null), userFromDb);
    }
}
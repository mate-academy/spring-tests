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
    private static final Long ID_1 = 1L;
    private static final String USER_EMAIL = "valid@i.ua";
    private static final String USER_PASSWORD = "abcd1234";
    private static final Role USER_ROLE = new Role(Role.RoleName.USER);
    private UserService userService;
    private UserDao userDao;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(ID_1);
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
        user.setRoles(Set.of(USER_ROLE));
        userDao = Mockito.mock(UserDao.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(passwordEncoder.encode(user.getPassword())).thenReturn(USER_PASSWORD);
        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @Test
    void save_validData_ok() {
        Mockito.when(userDao.save(user)).thenReturn(user);
        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getId(), actual.getId());
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
        Assertions.assertEquals(user.getRoles(), actual.getRoles());
    }

    @Test
    void findById_validData_ok() {
        Mockito.when(userDao.findById(ID_1)).thenReturn(Optional.of(user));
        Optional<User> actualOptional = userService.findById(ID_1);
        Assertions.assertNotNull(actualOptional.get());
        User actual = actualOptional.get();
        Assertions.assertEquals(user.getId(), actual.getId());
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
        Assertions.assertEquals(user.getRoles(), actual.getRoles());
    }

    @Test
    void findById_notValidId_notOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> userService.findById(2L).get());
    }

    @Test
    void findByEmail_validData_ok() {
        Mockito.when(userDao.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));
        Optional<User> actualOptional = userService.findByEmail(user.getEmail());
        Assertions.assertNotNull(actualOptional.get());
        User actual = actualOptional.get();
        Assertions.assertEquals(user.getId(), actual.getId());
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
        Assertions.assertEquals(user.getRoles(), actual.getRoles());
    }

    @Test
    void findByEmail_notValidEmail_notOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> userService.findByEmail("notValid@i.ua").get());
    }
}

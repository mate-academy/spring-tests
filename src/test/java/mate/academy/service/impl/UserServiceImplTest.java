package mate.academy.service.impl;

import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private UserService userService;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private User expectedUser;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setEmail("bchupika@mate.academy");
        expectedUser.setPassword("12345678");
    }

    @Test
    void save_ok() {
        Mockito.when(passwordEncoder.encode(expectedUser.getPassword())).thenReturn(expectedUser.getPassword());
        Mockito.when(userDao.save(expectedUser)).thenReturn(expectedUser);
        User actual = userService.save(expectedUser);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expectedUser.getEmail(), actual.getEmail());
        Assertions.assertEquals(expectedUser.getPassword(), actual.getPassword());
    }

    @Test
    void findById_ok() {
        Mockito.when(userDao.findById(expectedUser.getId())).thenReturn(Optional.of(expectedUser));
        Optional<User> actual = userService.findById(expectedUser.getId());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expectedUser.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(expectedUser.getPassword(), actual.get().getPassword());
    }

    @Test
    void findByEmail_ok() {
        Mockito.when(userDao.findByEmail(expectedUser.getEmail())).thenReturn(Optional.of(
                expectedUser));
        Optional<User> actual = userService.findByEmail(expectedUser.getEmail());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expectedUser.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(expectedUser.getPassword(), actual.get().getPassword());
    }
}

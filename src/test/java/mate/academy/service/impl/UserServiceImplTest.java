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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {

    private User userIn;
    private User userOut;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userDao, passwordEncoder);
        String email = "bob@i.ua";
        String password = "1q@w#edfr4";
        userIn = new User();
        userIn.setEmail(email);
        userIn.setPassword(password);
        userIn.setRoles(Set.of(new Role(Role.RoleName.USER)));
        userOut = new User();
        userOut.setId(1L);
        userOut.setEmail(email);
        userOut.setPassword(password);
        userOut.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void save_user_OK() {
        Mockito.when(userDao.save(userIn)).thenReturn(userOut);
        User actual = userService.save(userIn);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, userOut);
    }

    @Test
    void findById_id_OK() {
        Mockito.when(userDao.findById(userOut.getId())).thenReturn(Optional.of(userOut));
        Optional<User> actual = userService.findById(userOut.getId());
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(actual.get(), userOut);
    }

    @Test
    void findByEmail_OK() {
        Mockito.when(userDao.findByEmail(userOut.getEmail())).thenReturn(Optional.of(userOut));
        Optional<User> actual = userService.findByEmail(userOut.getEmail());
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(actual.get(), userOut);
    }
}

package mate.academy.service.impl;

import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.dao.impl.AbstractTest;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest extends AbstractTest {
    private static final String TEST_LOGIN = "bob@i.ua";
    private static final String TEST_PASSWORD = "1234";

    private UserService userService;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class, Role.RoleName.class};
    }

    @BeforeEach
    void setUp() {
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(TEST_PASSWORD);
        userDao = new UserDaoImpl(getSessionFactory());
        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @Test
    void save_Ok() {
        User bob = new User();
        bob.setEmail(TEST_LOGIN);
        bob.setPassword(TEST_PASSWORD);
        User actual = userService.save(bob);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findById_Ok() {
        User bob = new User();
        bob.setEmail(TEST_LOGIN);
        bob.setPassword(TEST_PASSWORD);
        userService.save(bob);
        Optional<User> actual = userService.findById(1L);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(TEST_LOGIN, actual.get().getEmail());
    }

    @Test
    void findByEmail_Ok() {
        User bob = new User();
        bob.setEmail(TEST_LOGIN);
        bob.setPassword(TEST_PASSWORD);
        userService.save(bob);
        Optional<User> actual = userService.findByEmail(TEST_LOGIN);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(1L, actual.get().getId());
        Assertions.assertEquals(TEST_PASSWORD, actual.get().getPassword());
    }
}

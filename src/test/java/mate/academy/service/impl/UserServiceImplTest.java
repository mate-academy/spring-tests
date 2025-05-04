package mate.academy.service.impl;

import java.util.Optional;
import mate.academy.AbstractTest;
import mate.academy.dao.UserDao;
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
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private String password;
    private String email;
    private User bobby;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        UserDao userDao = new UserDaoImpl(getSessionFactory());
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao,passwordEncoder);
        password = "Holoborodko";
        email = "bobby@president.gov.ua";
        bobby = new User();
        bobby.setEmail(email);
        bobby.setPassword(password);
    }

    @Test
    void save_Ok() {
        Mockito.when(passwordEncoder.encode(password)).thenReturn(password);
        User actual = userService.save(bobby);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(email, actual.getEmail());
        Assertions.assertEquals(password, actual.getPassword());
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(passwordEncoder.encode(password)).thenReturn(password);
        userService.save(bobby);
        Optional<User> actual = userService.findByEmail(email);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(1L, actual.get().getId());
        Assertions.assertEquals(password, actual.get().getPassword());
    }

    @Test
    void findById_Ok() {
        Mockito.when(passwordEncoder.encode(password)).thenReturn(password);
        userService.save(bobby);
        Optional<User> actual = userService.findById(1L);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(email, actual.get().getEmail());
        Assertions.assertEquals(password, actual.get().getPassword());
    }
}
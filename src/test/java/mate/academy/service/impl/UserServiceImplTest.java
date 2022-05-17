package mate.academy.service.impl;

import java.util.Optional;
import java.util.Set;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest extends AbstractTest {
    private UserDao userDao;
    private UserService userService;
    private PasswordEncoder encoder;
    private User expected;
    private static final String CORRECT_EMAIL = "rita.jones@gmail.com";
    private static final String PASSWORD = "454poiDFG";
    private static final Long CORRECT_ID = 1L;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDaoImpl.class);
        encoder = Mockito.mock(BCryptPasswordEncoder.class);
        userService = new UserServiceImpl(userDao, encoder);

        expected = new User();
        expected.setId(CORRECT_ID);
        expected.setEmail(CORRECT_EMAIL);
        expected.setPassword(PASSWORD);
        expected.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @Test
    void save_Ok() {
        Mockito.when(encoder.encode(PASSWORD)).thenReturn(PASSWORD);
        Mockito.when(userDao.save(expected)).thenReturn(expected);

        User actual = userService.save(expected);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getPassword(), actual.getPassword());
        Assertions.assertEquals(expected.getRoles(), actual.getRoles());
    }

    @Test
    void findById_Ok() {
        Mockito.when(userService.findById(CORRECT_ID)).thenReturn(Optional.of(expected));

        Optional<User> actualOptional = userService.findById(CORRECT_ID);

        if (actualOptional.isEmpty()) {
            Assertions.fail("Optional must be present");
        }
        User actual = actualOptional.get();
        Assertions.assertEquals(CORRECT_ID, actual.getId());
        Assertions.assertEquals(CORRECT_EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(CORRECT_EMAIL)).thenReturn(Optional.of(expected));

        Optional<User> actualOptional = userService.findByEmail(CORRECT_EMAIL);

        if (actualOptional.isEmpty()) {
            Assertions.fail("Optional must be present");
        }
        User actual = actualOptional.get();
        Assertions.assertEquals(CORRECT_ID, actual.getId());
        Assertions.assertEquals(CORRECT_EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());    }
}

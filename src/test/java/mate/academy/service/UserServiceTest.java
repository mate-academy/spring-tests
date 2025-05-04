package mate.academy.service;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest extends AbstractTest {
    private static final String EMAIL = "alice@test.com";
    private static final String PASSWORD = "1234";
    private static UserDao userDao;
    private static RoleDao roleDao;
    private static User user;
    private static Role role;
    private static UserService userService;
    private static PasswordEncoder passwordEncoder;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(role));
    }

    @Test
    void save() {
        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findById_Ok() {
        User savedUser = userService.save(user);
        Optional<User> userById = userService.findById(savedUser.getId());
        Assertions.assertTrue(userById.isPresent());
        User actual = userById.get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findByEmail_Ok() {
        User savedUser = userService.save(user);
        Optional<User> userByEmail = userService.findByEmail(savedUser.getEmail());
        Assertions.assertFalse(userByEmail.isEmpty());
        User actual = userByEmail.get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findByEmail_Empty() {
        String email = "someMail@test.com";
        Optional<User> userByEmail = userService.findByEmail(email);
        Assertions.assertTrue(userByEmail.isEmpty());
    }

    @Test
    void findById_Empty() {
        Long id = 2L;
        User actual = userService.save(user);
        Assertions.assertNotEquals(id, actual.getId());
    }
}

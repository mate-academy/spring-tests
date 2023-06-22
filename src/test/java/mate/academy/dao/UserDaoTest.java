package mate.academy.dao;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest {
    private static final String EMAIL = "alice@test.com";
    private static final String PASSWORD = "1234";
    private static UserDao userDao;
    private static RoleDao roleDao;
    private static User user;
    private static Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(role));
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findByEmail_Ok() {
        User savedUser = userDao.save(user);
        Optional<User> userByEmail = userDao.findByEmail(savedUser.getEmail());
        Assertions.assertFalse(userByEmail.isEmpty());
        User actual = userByEmail.get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findByEmail_DataProcessingException() {
        String email = "someMail@test.com";
        Assertions.assertThrows(RuntimeException.class, () ->
                userDao.findByEmail(email).get());
    }

    @Test
    void findById_Ok() {
        User savedUser = userDao.save(user);
        Optional<User> userById = userDao.findById(savedUser.getId());
        Assertions.assertTrue(userById.isPresent());
        User actual = userById.get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());

    }
}

package mate.academy.dao;

import java.util.NoSuchElementException;
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
    private UserDao userDao;
    private RoleDao roleDao;
    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = roleDao.save(new Role(Role.RoleName.USER));
        userDao = new UserDaoImpl(getSessionFactory());
        user = new User();
        user.setEmail("bob@gmail.com");
        user.setPassword("12345678");
        user.setRoles(Set.of(role));
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, user);
    }

    @Test
    void findByEmail_Ok() {
        User expected = userDao.save(user);
        Optional<User> actual = userDao.findByEmail(user.getEmail());
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(actual.get().getEmail(), expected.getEmail());
        Assertions.assertEquals(actual.get().getId(), expected.getId());
    }

    @Test
    void findByEmail_InvalidEmail_NotOk() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userDao.findByEmail("qwerty").get(),
                "Expected to recieve NoSuchElementException");
    }
}

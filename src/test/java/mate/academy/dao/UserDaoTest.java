package mate.academy.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    private static final String EMAIL = "aboba@example.com";
    private static final String PASSWORD = "123456";
    private User user;
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        Role role = new Role(Role.RoleName.USER);
        RoleDao roleDao = new RoleDaoImpl(getSessionFactory());
        roleDao.save(role);
        user.setRoles(Set.of(role));
        userDao.save(user);
    }

    @Test
    void save_validUser_Ok() {
        assertNotNull(user);
        assertEquals(1L, user.getId());
    }

    @Test
    void findByEmail_validEmail_Ok() {
        Optional<User> actual = userDao.findByEmail(EMAIL);
        Assertions.assertFalse(actual.isEmpty());
        assertEquals(EMAIL, actual.get().getEmail());
    }

    @Test
    void findByEmail_invalidEmail_notOk() {
        Optional<User> actual = userDao.findByEmail("wdadawdawvefe");
        assertTrue(actual.isEmpty());
    }

    @Test
    void findById_validId_Ok() {
        Optional<User> actual = userDao.findById(1L);
        Assertions.assertFalse(actual.isEmpty());
        assertEquals(1L, actual.get().getId());
    }

    @Test
    void findById_invalidId_notOk() {
        Optional<User> actual = userDao.findById(999L);
        assertTrue(actual.isEmpty());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }
}

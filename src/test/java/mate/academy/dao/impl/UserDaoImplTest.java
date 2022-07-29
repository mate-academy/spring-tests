package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private UserDao userDao;
    private User user;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        RoleDao roleDao = new RoleDaoImpl(getSessionFactory());
        roleDao.save(new Role(Role.RoleName.USER));
        roleDao.save(new Role(Role.RoleName.ADMIN));
        user = new User();
        user.setEmail("user@i.ua");
        user.setPassword("2345");
        user.setRoles(Set.of(roleDao.getRoleByName(Role.RoleName.USER.name()).get()));
        userDao.save(user);
    }

    @Test
    void findByEmail_Ok() {
        Optional<User> actual = userDao.findByEmail(user.getEmail());
        assertNotNull(actual.get());
        assertEquals(user, actual.get());
    }

    @Test
    void findByEmail_Null_NotOk() {
        Optional<User> actual = userDao.findByEmail(null);
        assertTrue(actual.isEmpty());
    }

    @Test
    void findByEmail_NoSuchEmail_NotOk() {
        Optional<User> actual = userDao.findByEmail("bob@i.ua");
        assertTrue(actual.isEmpty());
    }
}

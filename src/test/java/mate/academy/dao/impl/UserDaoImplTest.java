package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final String USER_EMAIL = "bob@gmail.com";
    private static final String USER_PASSWORD = "1234";
    private UserDao userDao;
    private User user;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        user = new User();
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
        Role userRole = new Role(Role.RoleName.USER);
        RoleDao roleDao = new RoleDaoImpl(getSessionFactory());
        roleDao.save(userRole);
        user.setRoles(Set.of(userRole));
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(user);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
    }

    @Test
    void save_nullUser_NotOk() {
        assertThrows(RuntimeException.class,
                () -> userDao.save(null));
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(user.getEmail());
        assertNotNull(actual);
        assertEquals(user, actual.get());
    }

    @Test
    void findByEmail_emptyUser_NotOk() {
        Optional<User> actual = userDao.findByEmail(USER_EMAIL);
        assertEquals(Optional.empty(), actual);
    }

    @Test
    void findById_Ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findById(1L);
        assertNotNull(actual);
        assertEquals(user.getEmail(), actual.get().getEmail());
    }

    @Test
    void findById_emptyUser_NotOk() {
        Optional<User> actual = userDao.findById(1L);
        assertEquals(Optional.empty(), actual);
    }
}

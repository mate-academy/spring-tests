package mate.academy.dao.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoImplTest extends AbstractTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String INVALID_EMAIL = "alice@i.ua";
    private static final String PASSWORD = "1234";
    private static final Long ID = 1L;
    private static final Long INVALID_ID = 2L;
    private UserDao userDao;
    private RoleDao roleDao;
    private User user;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role();
        role.setRoleName(Role.RoleName.USER);
        roleDao.save(role);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(role));
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(user);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(EMAIL);
        assertTrue(actual.isPresent());
        assertEquals(EMAIL, actual.get().getEmail());
    }

    @Test
    void findByEmail_InvalidEmail_NotOk() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(INVALID_EMAIL);
        assertFalse(actual.isPresent());
    }

    @Test
    void findById_Ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findById(ID);
        assertTrue(actual.isPresent());
        assertEquals(ID, actual.get().getId());
    }

    @Test
    void findById_InvalidId_NotOk() {
        userDao.save(user);
        Optional<User> actual = userDao.findById(INVALID_ID);
        assertFalse(actual.isPresent());
    }
}

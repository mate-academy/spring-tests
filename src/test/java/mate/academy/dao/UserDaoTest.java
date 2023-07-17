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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest {
    private static final String VALID_EMAIL = "bob@i.ua";
    private static final String VALID_PASSWORD = "1234";
    private static final String INVALID_EMAIL = "alice@i.ua";
    private UserDao userDao;
    private RoleDao roleDao;
    private User testUser;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        testUser = new User();
        testUser.setEmail(VALID_EMAIL);
        testUser.setPassword(VALID_PASSWORD);
        Role role = roleDao.save(new Role(Role.RoleName.USER));
        testUser.setRoles(Set.of(role));
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(testUser);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
    }

    @Test
    void findByEmail_validEmail_Ok() {
        User actual = userDao.save(testUser);
        Optional<User> userByEmail = userDao.findByEmail(VALID_EMAIL);
        assertTrue(userByEmail.isPresent());
        assertEquals(actual.getEmail(), userByEmail.get().getEmail());
    }

    @Test
    void findByEmail_invalidEmail_isEmpty() {
        User actual = userDao.save(testUser);
        Optional<User> userByEmail = userDao.findByEmail(INVALID_EMAIL);
        assertTrue(userByEmail.isEmpty());
    }

}

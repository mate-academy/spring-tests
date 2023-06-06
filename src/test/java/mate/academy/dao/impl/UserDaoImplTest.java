package mate.academy.dao.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.Role.RoleName;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final String VALID_EMAIL = "bob@i.ua";
    private static final String INVALID_EMAIL = "alice@i.ua";
    private static final String VALID_PASSWORD = "1234";
    private UserDao userDao;
    private RoleDao roleDao;
    private User testUser;

    @Override
    protected Class<?>[] entities() {
        return new Class[] { User.class, Role.class };
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        testUser = new User();
        testUser.setEmail(VALID_EMAIL);
        testUser.setPassword(VALID_PASSWORD);
        Role role = new Role(RoleName.USER);
        testUser.setRoles(Set.of(roleDao.save(role)));
    }

    @Test
    void save_ok() {
        User actual = userDao.save(testUser);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findByEmail_existingEmail_ok() {
        userDao.save(testUser);
        User actual = userDao.findByEmail(VALID_EMAIL).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(testUser.getEmail(), actual.getEmail());
    }

    @Test
    void findByEmail_notExistingEmail_isNotPresent() {
        userDao.save(testUser);
        Optional<User> actual = userDao.findByEmail(INVALID_EMAIL);
        Assertions.assertFalse(actual.isPresent());
        Assertions.assertThrows(NoSuchElementException.class, 
                () -> userDao.findByEmail(INVALID_EMAIL).get());
    }

    @Test
    void findById_ok() {
        userDao.save(testUser);
        User actual = userDao.findById(1L).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(testUser.getEmail(), actual.getEmail());
    }

    @Test
    void findById_notExistId_emptyOptional() {
        Optional<User> actual = userDao.findById(0L);
        Assertions.assertFalse(actual.isPresent());
        Assertions.assertThrows(NoSuchElementException.class, 
                () -> userDao.findById(0L).get());
    }
}

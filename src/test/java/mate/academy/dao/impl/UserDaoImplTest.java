package mate.academy.dao.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final String USER_ROLE = "USER";
    private static final String ADMIN_ROLE = "ADMIN";
    private UserDao userDao;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleDao.save(new Role(Role.RoleName.USER));
        roleDao.save(new Role(Role.RoleName.ADMIN));
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @Test
    void save_Ok() {
        User expectedBob = new User();
        expectedBob.setEmail("bob@i.ua");
        expectedBob.setPassword("1234");
        expectedBob.setRoles(Set.of(roleDao.getRoleByName(USER_ROLE).get()));

        User expectedAlice = new User();
        expectedAlice.setEmail("alice@i.ua");
        expectedAlice.setPassword("1234");
        expectedAlice.setRoles(Set.of(roleDao.getRoleByName(ADMIN_ROLE).get()));

        User actualBob = userDao.save(expectedBob);
        expectedBob.setId(1L);
        User actualAlice = userDao.save(expectedAlice);
        expectedAlice.setId(2L);
        Assertions.assertNotNull(actualBob);
        Assertions.assertNotNull(actualAlice);

        Assertions.assertEquals(expectedBob, actualBob);
        Assertions.assertEquals(expectedAlice, actualAlice);
    }

    @Test
    void save_Null_NotOk() {
        User bob = null;
        Assertions.assertThrows(DataProcessingException.class, () -> userDao.save(bob));
    }

    @Test
    void save_NotOk_DataProcessingException() {
        User bob = null;
        try {
            userDao.save(bob);
        } catch (Exception e) {
            Assertions.assertEquals("Can't create entity: " + bob, e.getMessage());
            return;
        }
        Assertions.fail();
    }

    @Test
    void findByEmail_Ok() {
        User bob = new User();
        bob.setEmail("bob@i.ua");
        bob.setPassword("1234");
        bob.setRoles(Set.of(roleDao.getRoleByName(USER_ROLE).get()));
        Optional<User> savedBob = Optional.of(userDao.save(bob));

        Optional<User> actual = userDao.findByEmail(bob.getEmail());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(savedBob, actual);
    }

    @Test
    void findByEmail_EmptyUser() {
        Optional<User> actual = userDao.findByEmail("bob");

        Assertions.assertEquals(Optional.empty(), actual);
    }

    @Test
    void findById_Ok() {
        User bob = new User();
        bob.setEmail("bob@i.ua");
        bob.setPassword("1234");
        bob.setRoles(Set.of(roleDao.getRoleByName(USER_ROLE).get()));
        Optional<User> savedBob = Optional.of(userDao.save(bob));

        Optional<User> actual = userDao.findById(1L);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(savedBob, actual);
    }

    @Test
    void findById_EmptyUser() {
        Optional<User> actual = userDao.findById(Long.MAX_VALUE);

        Assertions.assertEquals(Optional.empty(), actual);
    }
}

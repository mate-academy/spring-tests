package mate.academy.dao;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest {
    private static final String USER_ROLE = "USER";
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String BOB_EMAIL = "bob@email.com";
    private static final String BOB_PASSWORD = "bob";
    private static final String ALICE_EMAIL = "alice@email.com";
    private static final String ALICE_PASSWORD = "alice";
    private UserDao userDao;
    private RoleDao roleDao;
    private User alice;
    private User bob;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleDao.save(new Role(Role.RoleName.USER));
        roleDao.save(new Role(Role.RoleName.ADMIN));
        bob = new User();
        alice = new User();
        bob.setEmail(BOB_EMAIL);
        bob.setPassword(BOB_PASSWORD);
        bob.setRoles(Set.of(roleDao.getRoleByName(USER_ROLE).get()));
        alice.setPassword(ALICE_PASSWORD);
        alice.setEmail(ALICE_EMAIL);
        alice.setRoles(Set.of(roleDao.getRoleByName(ADMIN_ROLE).get()));
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @Test
    void save_Ok() {
        User actualBob = userDao.save(bob);
        User actualAlice = userDao.save(alice);

        Assertions.assertNotNull(actualBob);
        Assertions.assertNotNull(actualAlice);
        Assertions.assertEquals(actualBob, bob);
        Assertions.assertEquals(actualAlice, alice);
    }

    @Test
    void save_Null_NotOk() {
        User user = null;
        Assertions.assertThrows(DataProcessingException.class, () -> userDao.save(user));
    }

    @Test
    void save_DataProcessingException_NotOk() {
        User user = null;
        try {
            userDao.save(user);
        } catch (Exception e) {
            Assertions.assertEquals("Can't create entity: " + user, e.getMessage());
            return;
        }
        Assertions.fail();
    }

    @Test
    void findByEmail_Ok() {
        User expectedBob = userDao.save(bob);
        User actualBob = userDao.findByEmail(bob.getEmail()).get();
        Assertions.assertNotNull(actualBob);
        Assertions.assertEquals(actualBob.toString(), expectedBob.toString());
    }

    @Test
    void findByEmail_Empty_Ok() {
        Optional<User> actual = userDao.findByEmail("null");
        Assertions.assertEquals(Optional.empty(), actual);
    }

    @Test
    void findById_Ok() {
        User savedBob = Optional.of(userDao.save(bob)).get();
        User actual = userDao.findById(1L).get();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(savedBob.toString(), actual.toString());
    }

    @Test
    void findById_EmptyUser() {
        Optional<User> actual = userDao.findById(99L);
        Assertions.assertEquals(Optional.empty(), actual);
    }

    @Test
    void findById_Null_NotOk() {
        Assertions.assertThrows(DataProcessingException.class, () -> userDao.findById(null));
    }
}

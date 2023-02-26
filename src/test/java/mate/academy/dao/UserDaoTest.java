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
    private static final String BOB_EMAIL = "bob@i.ua";
    private static final String BOB_PASSWORD = "123";
    private static final String ELLIS_EMAIL = "allis@i.ua";
    private static final String ELLIS_PASSWORD = "123";
    private static final Long CHECK_ID = 1L;
    private static final Long NULL_ID = null;
    private UserDao userDao;
    private User bob;
    private User elis;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        Role roleUser = new Role();
        roleUser.setRoleName(Role.RoleName.USER);
        RoleDao roleDao = new RoleDaoImpl(getSessionFactory());
        roleDao.save(roleUser);
        bob = new User();
        bob.setEmail(BOB_EMAIL);
        bob.setPassword(BOB_PASSWORD);
        bob.setRoles(Set.of(roleUser));
        elis = new User();
        elis.setEmail(ELLIS_EMAIL);
        elis.setPassword(ELLIS_PASSWORD);
        elis.setRoles(Set.of(roleUser));
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @Test
    void saveUser_Ok() {
        User actual = userDao.save(bob);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(CHECK_ID, actual.getId());
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(elis);
        userDao.save(bob);
        Optional<User> actual = userDao.findByEmail(ELLIS_EMAIL);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(elis.getEmail(), actual.get().getEmail());
    }

    @Test
    void findById_Ok() {
        userDao.save(elis);
        userDao.save(bob);
        Optional<User> actual = userDao.findById(CHECK_ID);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(CHECK_ID, actual.get().getId());
    }

    @Test
    void findByNullId_NotOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> userDao.findById(NULL_ID));
    }
}

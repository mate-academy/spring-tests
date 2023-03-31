package mate.academy.dao.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractDaoTest {
    private static final String TEST_EMAIL = "dima@gmail.com";
    private Role testRole;
    private User testUser;
    private UserDao userDao;
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        testRole = new Role(Role.RoleName.USER);
        testUser = new User();
        testUser.setEmail(TEST_EMAIL);
        testUser.setPassword("1234");
        roleDao = new RoleDaoImpl(getSessionFactory());
        userDao = new UserDaoImpl(getSessionFactory());
    }

    @Test
    void save_ok() {
        User actual = userDao.save(testUser);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findByEmail_Ok() {
        testUser.setRoles(Set.of(new Role(Role.RoleName.USER)));
        roleDao.save(new Role(Role.RoleName.USER));
        userDao.save(testUser);
        Optional<User> actual = userDao.findByEmail(TEST_EMAIL);
        Assertions.assertTrue(actual.isPresent(),
                String.format("Expect user by email: %s, but user was empty", TEST_EMAIL));
        Assertions.assertEquals(TEST_EMAIL, actual.get().getEmail(),
                String.format("Expect user by email: %s, but user was empty", TEST_EMAIL));
    }
}

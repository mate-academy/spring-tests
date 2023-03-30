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

class UserDaoImplTest extends AbstractTest {
    private static final String VALID_EMAIL = "bob@i.ua";
    private static final String INVALID_EMAIL = "alice@i.ua";
    private static final String VALID_PASSWORD = "12345678";
    private User user;
    private Role role;
    private RoleDao roleDao;
    private UserDao userDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        role = new Role(Role.RoleName.USER);
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleDao.save(role);
        userDao = new UserDaoImpl(getSessionFactory());
        user = new User();
        user.setEmail(VALID_EMAIL);
        user.setPassword(VALID_PASSWORD);
        user.setRoles(Set.of(role));
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(VALID_EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(VALID_EMAIL, actual.get().getEmail());
    }

    @Test
    void findByEmail_emailIsNotInDb_ok() {
        userDao.save(user);
        try {
            userDao.findByEmail(INVALID_EMAIL);
        } catch (Exception e) {
            Assertions.assertEquals("Couldn't get user by email: "
                    + INVALID_EMAIL, e.getMessage());
        }
    }
}

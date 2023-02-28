package mate.academy.dao;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final String USER_EMAIL = "bob@i.ua";
    private static final String NOT_EXIST_USER_EMAIL = "user@i.ua";
    private static final String USER_PASSWORD = "password";
    private static final Role USER_ROLE = new Role(Role.RoleName.USER);
    private UserDao userDao;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleDao.save(USER_ROLE);
        User bob = new User();
        bob.setEmail(USER_EMAIL);
        bob.setPassword(USER_PASSWORD);
        bob.setRoles(Set.of(USER_ROLE));
        userDao.save(bob);
    }

    @Test
    void findByEmail_Ok() {
        Optional<User> actual = userDao.findByEmail(USER_EMAIL);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(actual.get().getEmail(), USER_EMAIL);
    }

    @Test
    void findByEmail_UserNotFound_NotOk() {
        Optional<User> actual = userDao.findByEmail(NOT_EXIST_USER_EMAIL);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }
}

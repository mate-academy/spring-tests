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
    private static final String USER_EMAIL = "bob123@gmail.com";
    private static final String USER_PASSWORD = "bob123";
    private UserDao userDao;
    private User bob;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        bob = new User();
        bob.setEmail(USER_EMAIL);
        bob.setPassword(USER_PASSWORD);
        Role admin = new Role(Role.RoleName.ADMIN);
        RoleDao roleDao = new RoleDaoImpl(getSessionFactory());
        roleDao.save(admin);
        bob.setRoles(Set.of(admin));
    }

    @Test
    void saveUser_Ok() {
        User actual = userDao.save(bob);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(bob, actual);
    }

    @Test
    void saveUserNullValue_NotOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> userDao.save(null));
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(bob);
        Optional<User> actual = userDao.findByEmail(USER_EMAIL);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(bob, actual.get());
    }

    @Test
    void findById_Ok() {
        userDao.save(bob);
        Optional<User> actual = userDao.findById(1L);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(bob.getEmail(), actual.get().getEmail());
    }
}

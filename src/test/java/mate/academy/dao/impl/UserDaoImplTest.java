package mate.academy.dao.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final Role.RoleName ROLE_USER = Role.RoleName.USER;
    private UserDao userDao;
    private RoleDao roleDao;
    private User bob;
    private Role roleUser;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        userDao = new UserDaoImpl(getSessionFactory());
        roleUser = new Role(ROLE_USER);
        Role savedRole = roleDao.save(roleUser);
        bob = new User();
        bob.setEmail("bob@gmail.com");
        bob.setRoles(Set.of(savedRole));
        bob.setPassword("12345678");
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(bob);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findById_Ok() {
        userDao.save(bob);
        Optional<User> actual = userDao.findById(1L);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals("bob@gmail.com", actual.get().getEmail());
    }

    @Test
    void findById_NoSuchId() {
        userDao.save(bob);
        Optional<User> actual = userDao.findById(2L);
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(bob);
        Optional<User> actual = userDao.findByEmail(bob.getEmail());
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals("bob@gmail.com", actual.get().getEmail());
    }

    @Test
    void findByEmail_NoSuchEmail() {
        userDao.save(bob);
        Optional<User> actual = userDao.findByEmail("alice@gmail.com");
        Assertions.assertFalse(actual.isPresent());
    }
}

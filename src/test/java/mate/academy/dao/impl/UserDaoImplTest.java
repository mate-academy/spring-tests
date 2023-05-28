package mate.academy.dao.impl;

import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private UserDao userDao;
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        SessionFactory sessionFactory = getSessionFactory();
        userDao = new UserDaoImpl(sessionFactory);
        roleDao = new RoleDaoImpl(sessionFactory);
    }

    @Test
    void save_validUser_ok() {
        Role userRole = new Role(Role.RoleName.USER);
        userRole = roleDao.save(userRole);
        Role adminRole = new Role(Role.RoleName.ADMIN);
        adminRole = roleDao.save(adminRole);
        User expected = new User();
        expected.setEmail("user@gmail.com");
        expected.setPassword("password");
        expected.setRoles(Set.of(adminRole, userRole));
        User actual = userDao.save(expected);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
        Assertions.assertEquals(expected.getPassword(), actual.getPassword());
        Assertions.assertTrue(actual.getRoles().containsAll(expected.getRoles()));
    }

    @Test
    void findById_validId_ok() {
        Role userRole = new Role(Role.RoleName.USER);
        userRole = roleDao.save(userRole);
        Role adminRole = new Role(Role.RoleName.ADMIN);
        adminRole = roleDao.save(adminRole);
        User expected = new User();
        expected.setEmail("user@gmail.com");
        expected.setPassword("password");
        expected.setRoles(Set.of(adminRole, userRole));
        expected = userDao.save(expected);
        User actual = userDao.findById(expected.getId()).get();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void findById_notExistedId_ok() {
        Assertions.assertFalse(userDao.findById(0L).isPresent());
        Assertions.assertFalse(userDao.findById(100L).isPresent());
        Assertions.assertFalse(userDao.findById(1000L).isPresent());
    }

    @Test
    void findById_nullId_notOk() {
        Assertions.assertThrows(DataProcessingException.class, () -> userDao.findById(null));
    }

    @Test
    void findByEmail_validEmail_ok() {
        Role userRole = new Role(Role.RoleName.USER);
        userRole = roleDao.save(userRole);
        Role adminRole = new Role(Role.RoleName.ADMIN);
        adminRole = roleDao.save(adminRole);
        User expected = new User();
        String email = "user@gmail.com";
        expected.setEmail(email);
        expected.setPassword("password");
        expected.setRoles(Set.of(adminRole, userRole));
        expected = userDao.save(expected);
        User actual = userDao.findByEmail(email).get();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
    }

    @Test
    void findByEmail_invalidEmail_ok() {
        Assertions.assertFalse(userDao.findByEmail(null).isPresent());
        Assertions.assertFalse(userDao.findByEmail("notExistedEmail").isPresent());
    }
}

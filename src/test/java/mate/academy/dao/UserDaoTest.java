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

class UserDaoTest extends AbstractTest {
    private final static String USER_EMAIL = "bob@gmail.ua";
    private final static String USER_PASSWORD = "1234";
    private final static String ADMIN_EMAIL = "alica@gmail.ua";
    private final static String ADMIN_PASSWORD = "4321";
    private UserDao userDao;
    private User user;
    private User admin;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        RoleDao roleDao = new RoleDaoImpl(getSessionFactory());
        Role adminRole = new Role(Role.RoleName.ADMIN);
        Role userRole = new Role(Role.RoleName.USER);
        roleDao.save(adminRole);
        roleDao.save(userRole);

        user = new User();
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
        user.setRoles(Set.of(userRole));

        admin = new User();
        admin.setEmail(ADMIN_EMAIL);
        admin.setPassword(ADMIN_PASSWORD);
        admin.setRoles(Set.of(adminRole));
    }

    @Test
    void save_ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findByEmail_userData_ok() {
        userDao.save(user);
        User actual = userDao.findByEmail(USER_EMAIL).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findByEmail_adminData_ok() {
        userDao.save(admin);
        User actual = userDao.findByEmail(ADMIN_EMAIL).get();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findByEmail_incorrectData_notOk() {
        String incorrectEmail = "Incorrect@gmail.com";
        Optional<User> byEmail = userDao.findByEmail(incorrectEmail);
        Assertions.assertEquals(Optional.empty(), byEmail);
    }

    @Test
    void findById_ok() {
        Long id = 1L;
        userDao.save(user);
        User actual = userDao.findById(id).get();
        Assertions.assertNotNull(user);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findById_notExistId_notOk() {
        Long id = 100L;
        userDao.save(user);
        Optional<User> actual = userDao.findById(id);
        Optional<User> expected = Optional.empty();
        Assertions.assertEquals(expected, actual);
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{
                User.class, Role.class};
    }
}

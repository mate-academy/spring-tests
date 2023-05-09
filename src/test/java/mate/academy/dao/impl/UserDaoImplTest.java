package mate.academy.dao.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final long ADMIN_ROLE_ID = 1L;
    private static final long USER_ROLE_ID = 2L;
    private static final String ADMIN_EMAIL = "admin@me.com";
    private static final String ADMIN_PASSWORD = "12345678";
    private static final String USER_EMAIL = "user@me.com";
    private static final String USER_PASSWORD = "87654321";
    private static final String NON_EXIST_EMAIL = "mary@me.com";
    private static final long NOT_EXIST_ID = 7L;
    private User admin;
    private User user;
    private UserDao userDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class, User.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        RoleDao roleDao = new RoleDaoImpl(getSessionFactory());
        admin = new User();
        admin.setRoles(Set.of(roleDao.save(new Role(Role.RoleName.ADMIN))));
        admin.setEmail(ADMIN_EMAIL);
        admin.setPassword(ADMIN_PASSWORD);
        user = new User();
        user.setRoles(Set.of(roleDao.save(new Role(Role.RoleName.USER))));
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
    }

    @Test
    void save_Ok() {
        User actualAdmin = userDao.save(admin);
        User actualUser = userDao.save(user);
        Assertions.assertNotNull(actualAdmin);
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(ADMIN_ROLE_ID, actualAdmin.getId());
        Assertions.assertEquals(USER_ROLE_ID, actualUser.getId());
        Assertions.assertEquals(admin.getPassword(), actualAdmin.getPassword());
        Assertions.assertEquals(user.getPassword(), actualUser.getPassword());
    }

    @Test
    void findByEmail_existEmail_Ok() {
        User expected = userDao.save(admin);
        String existEmail = admin.getEmail();
        Optional<User> actual = userDao.findByEmail(existEmail);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected.getId(), actual.get().getId());
        Assertions.assertEquals(expected.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(expected.getPassword(), actual.get().getPassword());
        Set<Role.RoleName> expectedRoles = expected.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());
        Set<Role.RoleName> actualRoles = actual.get().getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());
        Assertions.assertTrue(expectedRoles.containsAll(actualRoles),
                "Roles are different");
    }

    @Test
    void findByEmail_notExistEmail_notOk() {
        userDao.save(admin);
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(NON_EXIST_EMAIL);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findByEmail_notExistEmail_Ok() {
        userDao.save(admin);
        userDao.save(user);
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userDao.findByEmail(NON_EXIST_EMAIL).get(),
                "Expected to receive NoSuchElementException");
    }

    @Test
    void findById_existId_Ok() {
        User expected = userDao.save(admin);
        long existId = admin.getId();
        Optional<User> actual = userDao.findById(existId);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected.getId(), actual.get().getId());
        Assertions.assertEquals(expected.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(expected.getPassword(), actual.get().getPassword());
    }

    @Test
    void findById_notExistId_notOk() {
        userDao.save(admin);
        userDao.save(user);
        Optional<User> actual = userDao.findById(NOT_EXIST_ID);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findById_notExistId_Ok() {
        userDao.save(admin);
        userDao.save(user);
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userDao.findById(NOT_EXIST_ID).get(),
                "Expected to receive NoSuchElementException");
    }
}

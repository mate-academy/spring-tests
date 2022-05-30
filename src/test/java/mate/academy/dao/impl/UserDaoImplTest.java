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
    private UserDao userDao;
    private RoleDao roleDao;
    private User admin;
    private User user;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());

        admin = new User();
        admin.setEmail("testUser_1@mail.com");
        admin.setPassword("testUser_1_1234");
        admin.setRoles(Set.of(roleDao.save(new Role(Role.RoleName.ADMIN))));

        user = new User();
        user.setEmail("testUser_2@mail.com");
        user.setPassword("testUser_2_1234");
        user.setRoles(Set.of(roleDao.save(new Role(Role.RoleName.USER))));
    }

    @Test
    void save_Ok() {
        User actualAdmin = userDao.save(admin);
        User actualUser = userDao.save(user);
        Assertions.assertNotNull(actualAdmin);
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(1L, actualAdmin.getId());
        Assertions.assertEquals(2L, actualUser.getId());
        Assertions.assertEquals(admin.getEmail(), actualAdmin.getEmail());
        Assertions.assertEquals(user.getEmail(), actualUser.getEmail());
        Assertions.assertEquals(admin.getPassword(), actualAdmin.getPassword());
        Assertions.assertEquals(user.getPassword(), actualUser.getPassword());
    }

    @Test
    void findByEmail_existEmail_Ok() {
        User expected = userDao.save(admin);
        String existEmail = admin.getEmail();
        Optional<User> actual = userDao.findByEmail(existEmail);
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(expected.getId(), actual.get().getId());
        Assertions.assertEquals(expected.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(expected.getPassword(), actual.get().getPassword());
        Assertions.assertEquals(expected.getRoles().size(), actual.get().getRoles().size(),
                "Number of roles is not equal");
        Set<Role.RoleName> expectedRoleNames = expected.getRoles().stream()
                .map(r -> r.getRoleName())
                .collect(Collectors.toSet());
        Set<Role.RoleName> actualRoleNames = actual.get().getRoles().stream()
                .map(r -> r.getRoleName())
                .collect(Collectors.toSet());
        Assertions.assertTrue(expectedRoleNames.containsAll(actualRoleNames), "Different set of role.");
    }

    @Test
    void findByEmail_notExistEmail_NotOk() {
        userDao.save(admin);
        userDao.save(user);
        String notExistEmail = "alice@i.ua";
        Optional<User> actual = userDao.findByEmail(notExistEmail);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findByEmail_notExistEmail_Ok() {
        userDao.save(admin);
        userDao.save(user);
        String notExistEmail = "alice@i.ua";
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userDao.findByEmail(notExistEmail).get(),
                "Expected NoSuchElementException while trying to get non-existent user by email.\n");
    }

    @Test
    void findById_existId_Ok() {
        User expected = userDao.save(admin);
        Long existId = admin.getId();
        Optional<User> actual = userDao.findById(existId);
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(expected.getId(), actual.get().getId());
        Assertions.assertEquals(expected.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(expected.getPassword(), actual.get().getPassword());
    }

    @Test
    void findById_notExistId_NotOk() {
        userDao.save(admin);
        userDao.save(user);
        Long notExistId = 3L;
        Optional<User> actual = userDao.findById(notExistId);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findById_notExistId_Ok() {
        userDao.save(admin);
        userDao.save(user);
        Long notExistId = 3L;
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userDao.findById(notExistId).get(),
                "Expected NoSuchElementException while trying to get non-existent user by id.\n");
    }
}

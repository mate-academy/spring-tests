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
        admin.setEmail("email@i.ua");
        admin.setPassword("12345678");
        admin.setRoles(Set.of(roleDao.save(new Role(Role.RoleName.ADMIN))));

        user = new User();
        user.setEmail("email2@i.ua");
        user.setPassword("12345678");
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
        Assertions.assertTrue(expectedRoleNames.containsAll(actualRoleNames), 
                "Different set of role.");
    }

    @Test
    void findByEmail_notExistEmail_NotOk() {
        userDao.save(admin);
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail("alice@i.ua");
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findByEmail_notExistEmail_Ok() {
        userDao.save(admin);
        userDao.save(user);
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userDao.findByEmail("alise@i.ua").get(),
                "Expected NoSuchElementException");
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
                "Expected NoSuchElementException");
    }
}

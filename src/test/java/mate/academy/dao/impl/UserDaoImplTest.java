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
    private User testUser_1;
    private User testUser_2;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());

        testUser_1 = new User();
        testUser_1.setEmail("testUser_1@mail.com");
        testUser_1.setPassword("testUser_1_1234");
        testUser_1.setRoles(Set.of(roleDao.save(new Role(Role.RoleName.ADMIN))));

        testUser_2 = new User();
        testUser_2.setEmail("testUser_2@mail.com");
        testUser_2.setPassword("testUser_2_1234");
        testUser_2.setRoles(Set.of(roleDao.save(new Role(Role.RoleName.USER))));
    }

    @Test
    void save_Ok() {
        User actual_1 = userDao.save(testUser_1);
        User actual_2 = userDao.save(testUser_2);
        Assertions.assertNotNull(actual_1);
        Assertions.assertNotNull(actual_2);
        Assertions.assertEquals(1L, actual_1.getId());
        Assertions.assertEquals(2L, actual_2.getId());
        Assertions.assertEquals(testUser_1.getEmail(), actual_1.getEmail());
        Assertions.assertEquals(testUser_2.getEmail(), actual_2.getEmail());
        Assertions.assertEquals(testUser_1.getPassword(), actual_1.getPassword());
        Assertions.assertEquals(testUser_2.getPassword(), actual_2.getPassword());
    }

    @Test
    void findByEmail_existEmail_Ok() {
        User expected = userDao.save(testUser_1);
        String existEmail = testUser_1.getEmail();
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
        userDao.save(testUser_1);
        userDao.save(testUser_2);
        String notExistEmail = "alice@i.ua";
        Optional<User> actual = userDao.findByEmail(notExistEmail);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findByEmail_notExistEmail_Ok() {
        userDao.save(testUser_1);
        userDao.save(testUser_2);
        String notExistEmail = "alice@i.ua";
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userDao.findByEmail(notExistEmail).get(),
                "Expected NoSuchElementException while trying to get non-existent user by email.\n");
    }

    @Test
    void findById_existId_Ok() {
        User expected = userDao.save(testUser_1);
        Long existId = testUser_1.getId();
        Optional<User> actual = userDao.findById(existId);
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(expected.getId(), actual.get().getId());
        Assertions.assertEquals(expected.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(expected.getPassword(), actual.get().getPassword());
    }

    @Test
    void findById_notExistId_NotOk() {
        userDao.save(testUser_1);
        userDao.save(testUser_2);
        Long notExistId = 3L;
        Optional<User> actual = userDao.findById(notExistId);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findById_notExistId_Ok() {
        userDao.save(testUser_1);
        userDao.save(testUser_2);
        Long notExistId = 3L;
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userDao.findById(notExistId).get(),
                "Expected NoSuchElementException while trying to get non-existent user by id.\n");
    }
}

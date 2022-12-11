package mate.academy.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private AbstractDao<User, Long> userAbstractDao;
    private UserDao userDao;
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userAbstractDao = new UserDaoImpl(getSessionFactory());
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    private List<User> injectUsers() {
        Role user = new Role(Role.RoleName.USER);
        Role admin = new Role(Role.RoleName.ADMIN);
        roleDao.save(user);
        roleDao.save(admin);
        User bob = new User();
        bob.setEmail("bob@i.ua");
        bob.setPassword("1234");
        bob.setRoles(Set.of(user));
        userAbstractDao.save(bob);
        User tom = new User();
        tom.setEmail("tom@i.ua");
        tom.setPassword("5678");
        tom.setRoles(Set.of(admin));
        userAbstractDao.save(tom);
        return List.of(bob, tom);
    }

    @Test
    void saveUser_Ok() {
        User bob = new User();
        bob.setEmail("bob@i.ua");
        bob.setPassword("1234");
        Role user = new Role(Role.RoleName.USER);
        roleDao.save(user);
        bob.setRoles(Set.of(user));
        User actual = userAbstractDao.save(bob);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findAllUsers_Ok() {
        List<User> expectedUsers = injectUsers();
        List<User> actualUsers = userAbstractDao.findAll();
        Assertions.assertNotNull(actualUsers);
        Assertions.assertArrayEquals(expectedUsers.stream().map(User::toString).toArray(),
                actualUsers.stream().map(User::toString).toArray());
    }

    @Test
    void findAllNotExistedUsers_EmptyList() {
        List<User> expectedUsers = new ArrayList<>();
        List<User> actualUsers = userAbstractDao.findAll();
        Assertions.assertNotNull(actualUsers);
        Assertions.assertEquals(expectedUsers, actualUsers);
    }

    @Test
    void findUserById_Ok() {
        injectUsers();
        Long id = 2L;
        Optional<User> actual = userAbstractDao.findById(id);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(2L, actual.get().getId());
    }

    @Test
    void findUserByNotExistedId_Ok() {
        injectUsers();
        Long id = 3L;
        Optional<User> actual = userAbstractDao.findById(id);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void deleteUserById_Ok() {
        injectUsers();
        Long id = 1L;
        userAbstractDao.delete(id);
        Optional<User> actual = userAbstractDao.findById(id);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void deleteUserByNotExistedId_DataProcessingException() {
        injectUsers();
        Long id = 3L;
        try {
            userAbstractDao.delete(id);
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Can't delete entity by id: " + id, e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }

    @Test
    void updateUser_Ok() {
        injectUsers();
        User bob = new User();
        bob.setId(1L);
        bob.setEmail("bob@i.ua");
        bob.setPassword("1234");
        User actual = userAbstractDao.update(bob);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(bob, actual);
        Assertions.assertEquals(bob.toString(), userAbstractDao.findById(1L).get().toString());
    }

    @Test
    void findUserByEmail_Ok() {
        List<User> users = injectUsers();
        String email = "bob@i.ua";
        Optional<User> actual = userDao.findByEmail(email);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(users.get(0), actual.get());
    }
}

package mate.academy.dao;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest {
    private static final String TEST_EMAIL = "alice@i.ua";
    private static final Long USER_ID = 1L;
    private UserDao userDao;
    private RoleDao roleDao;
    private User user;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        Role role = roleDao.save(new Role(Role.RoleName.USER));
        user = new User();
        user.setEmail(TEST_EMAIL);
        user.setRoles(Set.of(role));
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(USER_ID, actual.getId(),
                String.format("Expected id of user should be %s, but was %s",
                        USER_ID, actual.getId()));
        Assertions.assertEquals(TEST_EMAIL, actual.getEmail(),
                String.format("Expected email should be %s, but was %s",
                        TEST_EMAIL, actual.getEmail()));
    }

    @Test
    void findByEmail_ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(TEST_EMAIL);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(USER_ID, actual.get().getId(),
                String.format("Expected id of user should be %s, but was %s",
                        USER_ID, actual.get().getId()));
        Assertions.assertEquals(TEST_EMAIL, actual.get().getEmail(),
                String.format("Expected email should be %s, but was %s",
                        TEST_EMAIL, actual.get().getEmail()));
    }

    @Test
    void findByEmail_nonExistedEmail_notOk() {
        userDao.save(user);
        Assertions.assertThrows(NoSuchElementException.class, () ->
                        userDao.findByEmail("nonExisted_email").get(),
                "Expected NoSuchElementException to be thrown for non-existed email");
    }

    @Test
    void findById_ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findById(1L);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(USER_ID, actual.get().getId(),
                String.format("Expected id of user should be %s, but was %s.",
                        USER_ID, actual.get().getId()));
        Assertions.assertEquals(TEST_EMAIL, actual.get().getEmail(),
                String.format("Expected email of user should be %s, but was %s.",
                        TEST_EMAIL, actual.get().getEmail()));
    }

    @Test
    void findById_nullId_notOk() {
        userDao.save(user);
        Assertions.assertThrows(DataProcessingException.class, () ->
                userDao.findById(null),
                "Expected DataProcessingException to be thrown for null input");
    }

    @Test
    void findById_nonExistedId_notOk() {
        userDao.save(user);
        Assertions.assertThrows(NoSuchElementException.class, () ->
                userDao.findById(99L).get(),
                "Expected NoSuchElementException to be thrown for non-existed id");
    }
}

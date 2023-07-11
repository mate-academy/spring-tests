package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "1234";
    private UserDao userDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
    }

    @Test
    void save_Ok() {
        User actual = createUser();
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void save_invalidUser_Ok() {
        Assertions.assertThrows(DataProcessingException.class, () -> userDao.save(null));
    }

    @Test
    void findById_invalidId_Ok() {
        Assertions.assertEquals(Optional.empty(), userDao.findById(1L));
    }

    @Test
    void findById_validId_Ok() {
        User expected = createUser();
        Optional<User> actual = userDao.findById(expected.getId());
        Assertions.assertEquals(expected.getId(), actual.get().getId());
        Assertions.assertEquals(expected.getEmail(), actual.get().getEmail());
    }

    @Test
    void findByEmail_validEmail_Ok() {
        User expected = createUser();
        Optional<User> actual = userDao.findById(expected.getId());
        Assertions.assertEquals(expected.getEmail(), actual.get().getEmail());

    }

    @Test
    void findByEmail_invalidEmail_Ok() {
        Assertions.assertEquals(Optional.empty(), userDao.findByEmail(""));
    }

    private User createUser() {
        User bob = new User();
        bob.setEmail(EMAIL);
        bob.setPassword(PASSWORD);
        return userDao.save(bob);
    }
}

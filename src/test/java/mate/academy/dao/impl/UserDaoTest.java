package mate.academy.dao.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest {
    private UserDao userDao;
    private User user;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        user = new User();
        user.setEmail("bchupika@mate.academy");
        user.setPassword("12345678");
        user.setRoles(Set.of(
                new RoleDaoImpl(getSessionFactory())
                        .save(new Role(Role.RoleName.USER))));
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
        Assertions.assertEquals(1, actual.getRoles().size());
    }

    @Test
    void save_userIsNull_notOk() {
        Assertions.assertThrows(DataProcessingException.class, () -> userDao.save(null));
    }

    @Test
    void findByEmail_emailIsNull_ok() {
        Optional<User> actual = userDao.findByEmail(null);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findByEmail_userNotFound_ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail("notfound@mail.com");
        Assertions.assertTrue(actual.isEmpty());
    }
}

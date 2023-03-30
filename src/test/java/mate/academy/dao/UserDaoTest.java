package mate.academy.dao;

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
    private static final String EMAIL = "bob@i.ua";
    private static final String EMAIL_IS_NOT_IN_DB = "alice@i.ua";
    private static final Long ID = 1L;
    private static final Long ID_IS_NOT_IN_DB = 2L;
    private static final String PASSWORD = "1234";
    private UserDao userDao;
    private User user;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        RoleDao roleDao = new RoleDaoImpl(getSessionFactory());
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        user = new User();
        user.setRoles(Set.of(role));
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
    }

    @Test
    void save_ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findByEmail_ok() {
        userDao.save(user);
        Optional<User> optionalUser = userDao.findByEmail(EMAIL);
        Assertions.assertTrue(optionalUser.isPresent());
        User actual = optionalUser.get();
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void findByEmail_emailIsNotInDb_ok() {
        try {
            userDao.findByEmail(EMAIL_IS_NOT_IN_DB);
        } catch (Exception e) {
            Assertions.assertThrows(DataProcessingException.class,
                    () -> userDao.findByEmail(EMAIL_IS_NOT_IN_DB));
        }
    }
}

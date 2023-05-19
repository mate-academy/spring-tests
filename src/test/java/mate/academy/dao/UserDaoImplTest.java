package mate.academy.dao;

import java.util.Optional;
import java.util.Set;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final String EMAIL = "user@i.ua";
    private static final String PASSWORD = "user1234";
    private UserDaoImpl userDao;
    private Role userRole;
    private User user;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        RoleDao roleDao = new RoleDaoImpl(getSessionFactory());
        userRole = roleDao.save(new Role(Role.RoleName.USER));
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(userRole));
    }

    @Test
    void save_isSuccessful_ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
        Assertions.assertTrue(actual.getRoles().contains(userRole));
    }

    @Test
    void save_nullUser_notOk() {
        Assertions.assertThrows(DataProcessingException.class, () -> userDao.save(null),
                "Expected DataProcessingException");
    }

    @Test
    void findByEmail_isSuccessful_ok() {
        userDao.save(user);
        Optional<User> userOptional = userDao.findByEmail(EMAIL);
        Assertions.assertTrue(userOptional.isPresent());
        User actual = userOptional.get();
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void findByEmail_userIsNotInDB_notOk() {
        Assertions.assertFalse(userDao.findByEmail(EMAIL).isPresent());
    }
}

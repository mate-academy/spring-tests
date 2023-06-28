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
    private static final String EMAIL = "bob@gmail.com";
    private static final String PASSWORD = "1234";
    private static final Long ID = 1L;
    private UserDaoImpl userDao;
    private Role userRole;
    private User userBob;
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        userRole = roleDao.save(new Role(Role.RoleName.USER));
        userBob = new User();
        userBob.setEmail(EMAIL);
        userBob.setPassword(PASSWORD);
        userBob.setRoles(Set.of(userRole));
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(userBob);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(ID, actual.getId());
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
    void findByEmail_Ok() {
        userDao.save(userBob);
        Optional<User> userOptional = userDao.findByEmail(EMAIL);
        Assertions.assertTrue(userOptional.isPresent());
        User actual = userOptional.get();
        Assertions.assertEquals(ID, actual.getId());
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void findByEmail_userAbsent_notOk() {
        Assertions.assertFalse(userDao.findByEmail(EMAIL).isPresent());
    }
}

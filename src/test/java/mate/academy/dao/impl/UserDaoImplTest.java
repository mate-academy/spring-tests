package mate.academy.dao.impl;

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
    private UserDao userDao;
    private RoleDao roleDao;
    private User user;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());

        String email = "alice@com.ua";
        user = new User();
        user.setEmail(email);
        user.setPassword("1234");
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        user.setRoles(Set.of(role));
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void findEmail_Ok() {
        userDao.save(user);
        Optional<User> optionalUser = userDao.findByEmail("alice@com.ua");
        Assertions.assertTrue(optionalUser.isPresent());
        User actual = optionalUser.get();
        Assertions.assertEquals("alice@com.ua", actual.getEmail());
        Assertions.assertEquals("1234", actual.getPassword());
    }

    @Test
    void findEmail_emailNotFound_notOk() {
        try {
            userDao.findByEmail("bob@com.ua");
        } catch (Exception e) {
            Assertions.assertThrows(DataProcessingException.class,
                    () -> userDao.findByEmail("bob@com.ua"));
        }
    }
}

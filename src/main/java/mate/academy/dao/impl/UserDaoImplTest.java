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

public class UserDaoImplTest extends AbstractTest {
    private static final String CORRECT_EMAIL = "bob@i.ua";
    private static final String WRONG_EMAIL = "else@i.ua";
    private static final String PASSWORD = "1234";
    private User user;
    private Role role;
    private RoleDao roleDao;
    private UserDao userDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role();
        role.setRoleName(Role.RoleName.USER);
        roleDao.save(role);
        User user = new User();
        user.setEmail("bob@i.ua");
        user.setPassword("1234");
        user.setRoles(Set.of(role));
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(CORRECT_EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void findById_UserExists_Ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findById(1L);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(CORRECT_EMAIL, actual.get().getPassword());
        Assertions.assertEquals(PASSWORD, actual.get().getPassword());
    }

    @Test
    void findById_UserNotExists_NotOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> userDao.findById(2L));
    }

    @Test
    void findByEmail_UserExists_Ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(CORRECT_EMAIL);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(1L, actual.get().getId());
        Assertions.assertEquals(CORRECT_EMAIL, actual.get().getEmail());
        Assertions.assertEquals(PASSWORD, actual.get().getPassword());
    }

    @Test
    void findByEmail_UserNotExists_Ok() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> userDao.findByEmail(WRONG_EMAIL));
    }
}

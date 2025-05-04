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
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "1234";
    private static final String WRONG_EMAIL = "abc@i.ua";
    private static final Long USER_ID = 1L;
    private UserDao userDao;
    private User bob;
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        bob = new User();
        bob.setEmail(EMAIL);
        bob.setPassword(PASSWORD);
        bob.setRoles(Set.of(role));
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(bob);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(USER_ID, actual.getId());
        Assertions.assertEquals(EMAIL, actual.getEmail());
        Assertions.assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void save_UserIsNull_NotOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> userDao.save(null));
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(bob);
        Optional<User> optionalUser = userDao.findByEmail(EMAIL);
        Assertions.assertTrue(optionalUser.isPresent());
        Assertions.assertEquals(EMAIL, optionalUser.get().getEmail());
    }

    @Test
    void findByEmail_IncorrectEmail_NotOk() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userDao.findByEmail(WRONG_EMAIL).get());
    }

    @Test
    void findById_Ok() {
        userDao.save(bob);
        Optional<User> optionalUser = userDao.findById(bob.getId());
        Assertions.assertTrue(optionalUser.isPresent());
        Assertions.assertEquals(bob.getId(), optionalUser.get().getId());
    }

    @Test
    void findUserById_NullId_NotOk() {
        Assertions.assertThrows(DataProcessingException.class, () ->
                userDao.findById(null));
    }
}

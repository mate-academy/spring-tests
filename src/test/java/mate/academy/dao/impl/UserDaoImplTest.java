package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final String TEST_EMAIL_OK = "artem@gmail.com";
    private static final String TEST_PASSWORD_OK = "12345678";
    private UserDao userDao;
    private RoleDao roleDao;
    private Role role;
    private User user;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        role = new Role(Role.RoleName.USER);
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleDao.save(role);
        user = new User();
        user.setEmail(TEST_EMAIL_OK);
        user.setPassword(TEST_PASSWORD_OK);
        user.setRoles(Set.of(role));
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(TEST_EMAIL_OK);
        assertTrue(actual.isPresent());
        assertEquals(user.getPassword(), actual.get().getPassword());
        assertEquals(user.getEmail(), actual.get().getEmail());
        assertEquals(user.getId(), actual.get().getId());
    }

    @Test
    void findByEmail_IsIncorrectEmail_NotOk() {
        assertThrows(NoSuchElementException.class,
                () -> userDao.findByEmail("Wrong email").get());
    }

    @Test
    void saveUser_ok() {
        User actual = userDao.save(user);
        assertNotNull(actual);
        assertEquals(user.getEmail(), actual.getEmail());
        assertEquals(user.getPassword(), actual.getPassword());
        assertEquals(1L, actual.getId());
    }

    @Test
    void saveUser_Not_Ok() {
        assertThrows(DataProcessingException.class,() ->
                userDao.save(null));
    }

    @Test
    void findUserById_Ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findById(user.getId());
        assertTrue(actual.isPresent());
        assertEquals(user.getPassword(), actual.get().getPassword());
        assertEquals(user.getEmail(), actual.get().getEmail());
        assertEquals(user.getId(), actual.get().getId());
    }

    @Test
    void findUserById_Not_Ok() {
        assertThrows(DataProcessingException.class, () ->
                userDao.findById(null));
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }
}

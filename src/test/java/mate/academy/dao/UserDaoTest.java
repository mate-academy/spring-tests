package mate.academy.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "1234";
    private static final String NOT_EXISTED_EMAIL = "alice@i.ua";
    private UserDao userDao;
    private RoleDao roleDao;
    private User user;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        role = new Role(Role.RoleName.ADMIN);
        roleDao.save(role);
        user.setRoles(Set.of(role));
    }

    @Test
    void save_ok() {
        User actual = userDao.save(user);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
        assertEquals(EMAIL, actual.getEmail());
        assertEquals(PASSWORD, actual.getPassword());
    }

    @Test
    void save_DataProcessingException() {
        assertThrows(DataProcessingException.class, () -> userDao.save(null));
    }

    @Test
    void findByEmail_existedUser_ok() {
        userDao.save(user);
        Optional<User> expected = userDao.findByEmail(EMAIL);
        assertTrue(expected.isPresent());
        Optional<User> actual = userDao.findByEmail(expected.get().getEmail());
        assertTrue(actual.isPresent());
        assertEquals(expected.get().getId(), actual.get().getId());
        assertEquals(expected.get().getEmail(), actual.get().getEmail());
        assertEquals(expected.get().getPassword(), actual.get().getPassword());
        assertTrue(expected.get().getRoles().equals(actual.get().getRoles()));
    }

    @Test
    void findByEmail_notExistedUser_ok() {
        assertEquals(Optional.empty(), userDao.findByEmail(NOT_EXISTED_EMAIL));
    }

    @Test
    void findById_userExisted_ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findById(1L);
        assertTrue(actual.isPresent());
        assertEquals(1L, actual.get().getId());
        assertEquals(EMAIL, actual.get().getEmail());
        assertEquals(PASSWORD, actual.get().getPassword());
    }

    @Test
    void findById_notExistedUser_ok() {
        assertEquals(Optional.empty(), userDao.findById(999L));
    }
}

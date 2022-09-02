package mate.academy.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private UserDao userDao;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleDao.save(new Role(Role.RoleName.USER));
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @Test
    void save_ok() {
        User expected = new User();
        expected.setEmail("harrington@.ua");
        expected.setPassword("1234");
        expected.setRoles(Set.of(roleDao.getRoleByName("USER").get()));
        User actual = userDao.save(expected);
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getId(), actual.getId());
        assertFalse(expected.getRoles().isEmpty());
    }

    @Test
    void save_emptyUser_notOk() {
        assertThrows(DataProcessingException.class, () -> userDao.save(new User()),
                "Should receive DataProcessingException");
    }

    @Test
    void save_userIsNull_notOk() {
        assertThrows(DataProcessingException.class, () -> userDao.save(null),
                "Should receive DataProcessingException");
    }

    @Test
    void findById_ok() {
        User expected = new User();
        expected.setEmail("harrington@.ua");
        expected.setPassword("1234");
        expected.setRoles(Set.of(roleDao.getRoleByName("USER").get()));
        userDao.save(expected);
        Optional<User> actual = userDao.findById(expected.getId());
        assertTrue(actual.isPresent());
        assertEquals(expected.getEmail(), actual.get().getEmail());
        assertEquals(expected.getPassword(), actual.get().getPassword());
        assertFalse(expected.getRoles().isEmpty());
    }

    @Test
    void findById_noUserById_notOk() {
        assertThrows(NoSuchElementException.class, () -> userDao.findById(1L).get(),
                "Should receive NoSuchElementException");
    }

    @Test
    void findById_idIsNull_notOk() {
        assertThrows(DataProcessingException.class, () -> userDao.findById(null),
                "Should receive DataProcessingException");
    }

    @Test
    void findByEmail_ok() {
        User expected = new User();
        expected.setEmail("harrington@.ua");
        expected.setPassword("1234");
        expected.setRoles(Set.of(roleDao.getRoleByName("USER").get()));
        userDao.save(expected);
        Optional<User> actual = userDao.findByEmail(expected.getEmail());
        assertTrue(actual.isPresent());
        assertEquals(expected.getEmail(), actual.get().getEmail());
        assertEquals(expected.getPassword(), actual.get().getPassword());
        assertFalse(expected.getRoles().isEmpty());
    }

    @Test
    void findByEmail_noUserBySuchEmail_notOk() {
        assertThrows(NoSuchElementException.class, () -> userDao.findByEmail("harrington@.ua")
                        .get(), "Should receive NoSuchElementException");
    }
}

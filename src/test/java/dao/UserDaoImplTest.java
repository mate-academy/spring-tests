package dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserDaoImplTest extends AbstractTest {
    private UserDao userDao;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @Test
    void save_Ok() {
        User expected = new User();
        expected.setEmail("alice@mail.com");
        expected.setPassword("password");
        User actual = userDao.save(expected);
        assertNotNull(actual);
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(1L, actual.getId());
    }

    @Test
    void save_nullUser_NotOk() {
        assertThrows(DataProcessingException.class, () -> userDao.save(null),
                "Method should throw DataProcessingException if user is null");
    }

    @Test
    void save_emptyUser_NotOk() {
        assertThrows(DataProcessingException.class, () -> userDao.save(new User()),
                "Method should throw DataProcessingException if user is empty");
    }

    @Test
    void findByEmail_Ok() {
        Role role = new Role(Role.RoleName.USER);
        User expected = new User();
        expected.setEmail("alice@mail.com");
        expected.setPassword("password");
        expected.setRoles(Set.of(role));
        roleDao.save(role);
        userDao.save(expected);
        Optional<User> actual = userDao.findByEmail("alice@mail.com");
        assertNotNull(actual);
        assertEquals(expected.getEmail(), actual.get().getEmail());
        assertEquals(expected.getPassword(), actual.get().getPassword());
    }

    @Test
    void findByEmail_emailNotExists_NotOk() {
        Optional<User> actual = userDao.findByEmail("bob@mail.com");
        assertTrue(actual.isEmpty(), "You should return empty Optional "
                + "if there is no user with such email");
    }

    @Test
    void findById_Ok() {
        User expected = new User();
        expected.setEmail("alice@mail.com");
        expected.setPassword("password");
        userDao.save(expected);
        Optional<User> actual = userDao.findById(1L);
        assertNotNull(actual);
        assertEquals(expected.getEmail(), actual.get().getEmail());
        assertEquals(expected.getPassword(), actual.get().getPassword());
    }

    @Test
    void findById_wrongId_NotOk() {
        Optional<User> actual = userDao.findById(2L);
        assertTrue(actual.isEmpty(), "You should return empty Optional "
                + "if there is no user with such id");
    }
}

package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.NoSuchElementException;
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
    private User user;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        user = new User();
        user.setEmail("bob@i.ua");
        user.setPassword("1234");
        RoleDao roleDao = new RoleDaoImpl(getSessionFactory());
        user.setRoles(Set.of(roleDao.save(new Role(Role.RoleName.USER))));
    }

    @Test
    void save_user_OK() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals("bob@i.ua", actual.getEmail());
        assertEquals(1, actual.getRoles().size());
    }

    @Test
    void save_nullUser_notOK() {
        Assertions.assertThrows(DataProcessingException.class, () -> userDao.save(null));
    }

    @Test
    void findByEmail_OK() {
        userDao.save(user);
        User actual = userDao.findByEmail(user.getEmail()).get();
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
        assertEquals("bob@i.ua", actual.getEmail());
        assertEquals("1234", actual.getPassword());
        assertEquals(1, actual.getRoles().size());
        assertEquals("USER", actual.getRoles().stream()
                .map(r -> r.getRoleName().name())
                .filter(n -> n.equals("USER"))
                .findFirst()
                .orElseThrow(
                        () -> new RuntimeException("Couldn't find expected role name")));
    }

    @Test
    void findByEmail_nullEmail_notOK() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userDao.findByEmail(null).get());
    }

    @Test
    void findById_OK() {
        userDao.save(user);
        User actual = userDao.findById(user.getId()).get();
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
        assertEquals("bob@i.ua", actual.getEmail());
    }

    @Test
    void findById_nullId_notOK() {
        Assertions.assertThrows(DataProcessingException.class, () -> userDao.findById(null));
    }
}

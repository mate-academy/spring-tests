package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashSet;
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
        Set<Role> roles = Set.of(roleDao.save(new Role(Role.RoleName.ADMIN)),
                roleDao.save(new Role(Role.RoleName.USER)));
        user.setRoles(new HashSet<>(roles));
    }

    @Test
    void save_user_OK() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals("bob@i.ua", actual.getEmail());
        assertEquals(2, actual.getRoles().size());
    }

    @Test
    void save_nullUser_notOK() {
        try {
            userDao.save(null);
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Can't create entity: null", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }

    @Test
    void findByEmail_OK() {
        userDao.save(user);
        User actual = userDao.findByEmail(user.getEmail()).get();
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
        assertEquals("bob@i.ua", actual.getEmail());
        assertEquals("1234", actual.getPassword());
        assertEquals(2, actual.getRoles().size());
        assertEquals("USER", actual.getRoles().stream()
                .map(r -> r.getRoleName().name())
                .filter(n -> n.equals("USER"))
                .findFirst()
                .orElseThrow(
                        () -> new RuntimeException("Couldn't find expected role name")));
    }

    @Test
    void findByEmail_nullEmail_notOK() {
        try {
            userDao.findByEmail(null).get();
        } catch (NoSuchElementException e) {
            Assertions.assertEquals("No value present", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive NoSuchElementException");
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
        try {
            userDao.findById(null);
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Can't get entity: User by id null", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive DataProcessingException");
    }
}

package mate.academy.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest {
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "12345";
    private RoleDao roleDao;
    private UserDao userDao;
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

        role = new Role(Role.RoleName.USER);
        roleDao.save(role);

        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(role));
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(user);

        assertNotNull(actual);
        assertEquals(user, actual);
    }

    @Test
    void findById_Ok() {
        userDao.save(user);
        User actual = userDao.findById(1L).get();

        assertEquals(1L, actual.getId());
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(user);
        User actual = userDao.findByEmail(EMAIL).get();

        assertEquals(EMAIL, actual.getEmail());
    }

    @Test
    void findByEmail_Not_Ok() {
        userDao.save(user);
        Optional<User> actualOptional = userDao.findByEmail("bob");

        assertFalse(actualOptional.isPresent());
    }
}

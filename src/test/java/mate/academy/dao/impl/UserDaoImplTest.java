package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private UserDao userDao;
    private User admin;
    private RoleDao roleDao;
    private Role adminRole;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        admin = new User();
        adminRole = new Role(Role.RoleName.ADMIN);
        roleDao.save(adminRole);
        admin.setEmail("admin@mail.com");
        admin.setPassword("super1234");
        admin.setRoles(Set.of(adminRole));
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(admin);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
        assertEquals(admin,actual);
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(admin);
        Optional<User> actual = userDao.findByEmail(admin.getEmail());
        assertNotNull(actual);
        User actualUser = actual.get();
        assertEquals(admin.getEmail(), actualUser.getEmail());
        assertEquals(admin.getPassword(), actualUser.getPassword());
    }

    @Test
    void findByEmail_Not_Ok() {
        Optional<User> actual = userDao.findByEmail("us@mail.com");
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }
}

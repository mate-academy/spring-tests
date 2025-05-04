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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private UserDao userDao;
    private RoleDao roleDao;
    private User user;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        user = new User();
        user.setEmail("mark@test.ua");
        user.setPassword("12345678");
        Role role = roleDao.save(new Role(Role.RoleName.USER));
        user.setRoles(Set.of(role));
        userDao.save(user);
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @Test
    void findByEmail_existingUser_ok() {
        Optional<User> actualUser = userDao.findByEmail(user.getEmail());
        assertNotNull(actualUser);
        assertEquals(user.getEmail(), actualUser.get().getEmail());
    }

    @Test
    void findByEmail_nullEmail_notOk() {
        Assertions.assertDoesNotThrow(() -> userDao.findByEmail(null));
    }

    @Test
    void findByEmail_nonExistingUserEmail_notOk() {
        Optional<User> actualUser = userDao.findByEmail("non.existing@i.ua");
        assertTrue(actualUser.isEmpty());
    }
}

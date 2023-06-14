package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.Role.RoleName;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final String USER_EMAIL = "bob@com.ua";
    private static final String USER_PASSWORD = "password";
    private static final String EMAIL_NON_EXIST = "nonexist@com.ua";
    private UserDao userDao;
    private User user;
    private Role roleUser;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        RoleDao roleDao = new RoleDaoImpl(getSessionFactory());
        roleUser = new Role();
        roleUser.setRoleName(RoleName.USER);
        roleDao.save(roleUser);
        user = new User();
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
        user.setRoles(Set.of(roleUser));
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(user);
        assertNotNull(actual);
        assertEquals(actual.getEmail(), USER_EMAIL);
    }

    @Test
    void saveNullEmail_NotOk() {
        User userNullEmail = new User();
        userNullEmail.setEmail(null);
        userNullEmail.setPassword(USER_PASSWORD);
        userNullEmail.setRoles(Set.of(roleUser));
        assertThrows(DataProcessingException.class, () -> userDao.save(userNullEmail),
                "it is not possible to save a null email");
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail(user.getEmail());
        assertTrue(actual.isPresent());
        assertEquals(user.getEmail(), actual.get().getEmail());
    }

    @Test
    void findByEmail_NotOk() {
        Optional<User> actual = userDao.findByEmail(EMAIL_NON_EXIST);
        assertFalse(actual.isPresent());
    }

    @Test
    void findById_Ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findById(user.getId());
        assertTrue(actual.isPresent());
        assertEquals(user.getEmail(), actual.get().getEmail());
    }
}

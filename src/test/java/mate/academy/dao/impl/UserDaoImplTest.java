package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final String USER_EMAIL = "bob@gmail.com";
    private static final String INVALID_EMAIL = "blb@gmail.com";
    private static final String USER_PASSWORD = "1234";
    private UserDao userDao;
    private User testUser;
    private Role role;
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role();
        role.setRoleName(Role.RoleName.USER);
        roleDao.save(role);
        testUser = new User();
        testUser.setEmail(USER_EMAIL);
        testUser.setPassword(USER_PASSWORD);
        testUser.setRoles(Set.of(role));

    }

    @Test
    void save_Ok() {
        User actual = userDao.save(testUser);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
        assertEquals(USER_EMAIL, actual.getEmail());
        assertTrue(actual.getRoles().contains(role));
    }

    @Test
    void save_NullUser_notOk() {
        assertThrows(DataProcessingException.class, () -> userDao.save(null),
                "Method should Throw DataProcessingException if User null");
    }

    @Test
    void save_existedUser_notOk() {
        userDao.save(testUser);
        assertThrows(DataProcessingException.class, () -> userDao.save(testUser),
                "Method should Throw DataProcessingException "
                        + "if User with this email already exist");
    }

    @Test
    void findByEMail_Ok() {
        User expected = userDao.save(testUser);
        Optional<User> actual = userDao.findByEmail(USER_EMAIL);
        assertTrue(actual.isPresent(),
                "method should return not empty Optional if User exist in database");
        assertEquals(expected.getEmail(), actual.get().getEmail(),
                "method should return User with actual email");
        assertEquals(expected.getPassword(), actual.get().getPassword(),
                "method should return User with actual password");
    }

    @Test
    void findByEmail_WrongEmail_notOk() {
        assertThrows(NoSuchElementException.class, () -> userDao.findByEmail(INVALID_EMAIL).get(),
                "Method Should throw NoSUchElementException");
    }
}

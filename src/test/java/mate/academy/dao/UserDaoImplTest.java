package mate.academy.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import java.util.Set;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final Long USER_ID = 1L;
    private static final String USER_EMAIL = "email@gmail.com";
    private static final String USER_PASSWORD = "password";
    private static final Role.RoleName USER = Role.RoleName.USER;
    private static final User NULL_USER = null;
    private static final String NULL_EMAIL = null;
    private static final Long NULL_USER_ID = null;
    private UserDao underTest;
    private RoleDao roleDao;
    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        underTest = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = roleDao.save(new Role(USER));
        user = new User();
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_PASSWORD);
        user.setRoles(Set.of(role));
    }

    @Test
    void saveSuccess() {
        User actual = underTest.save(user);
        assertNotNull(actual);
        assertEquals(USER_ID, actual.getId());
    }

    @Test
    void saveException() {
        assertThrows(DataProcessingException.class,
                () -> underTest.save(NULL_USER));
    }

    @Test
    void findByEmailSuccess() {
        underTest.save(user);
        Optional<User> actual = underTest.findByEmail(USER_EMAIL);
        assertNotNull(actual.get());
        assertEquals(USER_ID, actual.get().getId());
        assertEquals(USER_EMAIL, actual.get().getEmail());

    }

    @Test
    void findByIdSuccess() {
        underTest.save(user);
        Optional<User> actual = underTest.findById(USER_ID);
        assertNotNull(actual.get());
        assertEquals(USER_ID, actual.get().getId());
        assertEquals(USER_EMAIL, actual.get().getEmail());
    }

    @Test
    void findByIdException() {
        assertThrows(DataProcessingException.class,
                () -> underTest.findById(NULL_USER_ID));
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }
}

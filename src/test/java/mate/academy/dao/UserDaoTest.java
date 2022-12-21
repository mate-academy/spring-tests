package mate.academy.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest {
    private static final Role.RoleName ROLE = Role.RoleName.USER;
    private static final String EMAIL = "someemail@gmail.com";
    private static final String PASSWORD = "password";
    private static User user;
    private static Role role;
    private UserDao userDao;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @BeforeAll
    static void beforeAll() {
        role = new Role();
        role.setRoleName(ROLE);

        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(role));
    }

    @Test
    @DisplayName("Save user with email " + EMAIL)
    void save_Ok() {
        roleDao.save(role);
        Long expectedUserId = 1L;
        assertThat(userDao.findById(expectedUserId)).isEmpty();
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual, "Method must return User object");
        Assertions.assertEquals(expectedUserId, actual.getId(),
                "Expected object with id " + expectedUserId + ", but was " + actual.getId());
    }

    @Test
    void findByEmail_Ok() {
        roleDao.save(role);
        String expected = userDao.save(user).getEmail();
        String actual = userDao.findByEmail(EMAIL).get().getEmail();
        Assertions.assertEquals(expected, actual,
                "Expected " + expected + ", but was " + actual);
    }

    @Test
    void findByEmail_NotOk() {
        Optional<User> actual = userDao.findByEmail(EMAIL);
        assertThat(actual).isEmpty();
    }

    @Test
    void findById_Ok() {
        Long id = 1L;
        assertThat(userDao.findById(id)).isEmpty();
        userDao.save(user);
        Long actual = userDao.findById(id).get().getId();
        Assertions.assertEquals(id, actual,
                "Expected object with id " + id + ", but was " + actual);
    }

    @Test
    void findById_NotOk() {
        Long expectedId = 1L;
        Optional<User> actual = userDao.findById(expectedId);
        assertThat(actual).isEmpty();
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }
}

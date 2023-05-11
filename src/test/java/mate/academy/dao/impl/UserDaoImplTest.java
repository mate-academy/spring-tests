package mate.academy.dao.impl;

import static mate.academy.model.Role.RoleName.USER;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static UserDao userDao;
    private static RoleDao roleDao;
    private static Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeAll
    static void beforeAll() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role(USER);
        roleDao.save(role);
    }

    @Test
    void findByEmail_existingEmail_ok() {
        String email = "valid@email.in.db";
        User expected = new User();
        expected.setEmail(email);
        expected.setRoles(Set.of(role));
        userDao.save(expected);
        Optional<User> actual = userDao.findByEmail(email);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(
                expected.getEmail(),
                actual.get().getEmail(),
                "Method should return optional containing user with email: "
                        + email + ". "
                        + "actual value: " + actual);
    }

    @Test
    void findByEmail_nonExistingEmail_ok() {
        String notPresentEmail = "other.name@test.test";
        Optional<User> actual = userDao.findByEmail(notPresentEmail);
        Assertions.assertTrue(actual.isEmpty(),
                "Method should return empty optional when email is not present. "
                        + "Was returned: " + actual
                        + " for email: " + notPresentEmail);
    }
}

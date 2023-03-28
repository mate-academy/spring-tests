package mate.academy.dao.impl;

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

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void findByEmail_existingEmail_ok() {
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
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
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        User expected = new User();
        expected.setEmail("some.name@test.test");
        expected.setRoles(Set.of(role));
        userDao.save(expected);
        String notPresentEmail = "other.name@test.test";
        Optional<User> actual = userDao.findByEmail(notPresentEmail);
        Assertions.assertTrue(actual.isEmpty(),
                "Method should return empty optional when email is not present. "
                        + "Was returned: " + actual
                        + " for email: " + notPresentEmail);
    }
}

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
    private final Role role = new Role(Role.RoleName.USER);
    private final String email = "existing@mail.db";
    private final User expected = new User();

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class, User.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        RoleDao roleDao = new RoleDaoImpl(getSessionFactory());
        roleDao.save(role);
        expected.setEmail(email);
        expected.setRoles(Set.of(role));
        userDao.save(expected);
    }

    @Test
    void findByEmail_existingEmail_ok() {
        Optional<User> actual = userDao.findByEmail(email);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected.getEmail(), actual.get().getEmail(),
                "It should return optional with user,  which email "
                        + " is '%s', but returned '%s"
                        .formatted(email, actual));
    }

    @Test
    void findByEmail_nonExistingEmail_ok() {
        String nonExistingEmail = "nonexistingemail@in.db";
        Optional<User> actual = userDao.findByEmail(nonExistingEmail);
        Assertions.assertTrue(actual.isEmpty(),
                ("It should return empty optional for wrong email "
                        + "but returned '%s' for email '%s'")
                        .formatted(actual, nonExistingEmail));
    }
}

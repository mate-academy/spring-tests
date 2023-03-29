package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final String BOB_EMAIL = "bob@gmail.ua";
    private static final String ALICE_EMAIL = "alice@gmail.ua";
    private static final String TEST_EMAIL = "test@gmail.ua";
    private static UserDaoImpl userDao;
    private Set<Role> roles;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());

        Role roleUser = new Role();
        roleUser.setRoleName(Role.RoleName.USER);
        new RoleDaoImpl(getSessionFactory()).save(roleUser);
        roles = Set.of(roleUser);
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{ User.class, Role.class };
    }

    @Test
    void save_saveUser_ok() {
        User user = new User();
        user.setEmail(BOB_EMAIL);
        userDao.save(user);

        assertNotNull(user);
        assertEquals(1L, user.getId());
    }

    @Test
    void findByEmail_twoUsersSavedGetRight_ok() {
        User userBob = new User();
        userBob.setEmail(BOB_EMAIL);
        userBob.setRoles(roles);
        userDao.save(userBob);

        User userAlice = new User();
        userAlice.setEmail(ALICE_EMAIL);
        userAlice.setRoles(roles);
        userDao.save(userAlice);

        Optional<User> actualUser = userDao.findByEmail(userBob.getEmail());
        assertTrue(actualUser.isPresent(),
                "Expected to find some user when getting by saved user's email");
        assertEquals(userBob.getEmail(), actualUser.get().getEmail(),
                "Expected to emails match");
    }

    @Test
    void findByEmail_emailIsNotExist_notOk() {
        Optional<User> actual = userDao.findByEmail(TEST_EMAIL);
        Assertions.assertFalse(actual.isPresent(),
                String.format("Should return empty optional for email: %s, "
                        + "but was: %s", TEST_EMAIL, actual));
    }

    @Test
    void findByEmail_emailIsNull_notOk() {
        Optional<User> actual = userDao.findByEmail(null);
        Assertions.assertFalse(actual.isPresent(),
                String.format("Should return empty optional for null email, but was: ", actual));
    }
}

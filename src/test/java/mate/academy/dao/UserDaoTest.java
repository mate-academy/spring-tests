package mate.academy.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserDaoTest extends AbstractTest {
    private static final String BOB_MAIL = "bob@mail.com";
    private static final String ALICE_EMAIl = "alice@mail.com";
    private static final String SIMPLE_PASSWORD = "1234";
    private static UserDao userDao;
    private User bob;
    private Role role = new Role();

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void beforeEach() {
        userDao = new UserDaoImpl(getSessionFactory());

        bob = new User();
        bob.setEmail(BOB_MAIL);
        bob.setPassword(SIMPLE_PASSWORD);
    }

    @Test
    void saveOneUser_Ok() {
        User actualBob = userDao.save(bob);
        assertNotNull(actualBob, "After saving you must return object of User");
        assertEquals(1L, actualBob.getId(), "After saving you must add id to User object");
    }

    @Test
    void saveTwoUser_Ok() {
        User alice = new User();
        alice.setEmail(ALICE_EMAIl);
        alice.setPassword(SIMPLE_PASSWORD);

        User actualBob = userDao.save(bob);
        User actualAlice = userDao.save(alice);
        assertEquals(1L, actualBob.getId(), "First saving user in query must have id 1");
        assertEquals(2L, actualAlice.getId(), "Second saving user in query must have id 2");
    }

    @Test
    void findById_Ok() {
        User expected = userDao.save(bob);
        Optional<User> actual = userDao.findById(expected.getId());
        assertEquals(expected.getEmail(), actual.get().getEmail(),
                "You need to return object with the same id");
    }

    @Test
    void findById_NotOk() {
        assertEquals(userDao.findById(100L), Optional.empty(),
                "When you take wrong id, you must return empty Optional");
    }

    @Test
    void findByEmail_Ok() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        bob.setRoles(Set.of(role));
        Session session = getSessionFactory().openSession();
        session.save(role);
        session.close();
        User expected = userDao.save(bob);
        Optional<User> actual = userDao.findByEmail(expected.getEmail());
        assertEquals(expected.getEmail(), actual.get().getEmail(),
                "You need to return object with the same email");
    }

    @Test
    void findByEmail_NotOk() {
        assertEquals(Optional.empty(), userDao.findByEmail(BOB_MAIL),
                "When you take wrong email, you must return empty Optional");
    }
}

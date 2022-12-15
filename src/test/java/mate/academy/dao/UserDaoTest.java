package mate.academy.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserDaoTest extends AbstractTest {
    private static final String BOB_MAIL = "bob@mail.com";
    private static final String ALICE_EMAIl = "alice@mail.com";
    private static final String SIMPLE_PASSWORD = "1234";
    private static final long NONE_ID = 0L;
    private static final long VALID_FIRST_ID = 1L;
    private static final long VALID_SECOND_ID = 2L;
    private static final long NOT_MATH_ID = 100L;
    private static UserDao userDao;
    private static User bob;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeAll
    static void setUp() {
        bob = new User();
    }

    @BeforeEach
    void beforeEach() {
        userDao = new UserDaoImpl(getSessionFactory());
        bob.setEmail(BOB_MAIL);
        bob.setPassword(SIMPLE_PASSWORD);
        bob.setId(NONE_ID);
    }

    @Test
    void saveOneUser_Ok() {
        User actualBob = userDao.save(bob);
        assertNotNull(actualBob,
                "After saving you must return object of User");
        assertEquals(VALID_FIRST_ID, actualBob.getId(),
                "After saving you must add id to User object");
    }

    @Test
    void saveTwoUser_Ok() {
        User alice = new User();
        alice.setEmail(ALICE_EMAIl);
        alice.setPassword(SIMPLE_PASSWORD);

        User actualBob = userDao.save(bob);
        User actualAlice = userDao.save(alice);
        assertEquals(VALID_FIRST_ID, actualBob.getId(),
                "First saving user in query must have id 1");
        assertEquals(VALID_SECOND_ID, actualAlice.getId(),
                "Second saving user in query must have id 2");
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
        assertEquals(userDao.findById(NOT_MATH_ID), Optional.empty(),
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

package mate.academy.dao.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final User USER = new User();
    private static final Role ROLE = new Role();
    private static final String EMAIL = "denis@mail.ru";
    private static final String NONEXISTENT_EMAIL = "nonexistentEmail";
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        SessionFactory factory = getSessionFactory();
        userDao = new UserDaoImpl(factory);
        USER.setEmail(EMAIL);
        ROLE.setRoleName(Role.RoleName.USER);
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            session.save(ROLE);
            USER.setRoles(Set.of(ROLE));
            session.save(USER);
            session.getTransaction().commit();
        }
    }

    @Test
    void findByEmail_ok() {
        Optional<User> user = Optional.empty();
        user = userDao.findByEmail(EMAIL);
        assertTrue(user.isPresent());
        assertEquals(user.get().getEmail(), EMAIL);
    }

    @Test
    void findByInvalidEmail_emptyOptional() {
        Optional<User> user = Optional.empty();
        user = userDao.findByEmail(NONEXISTENT_EMAIL);
        assertTrue(user.isEmpty());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }
}
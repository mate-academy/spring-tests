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
    private UserDao userDao;
    private SessionFactory factory;

    @BeforeEach
    void setUp() {
        factory = getSessionFactory();
        userDao = new UserDaoImpl(factory);
        USER.setEmail(EMAIL);
        ROLE.setRoleName(Role.RoleName.USER);
        try (Session session = factory.openSession()) {
            session.save(ROLE);
        }
        try (Session session = factory.openSession()) {
            Role role = session.get(Role.class, 1L);
            USER.setRoles(Set.of(role));
            session.save(USER);
        }
    }

    @Test
    void findByEmail_ok() {
        Optional<User> user = Optional.empty();
        user = userDao.findByEmail(EMAIL);
        assertTrue(user.isPresent());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }
}
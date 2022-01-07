package mate.academy.dao.impl;

import java.util.Set;
import mate.academy.dao.AbstractTest;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDaoImplTest extends AbstractTest {
    private UserDaoImpl userDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
    }

    @Test
    void findByEmail() {
        User user = new User();
        String email = "maksym@gmail.com";
        user.setEmail(email);
        user.setPassword("12345678");
        user.setRoles(Set.of());
        userDao.save(user);
        User userFromDatabase = userDao.findByEmail(email).get();
        user.setId(1L);
        assertEquals(user.toString(), userFromDatabase.toString());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] { User.class, Role.class };
    }
}
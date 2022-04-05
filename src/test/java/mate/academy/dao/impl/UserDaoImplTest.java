package mate.academy.dao.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private UserDaoImpl userDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
    }

    @Test
    void findByEmail() {
        User user = new User();
        String email = "bob@i.ua";
        String password = "1234bob";
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(Set.of());
        userDao.save(user);
        User userFromDatabase = userDao.findByEmail(email).get();
        user.setId(1L);
        Assertions.assertEquals(user.toString(), userFromDatabase.toString());
        Assertions.assertNotNull(user);
        Assertions.assertEquals(1L, user.getId());
        Assertions.assertEquals(email, user.getEmail());
        Assertions.assertEquals(password, user.getPassword());
    }

    @Test
    void findByEmail_NotOk() {
        Optional<User> actual = userDao.findByEmail("bob@i.ua");
        Assertions.assertTrue(actual.isEmpty());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] { User.class, Role.class };
    }
}

package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.AbstractDaoTest;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractDaoTest {
    private UserDao userDao;
    private User user;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        user = new User();
        user.setEmail("bob@mail.com");
        user.setPassword("1234");
        user.setRoles(Set.of(
                new RoleDaoImpl(getSessionFactory()).save(new Role(Role.RoleName.USER))
        ));
    }

    @Test
    void save_validUser_ok() {
        User actual = userDao.save(user);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
        assertEquals(user.getEmail(), actual.getEmail());
        assertEquals(user.getPassword(), actual.getPassword());
        assertEquals(1, actual.getRoles().size());
    }

    @Test
    void save_userIsNull_notOk() {
        assertThrows(DataProcessingException.class, () -> userDao.save(null));
    }

    @Test
    void findByEmail_userFound_ok() {
        User saved = userDao.save(user);
        Optional<User> actual = userDao.findByEmail(saved.getEmail());
        assertTrue(actual.isPresent());
        assertEquals(saved.getId(), actual.get().getId());
        assertEquals(saved.getEmail(), actual.get().getEmail());
        assertEquals(saved.getPassword(), actual.get().getPassword());
        assertEquals(saved.getRoles().size(), actual.get().getRoles().size());
    }

    @Test
    void findByEmail_userNotFound_ok() {
        userDao.save(user);
        Optional<User> actual = userDao.findByEmail("notfound@mail.com");
        assertTrue(actual.isEmpty());
    }

    @Test
    void findByEmail_emailIsNull_ok() {
        Optional<User> actual = userDao.findByEmail(null);
        assertTrue(actual.isEmpty());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }
}

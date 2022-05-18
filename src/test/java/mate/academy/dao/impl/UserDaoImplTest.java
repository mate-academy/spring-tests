package mate.academy.dao.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import mate.academy.dao.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.Optional;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;

class UserDaoImplTest extends AbstractTest {
    private static final String EMAIL = "bchupika@mate.academy";
    private static final String PASSWORD = "12345678";
    private UserDao userDao;
    private User expectedUser;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        expectedUser = new User();
        expectedUser.setEmail(EMAIL);
        expectedUser.setPassword(PASSWORD);
        expectedUser.setRoles(Set.of());
    }

    @Test
    void save_invalidUser_ok() {
        User actual = userDao.save(expectedUser);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
    }

    @Test
    void save_invalidId_notOk() {
        userDao.save(expectedUser);
        expectedUser.setId(1L);
        try {
            userDao.save(expectedUser);
        } catch (DataProcessingException e) {
            assertEquals("Can't create entity: " + expectedUser, e.getMessage());
        }
    }

    @Test
    void findByEmail_invalidEmail_notOk() {
        Optional<User> actual = userDao.findByEmail("bchupika@mate.academy");
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    void findById_validData_ok() {
        userDao.save(expectedUser);
        Optional<User> actual = userDao.findById(1L);
        assertEquals(1L, actual.get().getId());
        assertEquals("bchupika@mate.academy", actual.get().getEmail());
        assertEquals("12345678", actual.get().getPassword());
    }
}

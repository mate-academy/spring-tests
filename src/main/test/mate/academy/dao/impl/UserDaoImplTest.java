package mate.academy.dao.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserDaoImplTest extends AbstractTest {
    private static final String EMAIL = "alice@i.ua";
    private static final String PASSWORD = "12345678";
    private static final Long ID = 1L;
    private UserDao userDao;
    private User user;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    public void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        RoleDao roleDao = new RoleDaoImpl(getSessionFactory());
        role = roleDao.save(new Role(Role.RoleName.USER));
        user = userDao.save(testUser());
    }

    @Test
    public void save_Ok() {
        assertNotNull(user);
        assertEquals(ID, user.getId());
        assertEquals(EMAIL, user.getEmail());
    }

    @Test
    public void save_NotOk() {
        assertThrows(DataProcessingException.class,
                () -> userDao.save(testUser()));
    }

    @Test
    public void findByEmail_Ok() {
        Optional<User> actualOptional = userDao.findByEmail(EMAIL);
        assertNotNull(actualOptional);
        assertFalse(actualOptional.isEmpty());
        User actual = actualOptional.get();
        assertEquals(EMAIL, actual.getEmail());
    }

    @Test
    public void findByEmail_NotOk() {
        Optional<User> actualOptional = userDao.findByEmail("email@i.ua");
        assertTrue(actualOptional.isEmpty());
    }

    @Test
    public void findById_Ok() {
        Optional<User> actualOptional = userDao.findById(ID);
        assertNotNull(actualOptional);
        assertFalse(actualOptional.isEmpty());
        User actual = actualOptional.get();
        assertEquals(ID, actual.getId());
    }

    @Test
    public void findById_NotOk() {
        Optional<User> actualOptional = userDao.findById(23L);
        assertTrue(actualOptional.isEmpty());
    }

    private User testUser() {
        User testUser = new User();
        testUser.setEmail(EMAIL);
        testUser.setPassword(PASSWORD);
        testUser.setRoles(Set.of(role));
        return testUser;
    }
}
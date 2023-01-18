package mate.academy.dao;

import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class UserDaoImplTest extends AbstractTest {
    private static final long ID = 1L;
    private static final String EMAIL = "user@gmail.com";
    private static final String PASSWORD = "1234";
    private static final long WRONG_ID = 5L;
    private static final String WRONG_EMAIL = "wrong@gmail.com";
    private static final Role ROLE = new Role(Role.RoleName.USER);
    private UserDao userDao;
    private User user;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        RoleDao roleDao = new RoleDaoImpl(getSessionFactory());
        role = roleDao.save(ROLE);
        user = userDao.save(createUser());
    }

    @Test
    void save_Ok() {
        assertEquals(ID, user.getId());
        assertEquals(EMAIL, user.getEmail());
    }

    @Test
    void save_UserDataDuplication_NotOk() {
        assertThrows(DataProcessingException.class,
                () -> userDao.save(createUser()));
    }

    @Test
    void findByEmail_Ok() {
        Optional<User> actual = userDao.findByEmail(EMAIL);
        assertNotNull(actual);
        assertTrue(actual.isPresent());
        assertEquals(EMAIL, actual.get().getEmail());
    }

    @Test
    void findByEmail_NoUserEmailInDB_NotOk() {
        Optional<User> actual = userDao.findByEmail(WRONG_EMAIL);
        assertTrue(actual.isEmpty());
    }

    @Test
    void findById_Ok() {
        Optional<User> actual = userDao.findById(ID);
        assertNotNull(actual);
        assertTrue(actual.isPresent());
        assertEquals(ID, actual.get().getId());
    }

    @Test
    void findById_NoIdInDB_NotOk() {
        Optional<User> actual = userDao.findById(WRONG_ID);
        assertTrue(actual.isEmpty());
    }

    private User createUser() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(role));
        return user;
    }
}

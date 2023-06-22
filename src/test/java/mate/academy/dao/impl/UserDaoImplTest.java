package mate.academy.dao.impl;

import static mate.academy.model.Role.RoleName.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private static final String EXISTED_EMAIL = "test@ukr.net";
    private static final String NOT_EXISTED_EMAIL = "some@ukr.net";
    private static final String PASSWORD = "12345";
    private static final long EXISTED_ID = 1L;
    private static final long NOT_EXISTED_ID = 0L;
    private UserDao userDao;
    private User testUser;
    private Role testRole;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        testRole = new RoleDaoImpl(getSessionFactory()).save(new Role(USER));
        userDao = new UserDaoImpl(getSessionFactory());
        testUser = new User();
        testUser.setEmail(EXISTED_EMAIL);
        testUser.setPassword(PASSWORD);
        testUser.setRoles(Set.of(testRole));
    }

    @Test
    void saveUser_validValue_ok() {
        User actual = userDao.save(testUser);
        assertNotNull(actual,
                "method should not return null if  User value is valid");
        assertEquals(EXISTED_ID, actual.getId(),
                "method should return User with actual id");
        assertEquals(EXISTED_EMAIL, actual.getEmail(),
                "method should return User with actual email");
        assertEquals(PASSWORD, actual.getPassword(),
                "method should return User with actual password");
        assertTrue(actual.getRoles().contains(testRole),
                "method should return User with actual roles");
    }

    @Test
    void saveUser_duplicateUserEmail_notOk() {
        userDao.save(testUser);
        assertThrows(DataProcessingException.class, () -> userDao.save(testUser),
                "method should throw DataProcessingException if email already exist in database");
    }

    @Test
    void saveUser_nullUser_notOk() {
        assertThrows(DataProcessingException.class, () -> userDao.save(null),
                "method should throw DataProcessingException if User is null");
    }

    @Test
    void saveUser_nullUsersEmail_notOk() {
        testUser.setEmail(null);
        assertThrows(DataProcessingException.class, () -> userDao.save(testUser),
                "method should throw DataProcessingException if Users email is null");
    }

    @Test
    void findById_existedUserId_ok() {
        User expected = userDao.save(testUser);
        Optional<User> actual = userDao.findById(EXISTED_ID);
        assertTrue(actual.isPresent(),
                "method should return not empty Optional if User exist in database");
        assertEquals(expected.getEmail(), actual.get().getEmail(),
                "method should return User with actual email");
        assertEquals(expected.getPassword(), actual.get().getPassword(),
                "method should return User with actual password");
        assertTrue(actual.get().getRoles().contains(testRole),
                "method should return User with actual roles");
    }

    @Test
    void findById_notExistedUserId_notOk() {
        Optional<User> actual = userDao.findById(NOT_EXISTED_ID);
        assertTrue(actual.isEmpty(),
                "method should return empty Optional if User id not exist in database");
    }

    @Test
    void findById_nullUserId_notOk() {
        assertThrows(DataProcessingException.class, () -> userDao.findById(null),
                "method should throw DataProcessingException if User id is null");
    }

    @Test
    void findByEmail_existedUserEmail_ok() {
        User expected = userDao.save(testUser);
        Optional<User> actual = userDao.findByEmail(EXISTED_EMAIL);
        assertTrue(actual.isPresent(),
                "method should return not empty Optional if User exist in database");
        assertEquals(expected.getEmail(), actual.get().getEmail(),
                "method should return User with actual email");
        assertEquals(expected.getPassword(), actual.get().getPassword(),
                "method should return User with actual password");
        assertTrue(actual.get().getRoles().contains(testRole),
                "method should return User with actual roles");
    }

    @Test
    void findByEmail_notExistedUserEmail_notOk() {
        Optional<User> actual = userDao.findByEmail(NOT_EXISTED_EMAIL);
        assertTrue(actual.isEmpty(),
                "method should return empty Optional if User email not exist in database");
    }

    @Test
    void findByEmail_nullUserEmail_notOk() {
        Optional<User> actual = userDao.findByEmail(null);
        assertTrue(actual.isEmpty(),
                "method should return empty Optional if User email is null");
    }

    @Test
    void findByEmail_failConnection_notOk() {
        getSessionFactory().close();
        assertThrows(DataProcessingException.class, () -> userDao.findByEmail(EXISTED_EMAIL),
                "method should throw DataProcessingException in case of problems with database");
    }
}

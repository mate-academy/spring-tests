package mate.academy.dao;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest {
    private static final String EMAIL = "tom@gmail.com";
    private static final String PASSWORD = "1234";
    private UserDao userDao;
    private RoleDao roleDao;
    private Role adminRole;
    private Role userRole;
    private User actualUser;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        adminRole = roleDao.save(new Role(Role.RoleName.ADMIN));
        userRole = roleDao.save(new Role(Role.RoleName.USER));
    }

    @Test
    void save_Ok() {
        actualUser = userDao.save(getUserForTest());
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(1L, actualUser.getId());
    }

    @Test
    void findByEmail_Ok() {
        actualUser = userDao.save(getUserForTest());
        Optional<User> userByEmail = userDao.findByEmail(EMAIL);
        Assertions.assertTrue(userByEmail.isPresent());
        User userFindByEmail = userByEmail.get();
        Assertions.assertNotNull(userFindByEmail);
        Assertions.assertEquals(userFindByEmail.getEmail(), actualUser.getEmail());
    }

    @Test
    void findById_Ok() {
        actualUser = userDao.save(getUserForTest());
        User userId = userDao.findById(1L).orElseThrow();
        Assertions.assertNotNull(userId);
        Assertions.assertEquals(userId.getId(), actualUser.getId());
    }

    @Test
    void findByEmail_WrongEmail_NotOk() {
        userDao.save(getUserForTest());
        Optional<User> userByEmail = userDao.findByEmail("invalid_email");
        Assertions.assertTrue(userByEmail.isEmpty());
    }

    @Test
    void findByEmail_NotFound_NotOk() {
        try {
            actualUser = userDao.save(getUserForTest());
            if (userDao.findByEmail(EMAIL).isEmpty()) {
                Assertions.fail("Expected to receive DataProcessingException");
            }
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Couldn't get user by email: ", e.getMessage());
        }
    }

    @Test
    void findByEmail_EmptyEmail_NotOk() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userDao.findByEmail("").orElseThrow());
    }

    @Test
    void findByEmail_NullEmail_NotOk() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userDao.findByEmail(null).orElseThrow());
    }

    @Test
    void findByEmail_ExistingEmail_NotOk() {
        actualUser = userDao.save(getUserForTest());
        User duplicateEmailUser = new User();
        duplicateEmailUser.setEmail(EMAIL);
        duplicateEmailUser.setPassword("2222");
        duplicateEmailUser.setRoles(Set.of(adminRole));
        Assertions.assertThrows(DataProcessingException.class,
                () -> userDao.save(duplicateEmailUser));
    }

    @Test
    void findById_WrongId_NotOk() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userDao.findById(2L).orElseThrow());
    }

    @Test
    void findById_NullId_NotOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> userDao.findById(null).orElseThrow());
    }

    private User getUserForTest() {
        User testUser = new User();
        testUser.setEmail(EMAIL);
        testUser.setPassword(PASSWORD);
        testUser.setRoles(Set.of(userRole, adminRole));
        return testUser;
    }
}

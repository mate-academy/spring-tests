package mate.academy.dao;

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
    private static final String USER_MAIL = "example@mail.com";
    private static final String INVALID_USER_MAIL = "invalid_mail";
    private static final String USER_PASSWORD = "example_password";
    private static final Long USER_ID = 1L;
    private static final Role.RoleName USER_ROLE = Role.RoleName.USER;
    private UserDao userDao;
    private RoleDao roleDao;
    private User testUser;

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao.save(new Role(USER_ROLE));
        testUser = new User();
        testUser.setEmail(USER_MAIL);
        testUser.setPassword(USER_PASSWORD);
        Set<Role> setRole = Set.of(roleDao.getRoleByName(USER_ROLE.name()).get());
        testUser.setRoles(setRole);
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @Test
    void saveUser_Ok() {
        User actualUser = userDao.save(testUser);
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(USER_ID,actualUser.getId());
    }

    @Test
    void saveUser_userIsNull_notOk() {
        DataProcessingException dataProcessingExceptionExpected =
                Assertions.assertThrows(DataProcessingException.class,
                        () -> userDao.save(null), "DataProcessingException expected");
        Assertions.assertEquals("Can't create entity: null",
                dataProcessingExceptionExpected.getMessage());
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(testUser);
        Optional<User> optionalUser = userDao.findByEmail(USER_MAIL);
        Assertions.assertTrue(optionalUser.isPresent());
        User actualUser = optionalUser.get();
        Assertions.assertEquals(actualUser.getEmail(), USER_MAIL);
        Assertions.assertEquals(actualUser.getPassword(), USER_PASSWORD);
        Role.RoleName actualUserRoleName = actualUser.getRoles()
                .stream()
                .findFirst()
                .get()
                .getRoleName();
        Assertions.assertEquals(actualUserRoleName, USER_ROLE);
    }

    @Test
    void findByEmail_invalidEmail_notOk() {
        userDao.save(testUser);
        Optional<User> optionalUser = userDao.findByEmail(INVALID_USER_MAIL);
        Assertions.assertFalse(optionalUser.isPresent());
    }

    @Test
    void finById_Ok() {
        userDao.save(testUser);
        Optional<User> optionalUser = userDao.findById(USER_ID);
        Assertions.assertTrue(optionalUser.isPresent());
        User actualUser = optionalUser.get();
        Assertions.assertEquals(USER_ID,actualUser.getId());
    }
}

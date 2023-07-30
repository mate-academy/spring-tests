package mate.academy.dao.impl;

import java.util.NoSuchElementException;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserDaoImplTest extends AbstractTest {
    private static final String TEST_EMAIL = "testEmail@gmail.com";
    private static final String TEST_PASSWORD = "12345";
    private UserDao userDao;
    private RoleDao roleDao;
    private User testUser;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        testUser = new User();
        testUser.setEmail(TEST_EMAIL);
        testUser.setPassword(TEST_PASSWORD);
        testUser.setRoles(Set.of(role));
    }

    @Test
    void saveUser_UserSaved_Ok() {
        User user = userDao.save(testUser);
        Assertions.assertNotNull(user);
        Assertions.assertEquals(1L, user.getId(),
                "The id of actual user doesn't match the expected id");
    }

    @Test
    void findUserByEmail_UserFound_Ok() {
        userDao.save(testUser);
        User foundedUser = userDao.findByEmail(TEST_EMAIL).get();
        Assertions.assertEquals(foundedUser.getEmail(),TEST_EMAIL,
                "The actual user's email doesn't match the expected email");
    }

    @Test
    void findUserByEmail_FindUserByNonExistingEmail_NotOk() {
        userDao.save(testUser);
        Assertions.assertThrows(NoSuchElementException.class, () ->
                userDao.findByEmail("wrong").get(),
                "NoSuchElementException for non-existing email expected");
    }

    @Test
    void findUserByEmail_FindUserByEmptyEmail_NotOk() {
        Assertions.assertThrows(NoSuchElementException.class, () ->
                userDao.findByEmail("").get(),
                "NoSuchElementException for empty email expected");
    }

    @Test
    void findUserById_UserFound_Ok() {
        userDao.save(testUser);
        User foundedUser = userDao.findById(1L).get();
        Assertions.assertEquals(foundedUser.getId(),1L,
                "The id of actual user doesn't match the expected id");
    }

    @Test
    void findUserById_findUserByNonExistingId_NotOk() {
        userDao.save(testUser);
        Assertions.assertThrows(NoSuchElementException.class, () ->
                userDao.findById(2L).get(),
                "NoSuchElementException for non-existing id expected");
    }

    @Test
    void findUserById_findUserByNullId_NotOk() {
        userDao.save(testUser);
        Assertions.assertThrows(DataProcessingException.class, () ->
                userDao.findById(null).get(),
                "DataProcessingException for non-existing id expected");
    }
}

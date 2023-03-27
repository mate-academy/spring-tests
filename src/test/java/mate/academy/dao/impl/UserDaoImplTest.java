package mate.academy.dao.impl;

import java.util.NoSuchElementException;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserDaoImplTest extends AbstractTest {
    private static Role userRole;
    private static Role adminRole;
    private static User defaultUser;
    private UserDao userDao;
    private RoleDao roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @BeforeAll
    public static void initUser() {
        defaultUser = new User();
        defaultUser.setEmail("default@gmail.com");
        defaultUser.setPassword("1234");
    }

    @Test
    void save_ok() {
        setRoleDao();
        defaultUser.setRoles(Set.of(userRole, adminRole));
        Assertions.assertEquals(1L, userDao.save(defaultUser).getId(),
                "Role should be add to DB");
    }

    @Test
    void save_withoutPassword_ok() {
        User user = new User();
        user.setEmail("testUser@gmail.com");
        Assertions.assertEquals(1L, userDao.save(user).getId(),
                "Role without the password should be add to DB");
    }

    @Test
    void save_twoUserWithSameEmail_notOk() {
        userDao.save(defaultUser);
        Assertions.assertThrows(DataProcessingException.class,
                () -> userDao.save(defaultUser),
                "You can't to add user with similar email, the email must be unique");
    }

    @Test
    void save_withoutEmail_notOk() {
        User user = new User();
        user.setPassword("1234");
        Assertions.assertThrows(DataProcessingException.class, () -> userDao.save(user),
                "You can't add a user without an email, please fixed this");
    }

    @Test
    void findByEmail_correctEmail_ok() {
        userDao.save(defaultUser);
        User actual = userDao.findByEmail(defaultUser.getEmail()).orElseGet(
                () -> Assertions.fail("User must be find by email: "
                        + defaultUser.getEmail()));
        Assertions.assertEquals(defaultUser.getId(), actual.getId(),
                "User id should be: " + defaultUser.getId());
        Assertions.assertEquals(defaultUser.getEmail(), actual.getEmail(),
                "User email should be: " + defaultUser.getEmail());
    }

    @Test
    void findByEmail_nullEmail_notOk() {
        userDao.save(defaultUser);
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userDao.findByEmail(null).get(),
                "There should not be a user with email null");
    }

    @Test
    void findByEmail_incorrectEmail_notOk() {
        userDao.save(defaultUser);
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userDao.findByEmail("invalid email").get(),
                "There should not be a user with invalid email");
    }

    @Test
    void findById_correctCase_ok() {
        User user = new User();
        user.setEmail("user@gmail.com");
        user.setPassword("1234");
        userDao.save(defaultUser);
        userDao.save(user);
        Assertions.assertEquals(defaultUser.getEmail(), userDao.findById(
                defaultUser.getId()).get().getEmail());
        Assertions.assertEquals(user.getEmail(), userDao.findById(
                user.getId()).get().getEmail());
    }

    @Test
    void findById_nullId_notOk() {
        userDao.save(defaultUser);
        Assertions.assertThrows(DataProcessingException.class,
                () -> userDao.findById(null));
    }

    @Test
    void findById_incorrectId_notOk() {
        userDao.save(defaultUser);
        Assertions.assertThrows(NoSuchElementException.class, () -> userDao.findById(0L).get(),
                "We can't find user by id: 0");
        Assertions.assertThrows(NoSuchElementException.class, () -> userDao.findById(2L).get(),
                "In DB only one record, we can't get user by id: 2");
    }

    private void setRoleDao() {
        try {
            userRole = new Role(Role.RoleName.USER);
            adminRole = new Role(Role.RoleName.ADMIN);
            roleDao.save(userRole);
            roleDao.save(adminRole);
        } catch (Exception e) {
            throw new DataProcessingException("You have some problem with initialize roles, "
                    + "fix  problem in RoleDao)", e);
        }
    }
}

package mate.academy.dao.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private String userEmail;
    private UserDao userDao;
    private RoleDao roleDao;
    private User user;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class, Role.RoleName.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        userEmail = "user@gmail.com";
        String userPassword = "12345";
        user = new User();
        user.setPassword(userPassword);
        user.setEmail(userEmail);
    }

    @Test
    void save_Ok() {
        Long userId = 1L;
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(userId, actual.getId());
        Assertions.assertEquals(userEmail, actual.getEmail());
    }

    @Test
    void save_duplicatedUser_Ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertThrows(DataProcessingException.class, () -> userDao.save(user),
                "Expected DataProcessingException when saving User which already exist in DB");
    }

    @Test
    void findByEmail_Ok() {
        Role roleFromDb = roleDao.save(new Role(Role.RoleName.USER));
        user.setRoles(Set.of(roleFromDb));
        User userFromDb = userDao.save(user);
        String userEmail = "user@gmail.com";
        Optional<User> actual = userDao.findByEmail(userEmail);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(userFromDb.getId(), actual.get().getId());
        Assertions.assertEquals(userFromDb.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(userFromDb.getRoles().size(), actual.get().getRoles().size());
    }

    @Test
    void findByEmail_nonExistentUser_Ok() {
        User userFromDb = userDao.save(user);
        String userEmail = "prefix" + userFromDb.getEmail();
        Optional<User> actual = userDao.findByEmail(userEmail);
        Assertions.assertNotNull(actual);
        Assertions.assertTrue(actual.isEmpty());
    }
}

package mate.academy.dao.impl;

import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.Set;

class UserDaoImplTest extends AbstractDaoTest {
    private UserDao userDao;
    private static RoleDao roleDao;
    private static Role adminRole;
    private static Role userRole;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        adminRole = new Role(Role.RoleName.ADMIN);
        userRole = new Role(Role.RoleName.USER);
        roleDao.save(adminRole);
        roleDao.save(userRole);
    }

    @Test
    void save_ok() {
        User expectedUser = createUser();
        User actualUser = userDao.save(expectedUser);
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(expectedUser.getId(), actualUser.getId());
        Assertions.assertEquals(expectedUser, actualUser);
    }

    @Test
    void findByEmailExisting_ok() {
        User expectedUser = createUser();
        userDao.save(expectedUser);
        Optional<User> actualUserOptional = userDao.findByEmail(expectedUser.getEmail());
        Assertions.assertTrue(actualUserOptional.isPresent());
        User actualUser = actualUserOptional.get();
        Assertions.assertEquals(expectedUser.getId(), actualUser.getId());
        Assertions.assertEquals(expectedUser, actualUser);
    }

    @Test
    void findByEmailNotExisting_emptyOptional() {
        Optional<User> actualUserOptional = userDao.findByEmail("unknown@i.ua");
        Assertions.assertTrue(actualUserOptional.isEmpty());
    }

    @Test
    void findById_ok() {
        User expectedUser = createUser();
        userDao.save(expectedUser);
        Optional<User> actualUserOptional = userDao.findById(expectedUser.getId());
        Assertions.assertTrue(actualUserOptional.isPresent());
        User actualUser = actualUserOptional.get();
        Assertions.assertEquals(expectedUser.getId(), actualUser.getId());
        Assertions.assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        Assertions.assertEquals(expectedUser.getPassword(), actualUser.getPassword());
    }

    @Test
    void findByIdNotExisting_emptyOptional() {
        Optional<User> actualUserOptional = userDao.findById(101010L);
        Assertions.assertTrue(actualUserOptional.isEmpty());
    }

    private User createUser() {
        String email = "bob@i.ua";
        String password = "1234";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        adminRole = roleDao.getRoleByName("ADMIN").get();
        userRole = roleDao.getRoleByName("USER").get();
        user.setRoles(Set.of(adminRole, userRole));
        return user;
    }
}

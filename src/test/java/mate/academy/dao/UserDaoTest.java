package mate.academy.dao;

import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.Set;

class UserDaoTest extends AbstractTest {
    private UserDao userDao;
    private static RoleDao roleDao;
    private static Role roleAdmin;
    private static Role roleUser;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleAdmin = new Role(Role.RoleName.ADMIN);
        roleUser = new Role(Role.RoleName.USER);
        roleDao.save(roleAdmin);
        roleDao.save(roleUser);
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
        //findById returns User without Roles
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
        roleAdmin = roleDao.getRoleByName("ADMIN").get();
        roleUser = roleDao.getRoleByName("USER").get();
        user.setRoles(Set.of(roleAdmin, roleUser));
        return user;
    }
}
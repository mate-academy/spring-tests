package mate.academy.dao.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private UserDao userDao;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleDao.save(new Role(Role.RoleName.USER));
        roleDao.save(new Role(Role.RoleName.ADMIN));
    }

    @Test
    void save_ok() {
        User expectedUser = new User();
        expectedUser.setEmail("user@gmail.com");
        expectedUser.setPassword("userpassword");
        expectedUser.setRoles(Set.of(roleDao.getRoleByName("USER").get()));

        User expectedAdmin = new User();
        expectedAdmin.setEmail("admin@gmail.com");
        expectedAdmin.setPassword("adminpassword");
        expectedUser.setRoles(Set.of(roleDao.getRoleByName("ADMIN").get()));

        User actualUser = userDao.save(expectedUser);
        User actualAdmin = userDao.save(expectedAdmin);

        Assertions.assertNotNull(actualUser);
        Assertions.assertNotNull(actualAdmin);

        Assertions.assertEquals(1L, actualUser.getId());
        Assertions.assertEquals(2L, actualAdmin.getId());

        Assertions.assertEquals(expectedUser, actualUser);
        Assertions.assertEquals(expectedAdmin, actualAdmin);
    }

    @Test
    void save_nullUser_shouldThrowDataProcessingException() {
        User user = null;
        try {
            userDao.save(user);
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Can't create entity: " + user, e.getMessage());
            return;
        }
        Assertions.fail();
    }

    @Test
    void findById_validIk_shouldFind() {
        User user = new User();
        user.setEmail("denik@gmail.com");
        user.setPassword("password");
        user.setRoles(Set.of(roleDao.getRoleByName("USER").get()));
        userDao.save(user);
        Optional<User> optionalActual = userDao.findById(user.getId());
        Assertions.assertTrue(optionalActual.isPresent());
        User actual = optionalActual.get();
        Assertions.assertEquals(user, actual);
    }

    @Test
    void findById_wrongId_shouldReturnEmptyOptional() {
        Optional<User> actual = userDao.findById(5L);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findByEmail_existingEmail_shouldFind() {
        String email = "denik@gmail.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("password");
        user.setRoles(Set.of(roleDao.getRoleByName("USER").get()));
        userDao.save(user);
        Optional<User> optionalActual = userDao.findByEmail(email);
        Assertions.assertTrue(optionalActual.isPresent());
        User actual = optionalActual.get();
        Assertions.assertEquals(user, actual);
    }

    @Test
    void findByEmail_notExistingEmail_shouldReturnEmptyOptional() {
        Optional<User> actual = userDao.findByEmail("notexisting@gmail.com");
        Assertions.assertTrue(actual.isEmpty());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }
}

package mate.academy.dao;

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
    private UserDao userDao;
    private User user;
    private User admin;

    @BeforeEach
    void setUp() {
        RoleDao roleDao = new RoleDaoImpl(getSessionFactory());
        roleDao.save(new Role(Role.RoleName.USER));
        roleDao.save(new Role(Role.RoleName.ADMIN));

        userDao = new UserDaoImpl(getSessionFactory());
        user = new User();
        user.setPassword("1234");
        user.setEmail("user@i.ua");
        user.setRoles(Set.of(roleDao.getRoleByName("USER").get()));
        admin = new User();
        admin.setPassword("1234");
        admin.setEmail("admin@i.ua");
        admin.setRoles(Set.of(roleDao.getRoleByName("ADMIN").get()));
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(user);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
    }

    @Test
    void save_NullUser_ThrowException() {
        try {
            userDao.save(null);
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Can't create entity: " + null, e.getMessage());
            return;
        }
        Assertions.fail("Incorrect user should throw DataProcessingException");
    }

    @Test
    void findByEmail_Ok() {
        userDao.save(user);
        userDao.save(admin);

        String email = user.getEmail();
        User actual = userDao.findByEmail(email).orElse(new User());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(email, actual.getEmail());
    }

    @Test
    void findByEmail_NotExist_NotOk() {
        userDao.save(user);
        userDao.save(admin);

        String email = "bob@i.ua";
        User actual = userDao.findByEmail(email).orElse(null);
        Assertions.assertNull(actual);
    }

    @Test
    void findByEmail_NullEmail_NotOk() {
        userDao.save(user);
        userDao.save(admin);

        Assertions.assertTrue(userDao.findByEmail(null).isEmpty(),
                              "Method with null email argument should return null");
    }

    @Test
    void findById_Ok() {
        userDao.save(user);
        userDao.save(admin);

        Long id = user.getId();
        User actual = userDao.findById(id).orElse(new User());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(id, actual.getId());
    }

    @Test
    void findById_NotExist_NotOk() {
        userDao.save(user);
        userDao.save(admin);

        Long id = 50L;
        User actual = userDao.findById(id).orElse(null);
        Assertions.assertNull(actual);
    }

    @Test
    void findById_NullId_NotOk() {
        try {
            userDao.findById(null);
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Can't get entity: " + User.class.getSimpleName()
                                            + " by id " + null, e.getMessage());
            return;
        }
        Assertions.fail("Method with null ID argument should throw DataProcessingException");
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }
}

package mate.academy.dao.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.util.UserUtilForTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest{
    private UserDao userDao;
    private RoleDao roleDao;
    private UserUtilForTest userUtil;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class, User.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        userUtil = new UserUtilForTest();
    }

    @Test
    void saveUser_validUser_ok() {
        Role admin = roleDao.save(userUtil.getAdminRole());
        Role user = roleDao.save(userUtil.getUserRole());
        User expectedBoris = userUtil.getUserBoris();
        User expectedNadja = userUtil.getUserNadja();
        expectedBoris.setRoles(Set.of(admin));
        expectedNadja.setRoles(Set.of(user));

        User actualBoris = userDao.save(expectedBoris);
        User actualNadja = userDao.save(expectedNadja);

        Assertions.assertEquals(1L, actualBoris.getId());
        Assertions.assertEquals(2L, actualNadja.getId());
        Assertions.assertNotNull(actualBoris);
        Assertions.assertNotNull(actualNadja);
        Assertions.assertEquals(expectedBoris.getEmail(), actualBoris.getEmail());
        Assertions.assertEquals(expectedNadja.getEmail(), actualNadja.getEmail());
        Assertions.assertEquals(expectedBoris.getPassword(), actualBoris.getPassword());
        Assertions.assertEquals(expectedNadja.getPassword(), actualNadja.getPassword());
    }

    @Test
    void findByEmail_ok() {
        User expectedBoris = userUtil.getUserBoris();
        User expectedNadja = userUtil.getUserNadja();
        expectedBoris.setRoles(Set.of(roleDao.save(userUtil.getAdminRole())));
        expectedNadja.setRoles(Set.of(roleDao.save(userUtil.getUserRole())));
        userDao.save(expectedBoris);
        userDao.save(expectedNadja);

        Optional<User> actualBoris = userDao.findByEmail(userUtil.getBorisEmail());
        Optional<User> actualNadja = userDao.findByEmail(userUtil.getNadjaEmail());
        Assertions.assertFalse(actualBoris.isEmpty());
        Assertions.assertFalse(actualNadja.isEmpty());
        Assertions.assertEquals(expectedBoris.getId(), actualBoris.get().getId());
        Assertions.assertEquals(expectedBoris.getEmail(), actualBoris.get().getEmail());
        Assertions.assertEquals(expectedNadja.getId(), actualNadja.get().getId());
        Assertions.assertEquals(expectedNadja.getEmail(), actualNadja.get().getEmail());
    }

    @Test
    void findByEmail_notExistentUser_notOk() {
        try{
        userDao.findByEmail("john@.ua").get();
        }catch (NoSuchElementException e) {
            Assertions.assertEquals("No value present", e.getMessage());
            return;
        }
        Assertions.fail("Expected NoSuchElementException while trying to get not existent user");
    }
    @Test
    void findById_validId_ok() {
        User expectedBoris = userDao.save(userUtil.getUserBoris());
        User expectedNadja = userDao.save(userUtil.getUserNadja());
        User actualBoris = userDao.findById(expectedBoris.getId()).get();
        User actualNadja = userDao.findById(expectedNadja.getId()).get();
        Assertions.assertNotNull(actualBoris);
        Assertions.assertNotNull(actualNadja);
        Assertions.assertEquals(expectedBoris.getEmail(), actualBoris.getEmail());
        Assertions.assertEquals(expectedBoris.getId(), actualBoris.getId());
        Assertions.assertEquals(expectedNadja.getEmail(), actualNadja.getEmail());
        Assertions.assertEquals(expectedNadja.getId(), actualNadja.getId());
    }

    @Test
    void findById_notExistentId_notOk() {
        Optional<User> actual = userDao.findById(4L);
        Assertions.assertTrue(actual.isEmpty());
    }
}

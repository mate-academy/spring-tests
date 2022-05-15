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
    private UserUtilForTest utilUtil;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class, User.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        utilUtil = new UserUtilForTest();
    }

    @Test
    void saveUser_ok() {
        Role admin = roleDao.save(utilUtil.getAdminRole());
        Role user = roleDao.save(utilUtil.getUserRole());
        User expectedBoris = utilUtil.getUserBoris();
        User expectedNadja = utilUtil.getUserNadja();
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
    void findUserByEmail_ok() {
        User expectedBoris = utilUtil.getUserBoris();
        User expectedNadja = utilUtil.getUserNadja();
        expectedBoris.setRoles(Set.of(roleDao.save(utilUtil.getAdminRole())));
        expectedNadja.setRoles(Set.of(roleDao.save(utilUtil.getUserRole())));
        userDao.save(expectedBoris);
        userDao.save(expectedNadja);

        Optional<User> actualBoris = userDao.findByEmail(utilUtil.getBorisEmail());
        Optional<User> actualNadja = userDao.findByEmail(utilUtil.getNadjaEmail());
        Assertions.assertFalse(actualBoris.isEmpty());
        Assertions.assertFalse(actualNadja.isEmpty());
        Assertions.assertEquals(expectedBoris.getId(), actualBoris.get().getId());
        Assertions.assertEquals(expectedBoris.getEmail(), actualBoris.get().getEmail());
        Assertions.assertEquals(expectedNadja.getId(), actualNadja.get().getId());
        Assertions.assertEquals(expectedNadja.getEmail(), actualNadja.get().getEmail());
    }

    @Test
    void findUserByEmail_noSuchElementException() {
        try{
        userDao.findByEmail("john@.ua").get();
        }catch (NoSuchElementException e) {
            Assertions.assertEquals("No value present", e.getMessage());
            return;
        }
        Assertions.fail();
    }
    @Test
    void findUserById_ok() {
        User expectedBoris = userDao.save(utilUtil.getUserBoris());
        User expectedNadja = userDao.save(utilUtil.getUserNadja());
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
    void findUserById_notOk() {
        Optional<User> actual = userDao.findById(4L);
        Assertions.assertTrue(actual.isEmpty());
    }
}
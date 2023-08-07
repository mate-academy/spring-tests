package mate.academy.dao.impl;

import java.util.NoSuchElementException;
import java.util.Set;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private UserDaoImpl userDao;
    private RoleDaoImpl roleDao;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
    }

    @Test
    void save_Ok() {
        User user = new User();
        user.setPassword("1234");
        user.setEmail("id@gmail.com");
        Role userRole = new Role();
        Role adminRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        adminRole.setRoleName(Role.RoleName.ADMIN);
        roleDao.save(userRole);
        roleDao.save(adminRole);
        user.setRoles(Set.of(userRole, adminRole));
        Assertions.assertNotNull(userDao.save(user));
    }

    @Test
    void save_NotOk_Throw_Exception() {
        User user = null;
        Assertions.assertThrows(DataProcessingException.class,
                () -> userDao.save(user));
    }

    @Test
    void findByEmail_Ok() {
        String email = "id@gmail.com";
        User user = new User();
        user.setPassword("1234");
        user.setEmail(email);
        Role userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        roleDao.save(userRole);
        user.setRoles(Set.of(userRole));
        userDao.save(user);
        Assertions.assertNotNull(userDao.findByEmail(email));
        Assertions.assertEquals(email, userDao.findByEmail(email).get().getEmail());
    }

    @Test
    void findByEmail_Not_Ok_Throw_Exception() {
        String email = "id@gmail.com";
        User user = new User();
        user.setPassword("1234");
        user.setEmail(email);
        Role userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        roleDao.save(userRole);
        user.setRoles(Set.of(userRole));
        userDao.save(user);
        Assertions.assertThrows(NoSuchElementException.class,
                () -> userDao.findByEmail("d@gmail.com").get());
    }
}

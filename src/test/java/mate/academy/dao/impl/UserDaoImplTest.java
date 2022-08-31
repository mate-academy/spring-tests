package mate.academy.dao.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoImplTest extends AbstractTest {
    private UserDao userDao;
    private RoleDao roleDao;
    private User user;
    private Role userRole;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class, Role.RoleName.class};
    }

    @BeforeEach
    public void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        userRole = new Role(Role.RoleName.USER);
        user = new User();
        user.setEmail("bob@i.ua");
        user.setPassword("1234");
    }

    @Test
    public void save_ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user.getEmail(), actual.getEmail());
        Assertions.assertEquals(user.getPassword(), actual.getPassword());
    }

    @Test
    public void findByEmail_ok() {
        user.setRoles(Set.of(userRole));
        Role actualRole = roleDao.save(userRole);
        User actualUser = userDao.save(user);
        Optional<User> actualOption = userDao.findByEmail(user.getEmail());
        Assertions.assertTrue(actualOption.isPresent());
        Assertions.assertEquals(user.getEmail(), actualOption.get().getEmail());
    }

    @Test
    public void findById_ok() {
        user.setRoles(Set.of(userRole));
        Role actualRole = roleDao.save(userRole);
        User actualUser = userDao.save(user);
        Optional<User> actualOptional = userDao.findById(1L);
        Assertions.assertTrue(actualOptional.isPresent());
        Assertions.assertEquals(user.getId(), actualOptional.get().getId());
        Assertions.assertEquals(user.getEmail(), actualOptional.get().getEmail());
    }
}

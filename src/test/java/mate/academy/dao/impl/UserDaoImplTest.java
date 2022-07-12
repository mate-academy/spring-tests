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
    private Role useRole;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class, Role.RoleName.class};
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        useRole = new Role(Role.RoleName.USER);
        user = new User();
        user.setPassword("1234");
        user.setEmail("bob@i.ua");
    }

    @Test
    void save_Ok() {
        User actual = userDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void findByEmail_Ok() {
        user.setRoles(Set.of(useRole));
        Role actualRole = roleDao.save(useRole);
        User actualUser = userDao.save(user);
        Optional<User> actualOptional = userDao.findByEmail(user.getEmail());
        Assertions.assertNotNull(actualOptional);
        Assertions.assertEquals(user.getEmail(), actualOptional.get().getEmail());
    }

    @Test
    void findById_Ok() {
        user.setRoles(Set.of(useRole));
        Role actualRole = roleDao.save(useRole);
        User actualUser = userDao.save(user);
        Optional<User> actualOptional = userDao.findById(1L);
        Assertions.assertNotNull(actualOptional.orElse(null));
        Assertions.assertEquals(user.getId(), actualOptional.get().getId());
        Assertions.assertEquals(user.getEmail(), actualOptional.get().getEmail());
    }
}

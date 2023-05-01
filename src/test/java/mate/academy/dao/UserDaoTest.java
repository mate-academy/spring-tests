package mate.academy.dao;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserDaoTest extends AbstractTest {
    private User userAlice;
    private User userBob;
    private UserDao userDao;
    
    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }
    
    @BeforeEach
    void setUp() {
        SessionFactory sessionFactory = getSessionFactory();
        Role adminRole = new Role(Role.RoleName.ADMIN);
        RoleDao roleDao = new RoleDaoImpl(sessionFactory);
        roleDao.save(adminRole);
        userAlice = new User();
        userAlice.setEmail("alice@gmail.com");
        userAlice.setPassword("qwerty");
        userAlice.setRoles(Set.of(adminRole));
        userBob = new User();
        userBob.setEmail("bob@gmail.com");
        userBob.setPassword("1234");
        userDao = new UserDaoImpl(sessionFactory);
        userDao.save(userAlice);
    }
    
    @Test
    void save_ok() {
        User actual = userDao.save(userBob);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(2L, actual.getId());
    }
    
    @Test
    void findByEmail_ok() {
        Optional<User> actual = userDao.findByEmail(userAlice.getEmail());
        Assertions.assertNotNull(actual.get());
        Assertions.assertEquals(userAlice.getEmail(), actual.get().getEmail());
    }
    
    @Test
    void findByEmail_userNotFound_notOk() {
        Optional<User> actual = userDao.findByEmail(userBob.getEmail());
        Assertions.assertThrows(NoSuchElementException.class, actual::get);
    }
    
    @Test
    void findById_ok() {
        Optional<User> actual = userDao.findById(1L);
        Assertions.assertNotNull(actual.get());
        Assertions.assertEquals(userAlice.getEmail(), actual.get().getEmail());
    }
    
    @Test
    void findById_userNotFound_notOk() {
        Optional<User> actual = userDao.findById(2L);
        Assertions.assertThrows(NoSuchElementException.class, actual::get);
    }
}

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
    private static final String EXISTENT_USER_EMAIL = "existentuser@gmail.com";
    private static final String NOT_EXISTENT_USER_EMAIL = "notexistentuser@gmail.com";
    private UserDao userDao;
    private RoleDao roleDao;
    private User existentUser;
    private Role userRole = new Role(Role.RoleName.USER);
    private Role adminRole = new Role(Role.RoleName.ADMIN);

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleDao.save(userRole);
        roleDao.save(adminRole);
        existentUser = new User();
        existentUser.setEmail("existentuser@gmail.com");
        existentUser.setPassword("12345");
        existentUser.setRoles(Set.of(userRole));
        userDao.save(existentUser);
    }

    @Test
    void saveUser_Ok() {
        Role roleUser = new Role(Role.RoleName.USER);
        roleDao.save(roleUser);
        User newUser = new User();
        newUser.setEmail("bob@i.ua");
        newUser.setPassword("qwerty");
        newUser.setRoles(Set.of(roleUser));
        userDao.save(newUser);
        Assertions.assertNotNull(newUser);
        Assertions.assertEquals(2L, newUser.getId());
        Assertions.assertEquals(newUser.getEmail(),newUser.getEmail());
        Assertions.assertTrue(newUser.getRoles().contains(roleUser));
        Assertions.assertEquals(newUser.getPassword(),newUser.getPassword());
    }

    @Test
    void saveUserWithNonPersistentRole_dataProcessingException_NotOk() {
        Role role = new Role(Role.RoleName.USER);
        User user = new User();
        user.setEmail("alex@i.ua");
        user.setPassword("qwerty");
        user.setRoles(Set.of(role));
        Assertions.assertThrows(DataProcessingException.class,() -> userDao.save(user));
    }

    @Test
    void findByEmail_existentUser_Ok() {
        Optional<User> byEmail = userDao.findByEmail(EXISTENT_USER_EMAIL);
        Assertions.assertTrue(byEmail.isPresent());
    }

    @Test
    void findByEmail_notExistentUser_OptionalEmpty() {
        Optional<User> byEmail = userDao.findByEmail(NOT_EXISTENT_USER_EMAIL);
        Assertions.assertTrue(byEmail.isEmpty());
    }

    @Test
    void findById_existentUser_Ok() {
        Optional<User> optionalUserById = userDao.findById(1L);
        Assertions.assertTrue(optionalUserById.isPresent());
    }

    @Test
    void findById_notExistentUser_optionalEmpty() {
        Optional<User> optionalUserById = userDao.findById(9L);
        Assertions.assertTrue(optionalUserById.isEmpty());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {User.class, Role.class};
    }
}

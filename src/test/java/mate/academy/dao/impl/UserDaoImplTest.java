package mate.academy.dao.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.RoleDao;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserDaoImplTest extends AbstractTest {
    private static final String INVALID_EMAIL = "Invalid";
    private static final Long INVALID_ID = 0L;
    private static PasswordEncoder passwordEncoder;
    private UserDao userDao;
    private User user;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeAll
    static void beforeAll() {
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
    }

    @BeforeEach
    public void setUp() {
        userDao = new UserDaoImpl(getSessionFactory());
        RoleDao roleDao = new RoleDaoImpl(getSessionFactory());
        Role role = new Role(Role.RoleName.USER);
        roleDao.save(role);
        user = new User();
        user.setEmail("test@email.com");
        user.setPassword(passwordEncoder.encode("Test1234"));
        user.setRoles(Set.of(roleDao.getRoleByName(Role.RoleName.USER.name())
                .orElseThrow(() -> new RuntimeException("Can't get role by role name "
                        + Role.RoleName.USER.name()))));
    }

    @Test
    public void saveUser_Ok() {
        User savedUser = userDao.save(user);
        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals(user, savedUser);
    }

    @Test
    public void findByEmail_ExistentEmail_Ok() {
        userDao.save(user);
        Optional<User> retrievedUser = userDao.findByEmail(user.getEmail());
        Assertions.assertNotNull(retrievedUser);
        Assertions.assertTrue(retrievedUser.isPresent(),
                "User with inputted email doesn't present");
        Assertions.assertEquals(user.getEmail(), retrievedUser.get().getEmail());
    }

    @Test
    public void findByEmail_NotExistentEmail_NotOk() {
        userDao.save(user);
        Optional<User> retrievedUserByEmail = userDao.findByEmail(INVALID_EMAIL);
        Assertions.assertFalse(retrievedUserByEmail.isPresent(),
                "User with inputted email doesn't present");
    }

    @Test
    public void findById_ExistentId_Ok() {
        userDao.save(user);
        Optional<User> retrievedUserById = userDao.findById(user.getId());
        Assertions.assertNotNull(retrievedUserById);
        Assertions.assertTrue(retrievedUserById.isPresent(),
                "User with inputted id doesn't present");
        Assertions.assertEquals(user.getId(), retrievedUserById.get().getId());
    }

    @Test
    public void findById_NotExistentId_NotOk() {
        userDao.save(user);
        Optional<User> retrievedUserById = userDao.findById(INVALID_ID);
        Assertions.assertFalse(retrievedUserById.isPresent(),
                "User with inputted id doesn't present");
    }
}

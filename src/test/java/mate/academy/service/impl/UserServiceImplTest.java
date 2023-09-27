package mate.academy.service.impl;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private static final Long INVALID_ID = 0L;
    private static final String INVALID_EMAIL = "Invalid";
    private static UserDao userDao;
    private static PasswordEncoder passwordEncoder;
    private static UserService userService;
    private static User user;

    @BeforeAll
    static void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @BeforeEach
    public void injectUser() {
        user = new User();
        user.setEmail("user@email.com");
        user.setPassword(passwordEncoder.encode("User1234"));
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    public void saveUser_Ok() {
        Mockito.when(userDao.save(any(User.class))).thenReturn(user);
        User savedUser = userService.save(user);
        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals(user, savedUser);
    }

    @Test
    public void findById_ExistentId_Ok() {
        Mockito.when(userDao.findById(user.getId()))
                .thenReturn(Optional.of(user));
        Optional<User> retrievedUserById = userService.findById(user.getId());
        Assertions.assertNotNull(retrievedUserById);
        Assertions.assertTrue(retrievedUserById.isPresent(),
                "User with inputted id doesn't present");
        Assertions.assertEquals(user, retrievedUserById.get());
    }

    @Test
    public void findById_NotExistentId_NotOk() {
        Mockito.when(userDao.findById(user.getId()))
                .thenReturn(Optional.of(user));
        Optional<User> retrievedUserById = userService.findById(INVALID_ID);
        Assertions.assertFalse(retrievedUserById.isPresent(),
                "User with inputted id doesn't present");
    }

    @Test
    public void findByEmail_ExistentEmail_Ok() {
        Mockito.when(userDao.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));
        Optional<User> retrievedUserByEmail = userService.findByEmail(user.getEmail());
        Assertions.assertNotNull(retrievedUserByEmail);
        Assertions.assertTrue(retrievedUserByEmail.isPresent(),
                "User with inputted email doesn't present");
        Assertions.assertEquals(user, retrievedUserByEmail.get());
    }

    @Test
    public void findByEmail_NotExistentEmail_NotOk() {
        Mockito.when(userDao.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));
        Optional<User> retrievedUserByEmail = userService.findByEmail(INVALID_EMAIL);
        Assertions.assertFalse(retrievedUserByEmail.isPresent(),
                "User with inputted email doesn't present");
    }
}

package mate.academy.service.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private static UserService userService;
    private static UserDao userDao;
    private static PasswordEncoder passwordEncoder;
    private static User existentUser;
    private static User existentAdmin;
    private static Role userRole;
    private static Role adminRole;

    @BeforeAll
    static void beforeAll() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userDao,passwordEncoder);
        userRole = new Role(Role.RoleName.USER);
        adminRole = new Role(Role.RoleName.ADMIN);
        existentAdmin = new User();
        existentAdmin.setId(1L);
        existentAdmin.setEmail("admin@i.ua");
        existentAdmin.setPassword("ytrewq");
        existentAdmin.setRoles(Set.of(adminRole));
        existentUser = new User();
        existentUser.setId(2L);
        existentUser.setEmail("user@i.ua");
        existentUser.setPassword("qwerty");
        existentUser.setRoles(Set.of(userRole));
    }

    @Test
    void saveUser_userWithRoleUser_Ok() {
        String email = "bob@i.ua";
        String password = "12345";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(Set.of(userRole));
        Mockito.when(userDao.save(user)).thenAnswer(invocationOnMock -> {
            User userFromArgument = (User) invocationOnMock.getArguments()[0];
            userFromArgument.setId(3L);
            return userFromArgument;
        });
        User savedUser = userService.save(user);
        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals(3L, savedUser.getId());
        Assertions.assertEquals(email,savedUser.getEmail());
        Assertions.assertTrue(passwordEncoder.matches(password, savedUser.getPassword()));
        Assertions.assertTrue(savedUser.getRoles().size() == 1
                && savedUser.getRoles().contains(userRole));
    }

    @Test
    void saveUser_userWithRoleAdmin_Ok() {
        String email = "alice@i.ua";
        String password = "54321";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(Set.of(adminRole));
        Mockito.when(userDao.save(user)).thenAnswer(invocationOnMock -> {
            User userFromArgument = (User) invocationOnMock.getArguments()[0];
            userFromArgument.setId(4L);
            return userFromArgument;
        });
        User savedUser = userService.save(user);
        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals(4L, savedUser.getId());
        Assertions.assertEquals(email,savedUser.getEmail());
        Assertions.assertTrue(passwordEncoder.matches(password, savedUser.getPassword()));
        Assertions.assertTrue(savedUser.getRoles().size() == 1
                && savedUser.getRoles().contains(adminRole));
    }

    @Test
    void findById_existentUserById_Ok() {
        Long id = 2L;
        Mockito.when(userDao.findById(id)).thenReturn(Optional.of(existentUser));
        Optional<User> userById = userService.findById(id);
        Assertions.assertTrue(userById.isPresent());
        Assertions.assertEquals(id, userById.get().getId());
    }

    @Test
    void findById_nonExistentUserById_optionalEmpty() {
        Long id = 9L;
        Mockito.when(userDao.findById(id)).thenReturn(Optional.empty());
        Optional<User> userById = userService.findById(id);
        Assertions.assertTrue(userById.isEmpty());
    }

    @Test
    void findByEmail_existentUser_Ok() {
        String email = "user@i.ua";
        Mockito.when(userDao.findByEmail(email)).thenReturn(Optional.of(existentUser));
        Optional<User> userByEmail = userService.findByEmail(email);
        Assertions.assertTrue(userByEmail.isPresent());
    }

    @Test
    void findByEmail_nonExistentUser_optionalEmpty() {
        String email = "alex@i.ua";
        Mockito.when(userDao.findByEmail(email)).thenReturn(Optional.empty());
        Optional<User> userByEmail = userService.findByEmail(email);
        Assertions.assertTrue(userByEmail.isEmpty());
    }
}

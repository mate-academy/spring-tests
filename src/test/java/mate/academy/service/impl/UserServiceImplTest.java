package mate.academy.service.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private static final String EMAIL = "email@meta.ua";
    private static final String PASSWORD = "123";
    private static final Set<Role> ROLES = Set.of(new Role(Role.RoleName.USER));
    private static User user;
    private UserService userService;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(ROLES);
    }

    @Test
    void save_ok() {
        String encodedPassword = "456";

        Mockito.when(passwordEncoder.encode(user.getPassword())).thenReturn(encodedPassword);
        Mockito.when(userDao.save(user)).thenReturn(user);

        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.getEmail(), EMAIL);
        Assertions.assertEquals(actual.getRoles(), ROLES);
        Assertions.assertEquals(actual.getPassword(), encodedPassword);
    }

    @Test
    void findById_ok() {
        Long id = 1L;
        user.setId(id);
        Optional<User> optionalUser = Optional.of(user);

        Mockito.when(userDao.findById(id)).thenReturn(optionalUser);

        Optional<User> userById = userService.findById(id);
        Assertions.assertNotNull(userById.get());
        Assertions.assertEquals(userById.get().getEmail(), EMAIL);
        Assertions.assertEquals(userById.get().getRoles(), ROLES);
        Assertions.assertEquals(userById.get().getPassword(), PASSWORD);
    }

    @Test
    void findByEmail_ok() {
        Optional<User> optionalUser = Optional.of(user);

        Mockito.when(userDao.findByEmail(EMAIL)).thenReturn(optionalUser);

        Optional<User> userById = userService.findByEmail(EMAIL);
        Assertions.assertNotNull(userById.get());
        Assertions.assertEquals(userById.get().getEmail(), EMAIL);
        Assertions.assertEquals(userById.get().getRoles(), ROLES);
        Assertions.assertEquals(userById.get().getPassword(), PASSWORD);
    }

    @Test
    void findById_wrongId_notOk() {
        Long wrongId = 2L;

        Mockito.when(userDao.findById(wrongId)).thenReturn(Optional.empty());

        Optional<User> userById = userService.findById(wrongId);
        Assertions.assertEquals(userById, Optional.empty());
    }

    @Test
    void findByEmail_wrongEmail_notOk() {
        String wrongEmail = "wrong@meta.ua";

        Mockito.when(userDao.findByEmail(wrongEmail)).thenReturn(Optional.empty());

        Optional<User> userById = userService.findByEmail(wrongEmail);
        Assertions.assertEquals(userById, Optional.empty());
    }
}

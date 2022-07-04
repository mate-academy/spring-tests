package mate.academy.service.impl;

import java.util.NoSuchElementException;
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
    private UserService userService;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
    }

    @Test
    void save_ok() {
        String email = "email@meta.ua";
        String password = "123";
        String encodedPassword = "456";
        Set<Role> roles = Set.of(new Role(Role.RoleName.USER));
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(roles);

        Mockito.when(passwordEncoder.encode(user.getPassword())).thenReturn(encodedPassword);
        Mockito.when(userDao.save(user)).thenReturn(user);

        User actual = userService.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.getEmail(), email);
        Assertions.assertEquals(actual.getRoles(), roles);
        Assertions.assertEquals(actual.getPassword(), encodedPassword);
    }

    @Test
    void findById_ok() {
        String email = "email@meta.ua";
        String password = "123";
        Set<Role> roles = Set.of(new Role(Role.RoleName.USER));
        Long id = 1L;
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(roles);
        user.setId(id);
        Optional<User> optionalUser = Optional.of(user);

        Mockito.when(userDao.findById(id)).thenReturn(optionalUser);

        Optional<User> userById = userService.findById(id);
        Assertions.assertNotNull(userById.get());
        Assertions.assertEquals(userById.get().getEmail(), email);
        Assertions.assertEquals(userById.get().getRoles(), roles);
        Assertions.assertEquals(userById.get().getPassword(), password);
    }

    @Test
    void findByEmail_ok() {
        String email = "email@meta.ua";
        String password = "123";
        Set<Role> roles = Set.of(new Role(Role.RoleName.USER));
        Long id = 1L;
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(roles);
        user.setId(id);
        Optional<User> optionalUser = Optional.of(user);

        Mockito.when(userDao.findByEmail(email)).thenReturn(optionalUser);

        Optional<User> userById = userService.findByEmail(email);
        Assertions.assertNotNull(userById.get());
        Assertions.assertEquals(userById.get().getEmail(), email);
        Assertions.assertEquals(userById.get().getRoles(), roles);
        Assertions.assertEquals(userById.get().getPassword(), password);
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
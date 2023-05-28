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
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private UserService userService;
    private UserDao userDao;
    private User user;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        Mockito.when(passwordEncoder.encode(Mockito.anyString()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user = new User();
        user.setId(1L);
        user.setEmail("valid@gmail.com");
        user.setPassword("password");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void save_validUser_ok() {
        User expected = user.clone();
        Mockito.when(userDao.findByEmail(Mockito.anyString()))
                .thenReturn(Optional.of(expected.clone()));
        User actual = userService.findByEmail(expected.getEmail()).get();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findById_validId_ok() {
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userDao.findById(user.getId())).thenReturn(Optional.of(user.clone()));
        Optional<User> userOptional = userService.findById(user.getId());

        Assertions.assertNotNull(userOptional);
        Assertions.assertTrue(userOptional.isPresent());
        Assertions.assertEquals(user, userOptional.get());
    }

    @Test
    void findByEmail_validEmail_ok() {
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        Mockito.when(userDao.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user.clone()));
        Optional<User> userOptional = userService.findByEmail(user.getEmail());

        Assertions.assertNotNull(userOptional);
        Assertions.assertTrue(userOptional.isPresent());
        Assertions.assertEquals(user, userOptional.get());
    }
}

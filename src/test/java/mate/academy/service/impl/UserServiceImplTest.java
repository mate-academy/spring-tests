package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private final UserDao userDao = mock(UserDao.class);
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserService userService = new UserServiceImpl(userDao, passwordEncoder);
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("bob@bobmail.com");
        user.setPassword("1234");
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
    }

    @Test
    void save_validUser_Ok() {
        User saved = new User();
        saved.setId(1L);
        saved.setEmail(user.getEmail());
        saved.setPassword(user.getPassword());
        saved.setRoles(user.getRoles());
        when(userDao.save(user)).thenReturn(saved);

        User actual = userService.save(user);
        assertNotNull(actual);
        assertEquals(saved, actual);
    }

    @Test
    void save_emailIsNull_notOk() {
        user.setEmail(null);
        when(userDao.save(user)).thenThrow(DataProcessingException.class);
        assertThrows(DataProcessingException.class, () -> userService.save(user));
    }

    @Test
    void findByEmail_userFound_ok() {
        user.setId(1L);
        when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Optional<User> actual = userService.findByEmail(user.getEmail());
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(user.getId(), actual.get().getId());
        Assertions.assertEquals(user.getEmail(), actual.get().getEmail());
        Assertions.assertEquals(user.getPassword(), actual.get().getPassword());
        Assertions.assertEquals(user.getRoles().size(), actual.get().getRoles().size());
    }

    @Test
    void findByEmail_UserNotFound_Ok() {
        when(userDao.findByEmail("wrongEmail")).thenReturn(Optional.empty());

        Optional<User> actual = userService.findByEmail("wrongEmail");
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void save_userIsNull_notOk() {
        assertThrows(NullPointerException.class, () -> userService.save(null));
    }
}

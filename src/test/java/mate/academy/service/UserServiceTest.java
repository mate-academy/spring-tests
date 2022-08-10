package mate.academy.service;

import static mate.academy.model.Role.RoleName.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.dao.impl.UserDaoImpl;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {
    private static UserDao userDao;
    private static PasswordEncoder passwordEncoder;
    private static UserService userService;
    private static User bob;

    @BeforeAll
    static void beforeAll() {
        userDao = Mockito.mock(UserDaoImpl.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao,passwordEncoder);
        Role role = new Role(USER);
        bob = getTestUser("bob@gmail.com", "qwEds!23", Set.of(role));
    }

    @Test
    void save_validUserDao_Ok() {
        Mockito.when(userDao.save(bob)).thenReturn(bob);
        User actual = userService.save(bob);
        assertNotNull(actual);
        assertEquals(bob, actual);
        assertEquals(bob.getEmail(), actual.getEmail());
        assertEquals(bob.getPassword(), actual.getPassword());
    }

    @Test
    void save_nonExisting_notOk() {
        assertThrows(Exception.class,
                () -> userService.save(any()));
    }

    @Test
    void findById_valid_Ok() {
        long id = 1;
        Mockito.when(userDao.findById(id)).thenReturn(Optional.of(bob));
        Optional<User> actual = userService.findById(id);
        assertTrue(actual.isPresent());
        assertEquals(actual.get(), bob);
    }

    @Test
    void findById_nonExistingId_notOk() {
        Mockito.when(userDao.findById(any())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class,
                () -> userService.findById(-1L).get());
    }

    @Test
    void findByEmail_valid_Ok() {
        Mockito.when(userDao.findByEmail(bob.getEmail())).thenReturn(Optional.of(bob));
        Optional<User> actualOptionalUserByEmail = userService.findByEmail(bob.getEmail());
        assertTrue(actualOptionalUserByEmail.isPresent());
        assertEquals(actualOptionalUserByEmail.get().getEmail(), bob.getEmail());
    }

    @Test
    void findByEmail_nonExistingId_notOk() {
        assertThrows(NoSuchElementException.class,
                () -> userService.findByEmail("asd@i.ua").get());
    }

    private static User getTestUser(String email, String password, Set<Role> roles) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(roles);
        return user;
    }
}

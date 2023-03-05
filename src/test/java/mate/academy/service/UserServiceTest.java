package mate.academy.service;

import static mate.academy.model.Role.RoleName.ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {
    private UserDao userDaoMock;
    private PasswordEncoder passwordEncoderMock;
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        userDaoMock = Mockito.mock(UserDao.class);
        passwordEncoderMock = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDaoMock, passwordEncoderMock);
        String email = "user@gmail.com";
        String password = "password";
        user = new User();
        user.setEmail(email);
        user.setPassword(password);
        Set<Role> roles = Set.of(new Role(ADMIN));
        user.setRoles(roles);
    }

    @Test
    void save_ValidUser_Ok() {
        Mockito.when(passwordEncoderMock.encode(user.getPassword())).thenReturn("encodedPassword");
        Mockito.when(userDaoMock.save(user)).thenReturn(user);
        User actual = userService.save(user);
        assertNotNull(actual);
        assertEquals(user, actual);
    }

    @Test
    void save_UserIsNull_NotOk() {
        assertThrows(RuntimeException.class, () -> {
            userService.save(null); });
    }

    @Test
    void findById_ValidId_Ok() {
        user.setId(1L);
        Mockito.when(userDaoMock.findById(user.getId())).thenReturn(Optional.of(user));
        Optional<User> optionalUser = userService.findById(user.getId());
        assertFalse(optionalUser.isEmpty());
        assertEquals(user, optionalUser.get());
    }

    @Test
    void findById_IdIsNull_NotOk() {
        assertThrows(RuntimeException.class, () -> {
            userService.findById(null); });
    }

    @Test
    void findByEmail_ValidEmail_Ok() {
        Mockito.when(userDaoMock.findByEmail("user@gmail.com")).thenReturn(Optional.of(user));
        Optional<User> optionalUser = userService.findByEmail(user.getEmail());
        assertFalse(optionalUser.isEmpty());
        assertEquals(user, optionalUser.get());
    }

    @Test
    void findByEmail_EmailIsNull_NotOk() {
        assertThrows(RuntimeException.class, () -> {
            userService.findByEmail(null); });
    }
}

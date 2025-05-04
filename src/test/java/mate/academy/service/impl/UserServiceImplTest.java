package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private static User admin;
    private UserDao userDao;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        admin = new User();
        admin.setEmail("admin@mail.com");
        admin.setPassword("superpassword");
        admin.setRoles(Set.of(new Role(Role.RoleName.ADMIN)));
        admin.setId(1L);

    }

    @Test
    void save_Ok() {
        Mockito.when(userDao.save(admin)).thenReturn(admin);
        User actual = userService.save(admin);
        assertNotNull(actual);
        assertEquals(admin, actual);
    }

    @Test
    void findById_Ok() {
        Mockito.when(userDao.findById(admin.getId())).thenReturn(Optional.of(admin));
        Optional<User> actual = userDao.findById(admin.getId());
        assertNotNull(actual);
        assertEquals(Optional.of(admin), actual);
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(admin.getEmail())).thenReturn(Optional.of(admin));
        Optional<User> actual = userDao.findByEmail(admin.getEmail());
        assertNotNull(actual);
        assertEquals(Optional.of(admin), actual);
    }

    @Test
    void findById_nonExistentId_NotOk() {
        Mockito.when(userDao.findById(admin.getId())).thenReturn(Optional.of(admin));
        Optional<User> actual = userDao.findById(5L);
        assertNotNull(actual);
        assertFalse(actual.isPresent());
    }

    @Test
    void findByEmail_nonExistentEmail_NotOk() {
        Mockito.when(userDao.findByEmail(admin.getEmail())).thenReturn(Optional.of(admin));
        Optional<User> actual = userDao.findByEmail("a@b.com");
        assertNotNull(actual);
        assertFalse(actual.isPresent());
    }
}

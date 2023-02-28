package mate.academy.service.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private static final String EMAIL = "bob@domain.com";
    private static final String PASSWORD = "1234";
    private static final Long ID = 1L;
    @Mock
    private UserDao userDao;
    @Spy
    private BCryptPasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;
    private User user;

    @BeforeEach
    void setUp() {
        user = createUser();
        user.setId(ID);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    @Test
    void save_Ok() {
        User inputUser = createUser();
        Mockito.when(userDao.save(inputUser)).thenReturn(user);
        User actual = userService.save(inputUser);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void findById_Ok() {
        Mockito.when(userDao.findById(ID)).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findById(ID);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(user.getId(), actual.get().getId());
    }

    @Test
    void findById_NotExistentId_NotOk() {
        Mockito.when(userDao.findById(ID)).thenReturn(Optional.empty());
        Optional<User> actual = userService.findById(ID);
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail(EMAIL);
        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(user.getEmail(), actual.get().getEmail());
    }

    @Test
    void findByEmail_NotExistentUser_NotOk() {
        Mockito.when(userDao.findByEmail(EMAIL)).thenReturn(Optional.empty());
        Optional<User> actual = userService.findByEmail(EMAIL);
        Assertions.assertTrue(actual.isEmpty());
    }

    private User createUser() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRoles(Set.of(new Role(Role.RoleName.USER)));
        return user;
    }
}

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
    private static final String EMAIL = "bob@i.ua";
    private static final String PASSWORD = "12345";
    private static final Long ID = 1L;
    @Mock
    private UserDao userDao;
    @InjectMocks
    private UserServiceImpl userService;
    @Spy
    private BCryptPasswordEncoder passwordEncoder;
    private User user;
    private Optional<User> userOptional;

    @BeforeEach
    void setUp() {
        user = createUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setId(ID);
        userOptional = Optional.of(user);
    }

    @Test
    void saveUser_ok() {
        User inputUser = createUser();
        Mockito.when(userDao.save(inputUser)).thenReturn(user);
        Assertions.assertEquals(user, userService.save(inputUser));
    }

    @Test
    void findById_ok() {
        Mockito.when(userDao.findById(ID)).thenReturn(userOptional);
        Assertions.assertEquals(userOptional, userService.findById(ID));
    }

    @Test
    void findByIncorrectId_notOk() {
        Mockito.when(userDao.findById(ID)).thenReturn(Optional.empty());
        Assertions.assertEquals(Optional.empty(), userService.findById(ID));
    }

    @Test
    void findByEmail_ok() {
        Mockito.when(userDao.findByEmail(EMAIL)).thenReturn(userOptional);
        Assertions.assertEquals(userOptional, userService.findByEmail(EMAIL));
    }

    @Test
    void findByIncorrectEmail_notOk() {
        Mockito.when(userDao.findByEmail(EMAIL)).thenReturn(Optional.empty());
        Assertions.assertEquals(Optional.empty(), userService.findByEmail(EMAIL));
    }

    private User createUser() {
        return new User(EMAIL, PASSWORD, Set.of(new Role(Role.RoleName.USER)));
    }
}

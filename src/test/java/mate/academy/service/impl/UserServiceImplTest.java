package mate.academy.service.impl;

import java.util.Optional;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class UserServiceImplTest {
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void SetUp() {
        userService = Mockito.mock(UserService.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
    }

    @Test
    void save() {
        User user = getTestUserWithId();
        Mockito.when(passwordEncoder.encode(Mockito.any())).thenReturn(user.getPassword());
        Mockito.when(userService.save(Mockito.any())).thenReturn(user);
        User savedUser = userService.save(user);
        assertEquals(savedUser.toString(), user.toString());
    }

    @Test
    void findById() {
        User user = getTestUserWithId();
        Mockito.when(passwordEncoder.encode(Mockito.any())).thenReturn(user.getPassword());
        Mockito.when(userService.findById(Mockito.any())).thenReturn(Optional.of(user));
        assertFalse(userService.findById(1L).isEmpty());
    }

    @Test
    void findByEmail_Ok() {
        User user = getTestUserWithId();
        Mockito.when(passwordEncoder.encode(Mockito.any())).thenReturn("");
        Mockito.when(userService.findByEmail("test@i.ua")).thenReturn(Optional.of(user));
        assertFalse(userService.findByEmail("test@i.ua").isEmpty());
    }

    private User getTestUserWithId() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@i.ua");
        user.setPassword("1234test");
        return user;
    }
}
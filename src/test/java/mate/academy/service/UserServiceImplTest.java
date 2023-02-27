package mate.academy.service;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class UserServiceImplTest {
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;
    private UserServiceImpl userServiceImpl;
    private User userRegister;
    private User userReturn;
    private User incorrectUserReturn;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        passwordEncoder = new BCryptPasswordEncoder();
        userServiceImpl = new UserServiceImpl(userDao, passwordEncoder);
        String password = passwordEncoder.encode("maxMax1234");
        userRegister = new User();
        userRegister.setEmail("max@max.com");
        userRegister.setPassword(password);
        userRegister.setRoles(Set.of(new Role(Role.RoleName.USER), new Role(Role.RoleName.ADMIN)));
        userReturn = new User();
        userReturn.setId(1L);
        userReturn.setEmail("max@max.com");
        userReturn.setPassword(password);
        userReturn.setRoles(Set.of(new Role(Role.RoleName.USER), new Role(Role.RoleName.ADMIN)));
        incorrectUserReturn = new User();
        incorrectUserReturn.setId(2L);
        incorrectUserReturn.setEmail("test@test.com");
        incorrectUserReturn.setPassword(passwordEncoder.encode("testTest1234"));
        incorrectUserReturn.setRoles(Set.of(new Role(Role.RoleName.USER)));
        when(userDao.save(userRegister)).thenReturn(userReturn);
        when(userDao.findById(1L)).thenReturn(Optional.of(userReturn));
        when(userDao.findById(2L)).thenReturn(Optional.empty());
        when(userDao.findByEmail("max@max.com")).thenReturn(Optional.of(userReturn));
        when(userDao.findByEmail("test@test.com")).thenReturn(Optional.empty());
    }

    @Test
    void save_correctData_ok() {
        assertEquals(userReturn, userServiceImpl.save(userRegister));
    }

    @Test
    void save_inCorrectData_ok() {
        assertNotEquals(incorrectUserReturn, userServiceImpl.save(userRegister));
    }

    @Test
    void findById_correctId_ok() {
        Optional<User> userOptional = userServiceImpl.findById(1L);
        assertNotNull(userOptional);
        assertEquals(1L, userOptional.get().getId());
    }

    @Test
    void findById_inCorrectId_notOk() {
        Optional<User> userOptional = userServiceImpl.findById(2L);
        assertNotNull(userOptional);
        assertEquals(Optional.empty(), userOptional);
    }

    @Test
    void findByEmail_correctEmail_ok() {
        Optional<User> userOptional = userServiceImpl.findByEmail("max@max.com");
        assertNotNull(userOptional);
        assertEquals("max@max.com", userOptional.get().getEmail());
    }

    @Test
    void findByEmail_inCorrectEmail_notOk() {
        Optional<User> userOptional = userServiceImpl.findByEmail("test@test.com");
        assertNotNull(userOptional);
        assertEquals(Optional.empty(), userOptional);
    }
}
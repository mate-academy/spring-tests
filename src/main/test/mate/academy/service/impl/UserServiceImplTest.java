package mate.academy.service.impl;

import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.model.User;
import mate.academy.service.UserService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalMatchers;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

class UserServiceImplTest {
    private static final User USER_RAW_PASS = new User();
    private static final User USER_ENCODED_PASS = new User();
    private static final User USER_SAVED = new User();
    private static final String VALID_EMAIL = "denis@mail.ru";
    private static final String INVALID_EMAIL = "invalid";
    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = -1L;
    private UserService userService;
    private UserDao userDao;
    private BCryptPasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        userDao = Mockito.mock(UserDao.class);
        encoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userDao, encoder);
        USER_RAW_PASS.setPassword("password");
        USER_ENCODED_PASS.setPassword(encoder.encode("password"));
        USER_SAVED.setPassword(encoder.encode("password"));
        USER_SAVED.setId(VALID_ID);
        USER_SAVED.setEmail(VALID_EMAIL);
    }

    @Test
    void save_ok() throws Exception {
        ReflectionTestUtils.setField(userService, "passwordEncoder", encoder, PasswordEncoder.class);
        Mockito.when(userDao.save(AdditionalMatchers.not(Mockito.eq(USER_RAW_PASS)))).thenReturn(USER_SAVED);
        User saved = userService.save(cloneUser(USER_RAW_PASS));
        assertNotNull(saved.getId());
        assertNotEquals(USER_RAW_PASS.getPassword(), saved.getPassword());
    }

    @Test
    void findById_ok() {
        Mockito.when(userDao.findById(VALID_ID)).thenReturn(Optional.of(USER_SAVED));
        assertTrue(userService.findById(VALID_ID).isPresent());
        assertEquals(VALID_ID, USER_SAVED.getId());
    }

    @Test
    void findById_notOk() {
        Mockito.when(userDao.findById(INVALID_ID)).thenReturn(Optional.empty());
        assertTrue(userService.findById(VALID_ID).isEmpty());
    }

    @Test
    void findByEmail_ok() {
        Mockito.when(userDao.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(USER_SAVED));
        assertTrue(userService.findByEmail(VALID_EMAIL).isPresent());
        assertEquals(VALID_EMAIL, USER_SAVED.getEmail());
    }

    @Test
    void findByEmail_notOk() {
        Mockito.when(userDao.findByEmail(INVALID_EMAIL)).thenReturn(Optional.empty());
        assertTrue(userService.findByEmail(VALID_EMAIL).isEmpty());
    }

    private User cloneUser(User userRawPass) {
        User user = new User();
        user.setPassword(userRawPass.getPassword());
        return user;
    }
}
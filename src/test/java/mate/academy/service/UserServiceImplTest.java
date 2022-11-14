package mate.academy.service;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {
    private static final long USER_ID = 1L;
    private static final String USER_EMAIL = "bob@i.ua";
    private static final String USER_EMAIL_CHANGED = "user@i.ua";
    private static final String USER_PASSWORD = "password";
    private static final String USER_ENCRYPTED_PASSWORD = "$2a$10$DuRPxCa5g4ghKqYO91HkneQWTVr"
            + "LjQNAyVqA38yAVWZBtapSa6kNm";
    private static final Role USER_ROLE = new Role(Role.RoleName.USER);
    private UserDao userDao;
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private final User user = new User();

    @BeforeEach
    void setUp() {
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userDao = Mockito.spy(UserDao.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);
        user.setId(USER_ID);
        user.setEmail(USER_EMAIL);
        user.setPassword(USER_ENCRYPTED_PASSWORD);
        user.setRoles(Set.of(USER_ROLE));
    }

    @Test
    void save_Ok() {
        User testUser = new User();
        testUser.setEmail(USER_EMAIL);
        testUser.setPassword(USER_PASSWORD);
        Mockito.when(userDao.save(testUser)).thenReturn(user);
        User actual = userService.save(testUser);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(USER_ID, actual.getId());
        Assertions.assertEquals(USER_EMAIL, actual.getEmail());
        Assertions.assertEquals(USER_ENCRYPTED_PASSWORD, actual.getPassword());
        Assertions.assertEquals(USER_ROLE, actual.getRoles().stream().findFirst().get());
    }

    @Test
    void findById() {
        Mockito.when(userDao.findById(USER_ID)).thenReturn(Optional.of(user));

    }

    @Test
    void findByEmail() {
    }

    @Test
    void update() {
    }
}

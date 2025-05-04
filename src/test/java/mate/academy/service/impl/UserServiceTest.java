package mate.academy.service.impl;

import java.util.Optional;
import java.util.Set;
import mate.academy.dao.UserDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {
    private static UserService userService;

    @BeforeAll
    public static void setUp() {
        UserDao userDao = Mockito.mock(UserDao.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userDao, passwordEncoder);

        User bobWithId = getDummyUser();
        bobWithId.setId(1L);

        // for save test
        Mockito.when(userDao.save(ArgumentMatchers.any())).thenReturn(bobWithId);
        // for null save test
        Mockito.when(userDao.save(null)).thenThrow(DataProcessingException.class);
        // for findById test
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.of(bobWithId));
        // for not existing user test
        Mockito.when(userDao.findById(69L)).thenReturn(Optional.empty());
        // for negative id test
        Mockito.when(userDao.findById(-1L)).thenThrow(DataProcessingException.class);
        // for null id test
        Mockito.when(userDao.findById(null)).thenThrow(DataProcessingException.class);
        // for findByEmail test
        Mockito.when(userDao.findByEmail("bob@i.ua")).thenReturn(Optional.of(bobWithId));
        // for no such user by email test
        Mockito.when(userDao.findByEmail("elon.musk@space.x")).thenReturn(Optional.empty());
        // for null email test
        Mockito.when(userDao.findByEmail(null)).thenThrow(DataProcessingException.class);
    }

    @Test
    public void save_ok() {
        User bob = getDummyUser();
        User actual = userService.save(bob);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals("bob@i.ua", actual.getEmail());
        Assertions.assertEquals("1234", actual.getPassword());
        Assertions.assertEquals(1, actual.getRoles().size());
        Assertions.assertEquals(actual.getRoles().stream()
                .findFirst().get().getRoleName(), Role.RoleName.USER);
    }

    @Test
    public void save_nullInput_notOk() {
        Assertions.assertThrows(NullPointerException.class, () -> userService.save(null));
    }

    @Test
    public void findById_ok() {
        User actual = userService.findById(1L).get();
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals("bob@i.ua", actual.getEmail());
        Assertions.assertEquals("1234", actual.getPassword());
        Assertions.assertEquals(1, actual.getRoles().size());
        Assertions.assertEquals(actual.getRoles().stream()
                .findFirst().get().getRoleName(), Role.RoleName.USER);
    }

    @Test
    public void findById_noSuchUserById_ok() {
        Assertions.assertTrue(userService.findById(69L).isEmpty());
    }

    @Test
    public void findById_invalidId_notOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> userService.findById(-1L));
    }

    @Test
    public void findById_nullId_notOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> userService.findById(null));
    }

    @Test
    public void findByEmail_ok() {
        User actual = userService.findByEmail("bob@i.ua").get();
        Assertions.assertEquals(actual.getId(), 1L);
        Assertions.assertEquals(actual.getEmail(), "bob@i.ua");
        Assertions.assertEquals(actual.getPassword(), "1234");
        Assertions.assertEquals(actual.getRoles().size(), 1);
        Assertions.assertEquals(actual.getRoles().stream()
                .findFirst().get().getRoleName(), Role.RoleName.USER);
    }

    @Test
    public void findByEmail_noSuchUserByEmail_ok() {
        Assertions.assertTrue(userService.findByEmail("elon.musk@space.x").isEmpty());
    }

    @Test
    public void findByEmail_nullInput_notOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> userService.findByEmail(null));
    }

    private static User getDummyUser() {
        User bob = new User();
        Role userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        userRole.setId(2L);
        bob.setEmail("bob@i.ua");
        bob.setPassword("1234");
        bob.setRoles(Set.of(userRole));
        return bob;
    }
}

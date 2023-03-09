package mate.academy.service;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceTest {
    private static final Role TEST_ROLE = new Role(Role.RoleName.USER);
    private RoleService roleService;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_Ok() {
        Mockito.when(roleDao.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        Role actual = roleService.save(TEST_ROLE);
        Assertions.assertNotNull(actual, "Role shouldn't be null");
        Assertions.assertEquals(actual, TEST_ROLE,
                String.format("Should return role: %s, but was: %s", TEST_ROLE, actual));
    }

    @Test
    void getRoleByName_Ok() {
        Mockito.when(roleDao.getRoleByName(any())).thenReturn(Optional.of(TEST_ROLE));
        Role actual = roleService.getRoleByName("USER");
        Assertions.assertNotNull(actual, "Role shouldn't be null");
        Assertions.assertEquals(actual.getRoleName().name(), "USER",
                String.format("Should return role: %s, but was: %s", "USER",
                        actual.getRoleName().name()));
    }

    @Test
    void getRoleByName_roleNameIsIncorrect_notOk() {
        Mockito.when(roleDao.getRoleByName(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(RuntimeException.class, () -> {
            roleService.getRoleByName("USER");
        });
    }

    @Test
    void getRoleByName_roleNameIsnull_notOk() {
        Mockito.when(roleDao.getRoleByName(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(RuntimeException.class, () -> {
            roleService.getRoleByName(null);
        });
    }
}

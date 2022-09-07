package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private static RoleService roleService;
    private static RoleDao roleDao;
    private static Role role;

    @BeforeAll
    static void beforeAll() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        role = new Role(Role.RoleName.USER);
    }

    @BeforeEach
    void setUp() {
        role.setRoleName(Role.RoleName.USER);
    }

    @Test
    void save_role_OK() {
        Mockito.when(roleDao.save(any(Role.class))).thenReturn(role);
        Role actual = roleService.save(role);
        assertNotNull(actual);
        assertEquals("USER", actual.getRoleName().name());
    }

    @Test
    void getRoleByName_nullValue_notOK() {
        role.setRoleName(null);
        Mockito.when(roleDao.getRoleByName("USER"))
                .thenReturn(Optional.of(role));
        Assertions.assertThrows(RuntimeException.class,
                () -> roleService.getRoleByName(role.getRoleName().name()));
    }

    @Test
    void getRoleByName_OK() {
        role.setRoleName(Role.RoleName.ADMIN);
        Mockito.when(roleDao.getRoleByName("ADMIN")).thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(role.getRoleName().name());
        assertNotNull(actual);
        assertEquals("ADMIN", actual.getRoleName().name());
    }
}

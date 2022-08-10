package mate.academy.service;

import static mate.academy.model.Role.RoleName.ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceTest {
    private static final String NonExistentRole = "USR";
    private static RoleDao roleDao;
    private static RoleService roleService;
    private static Role adminRole;

    @BeforeAll
    static void beforeAll() {
        roleDao = Mockito.mock(RoleDaoImpl.class);
        adminRole = new Role(ADMIN);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_validRoleDao_OK() {
        Mockito.when(roleDao.save(adminRole)).thenReturn(adminRole);
        Role actual = roleService.save(adminRole);
        assertNotNull(actual);
        assertEquals(adminRole.getRoleName(), actual.getRoleName());
    }

    @Test
    void save_nonExistent_notOK() {
        assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName(null));
    }

    @Test
    void getRoleByName_validByName_Ok() {
        Mockito.when(roleDao.getRoleByName("ADMIN")).thenReturn(Optional.of(adminRole));
        Role actual = roleService.getRoleByName("ADMIN");
        assertNotNull(actual);
        assertEquals(adminRole.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_nonExistent_notOk() {
        assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName(NonExistentRole));
    }
}

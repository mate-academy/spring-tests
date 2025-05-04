package mate.academy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceTest {
    private static final String NOT_USER = "NOT_USER";
    private RoleService roleService;
    private RoleDao roleDao;
    private Role userRole;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        userRole = new Role(Role.RoleName.USER);
    }

    @Test
    void save_Ok() {
        Mockito.when(roleDao.save(userRole)).thenReturn(userRole);
        Role actual = roleService.save(userRole);
        assertNotNull(actual);
        assertEquals(userRole.getRoleName().name(), actual.getRoleName().name());
    }

    @Test
    void getRoleByName_Ok() {
        Mockito.when(roleDao.getRoleByName(userRole.getRoleName().name()))
                .thenReturn(Optional.of(userRole));
        Role actual = roleService.getRoleByName(userRole.getRoleName().name());
        assertNotNull(actual);
        assertEquals(userRole.getRoleName().name(), actual.getRoleName().name());
    }

    @Test
    void getRoleByName_notExistingRole_notOk() {
        assertThrows(NoSuchElementException.class, () -> roleService.getRoleByName(NOT_USER));
    }

    @Test
    void getRoleByName_nullRole_notOk() {
        assertThrows(NoSuchElementException.class, () -> roleService.getRoleByName(null));
    }
}

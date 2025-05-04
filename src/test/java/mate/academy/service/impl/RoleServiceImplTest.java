package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
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
    void save_validData_ok() {
        Mockito.when(roleDao.save(userRole)).thenReturn(userRole);
        Role actual = roleService.save(userRole);
        assertNotNull(actual);
        assertEquals(userRole.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_validData_ok() {
        Mockito.when(roleDao.getRoleByName(userRole.getRoleName().name()))
                .thenReturn(Optional.of(userRole));
        Role actual = roleService.getRoleByName(userRole.getRoleName().name());
        assertNotNull(actual);
        assertEquals(userRole.getRoleName(), actual.getRoleName());
    }
}

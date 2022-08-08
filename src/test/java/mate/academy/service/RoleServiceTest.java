package mate.academy.service;

import static mate.academy.model.Role.RoleName.ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceTest {
    private RoleDao roleDao;
    private RoleService roleService;
    private Role adminRole;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDaoImpl.class);
        adminRole = new Role(ADMIN);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_validRoleDao_OK() {
        Mockito.when(roleDao.save(any())).thenReturn(adminRole);
        Role actual = roleService.save(adminRole);
        assertNotNull(actual);
        assertEquals(adminRole.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_validByName_Ok() {
        Mockito.when(roleDao.getRoleByName("ADMIN")).thenReturn(Optional.of(adminRole));
        Role actual = roleService.getRoleByName("ADMIN");
        assertNotNull(actual);
        assertEquals(adminRole.getRoleName(), actual.getRoleName());
    }
}

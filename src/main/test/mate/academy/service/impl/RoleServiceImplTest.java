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
    private RoleDao roleDao;
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_validRole_Ok() {
        Role roleWithoutId = new Role(Role.RoleName.ADMIN);
        Role roleWithId = new Role(Role.RoleName.ADMIN);
        roleWithId.setId(42L);
        Mockito.when(roleDao.save(roleWithoutId)).thenReturn(roleWithId);

        Role actualRole = roleService.save(roleWithoutId);
        assertNotNull(actualRole);
        assertEquals(actualRole, roleWithId);
    }

    @Test
    void getRoleByName_existingName_Ok() {
        Role.RoleName roleType = Role.RoleName.ADMIN;
        Role roleWithId = new Role(roleType);
        roleWithId.setId(1L);
        Mockito.when(roleDao.getRoleByName("ADMIN")).thenReturn(Optional.of(roleWithId));

        Role actualRole = roleService.getRoleByName("ADMIN");
        assertNotNull(actualRole);
        assertEquals(actualRole, roleWithId);
    }
}
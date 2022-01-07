package mate.academy.service.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RoleServiceImplTest {
    private RoleServiceImpl roleService;

    @Test
    void save() {
        RoleDao roleDao = Mockito.mock(RoleDao.class);
        Role role = new Role(Role.RoleName.USER);
        role.setId(1L);
        Mockito.when(roleDao.save(Mockito.any())).thenReturn(role);
        roleService = new RoleServiceImpl(roleDao);
        Role savedRole = roleService.save(role);
        assertNotNull(savedRole.getId());
    }

    @Test
    void getRoleByName() {
        RoleDao roleDao = Mockito.mock(RoleDao.class);
        Role role = new Role(Role.RoleName.USER);
        Mockito.when(roleDao.getRoleByName(Mockito.any())).thenReturn(Optional.of(role));
        roleService = new RoleServiceImpl(roleDao);
        Role roleFromDatabase = roleService.getRoleByName("USER");
        assertNotNull(roleFromDatabase);
        role.setId(1L);
        assertEquals(role.toString(), roleFromDatabase.toString());
    }
}
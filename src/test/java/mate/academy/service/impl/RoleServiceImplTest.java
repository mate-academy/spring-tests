package mate.academy.service.impl;

import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RoleServiceImplTest {
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        roleService = Mockito.mock(RoleService.class);
    }

    @Test
    void save() {
        Role role = new Role(Role.RoleName.USER);
        role.setId(1L);
        Mockito.when(roleService.save(Mockito.any())).thenReturn(role);
        Role savedRole = roleService.save(role);
        assertNotNull(savedRole.getId());
    }

    @Test
    void getRoleByName() {
        Role role = new Role(Role.RoleName.USER);
        Mockito.when(roleService.getRoleByName(Mockito.any())).thenReturn(role);
        Role roleFromDatabase = roleService.getRoleByName("ADMIN");
        assertNotNull(roleFromDatabase);
        assertEquals(role.toString(), roleFromDatabase.toString());
    }
}

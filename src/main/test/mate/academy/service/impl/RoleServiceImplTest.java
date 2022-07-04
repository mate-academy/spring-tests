package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {
    @Mock
    private RoleDao roleDao;
    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    void save_validRole_Ok() {
        Role roleWithoutId = new Role(Role.RoleName.ADMIN);
        Role roleWithId = new Role(Role.RoleName.ADMIN);
        roleWithId.setId(42L);
        when(roleDao.save(roleWithoutId)).thenReturn(roleWithId);

        Role actualRole = roleService.save(roleWithoutId);

        assertNotNull(actualRole);
        assertEquals(actualRole, roleWithId);
    }

    @Test
    void getRoleByName_existingName_Ok() {
        Role.RoleName roleType = Role.RoleName.ADMIN;
        Role roleWithId = new Role(roleType);
        roleWithId.setId(1L);
        when(roleDao.getRoleByName("ADMIN")).thenReturn(Optional.of(roleWithId));

        Role actualRole = roleService.getRoleByName("ADMIN");

        assertNotNull(actualRole);
        assertEquals(actualRole, roleWithId);
    }
}

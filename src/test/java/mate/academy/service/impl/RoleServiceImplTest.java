package mate.academy.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private RoleService roleService;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_Ok() {
        Role role = createRole(Role.RoleName.USER, 1L);
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Role actual = roleService.save(role);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(Role.RoleName.USER.name(), actual.getRoleName().name());
    }

    @Test
    void getRoleByName_Ok() {
        Role firstRole = createRole(Role.RoleName.USER, 1L);
        Mockito.when(roleDao.getRoleByName(firstRole.getRoleName().name()))
                .thenReturn(Optional.of(firstRole));
        Role actual = roleService.getRoleByName(firstRole.getRoleName().name());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(Role.RoleName.USER.name(), actual.getRoleName().name());

        Role secondRole = createRole(Role.RoleName.ADMIN, 2L);
        Mockito.when(roleDao.getRoleByName(secondRole.getRoleName().name()))
                .thenReturn(Optional.of(secondRole));
        actual = roleService.getRoleByName(secondRole.getRoleName().name());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(2L, actual.getId());
        Assertions.assertEquals(Role.RoleName.ADMIN.name(), actual.getRoleName().name());
    }

    @Test
    void getRoleByName_invalidRoleName_notOk() {
        Assertions.assertThrows(NoSuchElementException.class, () ->
                roleService.getRoleByName("NonExistingRole"));
    }

    private Role createRole(Role.RoleName roleName, Long id) {
        Role role = new Role();
        role.setRoleName(roleName);
        role.setId(id);
        return role;
    }
}

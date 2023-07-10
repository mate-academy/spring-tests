package mate.academy.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceTest {
    private RoleDao roleDao;
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_role_ok() {
        Role role = getRole(1L, Role.RoleName.USER);
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Role actual = roleService.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(Role.RoleName.USER, actual.getRoleName());
    }

    @Test
    void getRoleByName_existedRoles_ok() {
        Role role = getRole(1L, Role.RoleName.ADMIN);
        Mockito.when(roleDao.getRoleByName(role.getRoleName().name()))
                .thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(role.getRoleName().name());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(Role.RoleName.ADMIN, actual.getRoleName());
        role = getRole(2L, Role.RoleName.USER);
        Mockito.when(roleDao.getRoleByName(role.getRoleName().name()))
                .thenReturn(Optional.of(role));
        actual = roleService.getRoleByName(role.getRoleName().name());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(2L, actual.getId());
        Assertions.assertEquals(Role.RoleName.USER, actual.getRoleName());
    }

    @Test
    void getRoleByName_notSavedRole_notOk() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName(Role.RoleName.ADMIN.name()));
    }

    private Role getRole(Long id, Role.RoleName roleName) {
        Role role = new Role();
        role.setId(id);
        role.setRoleName(roleName);
        return role;
    }
}

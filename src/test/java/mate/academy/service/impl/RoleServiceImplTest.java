package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private static final String INVALID_ROLE_NAME = "Invalid";
    private static RoleService roleService;
    private static RoleDao roleDao;

    @BeforeAll
    static void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    public void saveRole_Ok() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        Mockito.when(roleDao.save(any(Role.class))).thenReturn(role);
        Role savedRole = roleService.save(role);
        Assertions.assertNotNull(savedRole);
        Assertions.assertEquals(role, savedRole);
    }

    @Test
    public void getRoleByName_ExistentRoleName_Ok() {
        Role.RoleName roleName = Role.RoleName.USER;
        Mockito.when(roleDao.getRoleByName(roleName.name()))
                .thenReturn(Optional.of(new Role(roleName)));
        Role retrievedRole = roleService.getRoleByName(roleName.name());
        Assertions.assertNotNull(retrievedRole);
        Assertions.assertEquals(roleName, retrievedRole.getRoleName());
    }

    @Test
    public void getRoleByName_NullRoleName_NotOk() {
        Role.RoleName roleName = Role.RoleName.USER;
        Mockito.when(roleDao.getRoleByName(roleName.name()))
                .thenReturn(Optional.of(new Role(roleName)));
        assertThrows(NoSuchElementException.class, () -> roleService.getRoleByName(null),
                "NoSuchElementException to be thrown, but nothing was thrown");
    }

    @Test
    public void getRoleByName_NotExistentRoleName_NotOk() {
        Role.RoleName roleName = Role.RoleName.USER;
        Mockito.when(roleDao.getRoleByName(roleName.name()))
                .thenReturn(Optional.of(new Role(roleName)));
        assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName(INVALID_ROLE_NAME),
                "NoSuchElementException to be thrown, but nothing was thrown");
    }
}

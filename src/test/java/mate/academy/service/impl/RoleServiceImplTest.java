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
    private static RoleService roleService;
    private static RoleDao roleDao;
    private static Role testRole;
    private static String roleName;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        testRole = new Role(Role.RoleName.USER);
        roleName = "USER";
    }

    @Test
    void save_Ok() {
        Mockito.when(roleDao.save(testRole)).thenReturn(testRole);
        Role roleFromDb = roleService.save(testRole);
        Assertions.assertEquals(roleFromDb.getRoleName(),testRole.getRoleName());
    }

    @Test
    void getRoleByName_Ok() {
        Mockito.when(roleDao.getRoleByName(roleName)).thenReturn(Optional.of(testRole));
        Role roleByName = roleService.getRoleByName(roleName);
        Assertions.assertEquals(roleByName.getRoleName().name(),roleName);
    }

    @Test
    void getRoleByName_Null_NotOk() {
        Mockito.when(roleDao.getRoleByName(null)).thenThrow(NoSuchElementException.class);
        Assertions.assertThrows(NoSuchElementException.class, () ->
                roleService.getRoleByName(null));
    }

    @Test
    void getRoleByName_Empty_NotOk() {
        Mockito.when(roleDao.getRoleByName("")).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () ->
                roleService.getRoleByName(""));
    }
}

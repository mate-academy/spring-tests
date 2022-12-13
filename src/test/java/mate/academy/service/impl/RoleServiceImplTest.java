package mate.academy.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private static RoleDao roleDao;
    private static RoleService roleService;
    private Role role;

    @BeforeAll
    static void beforeAll() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1L);
        role.setRoleName(Role.RoleName.ADMIN);
    }

    @Test
    void saveRole_Ok() {
        Role testedRole = new Role(Role.RoleName.ADMIN);
        Mockito.when(roleDao.save(testedRole)).thenReturn(role);
        Role actual = roleService.save(testedRole);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role, actual);
    }

    @Test
    void getRoleByName_Ok() {
        String roleName = role.getRoleName().name();
        Mockito.when(roleDao.getRoleByName(roleName)).thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(roleName);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role, actual);
    }

    @Test
    void getRoleByName_notExistingRole_notOk() {
        String roleName = role.getRoleName().name();
        Mockito.when(roleDao.getRoleByName(roleName)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName(roleName));
    }
}

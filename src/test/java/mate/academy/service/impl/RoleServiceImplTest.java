package mate.academy.service.impl;

import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.NoSuchElementException;
import java.util.Optional;

class RoleServiceImplTest {
    private RoleDao roleDao;
    private RoleServiceImpl roleService;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_OK() {
        Role expected = new Role();
        expected.setId(1L);
        expected.setRoleName(Role.RoleName.ADMIN);
        Mockito.when(roleDao.save(expected)).thenReturn(expected);
        Role roleActual = roleService.save(expected);
        Assertions.assertEquals(expected, roleActual);
    }

    @Test
    void getRoleByName_ok() {
        Role roleAdminExpected = new Role();
        roleAdminExpected.setId(1L);
        roleAdminExpected.setRoleName(Role.RoleName.ADMIN);
        Mockito.when(roleDao.getRoleByName(roleAdminExpected.getRoleName().name()))
                .thenReturn(Optional.of(roleAdminExpected));
        Role roleAdminActual = roleService.getRoleByName(roleAdminExpected.getRoleName().name());
        Assertions.assertEquals(roleAdminExpected, roleAdminActual);
    }

    @Test
    void getRoleByName_notOk() {
        try {
            roleService.getRoleByName("UNKNOWN");
        } catch (NoSuchElementException e) {
            Assertions.assertEquals("No value present", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive NoSuchElementException");
    }
}
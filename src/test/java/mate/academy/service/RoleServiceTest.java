package mate.academy.service;

import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.NoSuchElementException;
import java.util.Optional;

class RoleServiceTest {
    private RoleService roleService;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_ok() {
        Role roleAdminExpected = new Role();
        roleAdminExpected.setId(1L);
        roleAdminExpected.setRoleName(Role.RoleName.ADMIN);
        Mockito.when(roleDao.save(roleAdminExpected)).thenReturn(roleAdminExpected);
        Role roleAdminActual = roleService.save(roleAdminExpected);
        Assertions.assertEquals(roleAdminExpected, roleAdminActual);
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
package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private RoleDao roleDao;
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_Ok() {
        Role expected = new Role();
        expected.setRoleName(Role.RoleName.USER);
        Mockito.when(roleDao.save(expected)).thenReturn(expected);
        Role actual = roleService.save(expected);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void getRoleByName_Ok() {
        Role expected = new Role();
        expected.setRoleName(Role.RoleName.USER);
        String roleName = Role.RoleName.USER.name();
        Mockito.when(roleDao.getRoleByName(roleName)).thenReturn(Optional.of(expected));
        Role actual = roleService.getRoleByName(roleName);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void getRoleByName_NoSuchName_NotOk() {
        String roleName = Role.RoleName.USER.name();
        Mockito.when(roleDao.getRoleByName(roleName)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> roleService.getRoleByName(roleName));
    }
}

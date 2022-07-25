package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceTest {
    private static final String USER_ROLE_NAME = "USER";
    private static final String ADMIN_ROLE_NAME = "ADMIN";
    private static final String NONEXIST_ROLE_NAME = "NONEXIST";
    private RoleDao roleDao = Mockito.mock(RoleDao.class);
    private RoleService roleService = new RoleServiceImpl(roleDao);

    @Test
    public void save_Ok() {
        Role userRole = new Role(Role.RoleName.valueOf(USER_ROLE_NAME));
        Role savedUserRole = new Role(Role.RoleName.valueOf(USER_ROLE_NAME));
        savedUserRole.setId(1L);
        Role adminRole = new Role(Role.RoleName.valueOf(ADMIN_ROLE_NAME));
        Role savedAdminRole = new Role(Role.RoleName.valueOf(ADMIN_ROLE_NAME));
        savedAdminRole.setId(2L);
        Mockito.when(roleDao.save(userRole)).thenReturn(savedUserRole);
        Mockito.when(roleDao.save(adminRole)).thenReturn(savedAdminRole);
        Role actualUserRole = roleService.save(userRole);
        assertNotNull(actualUserRole);
        assertEquals(actualUserRole.getRoleName(), Role.RoleName.valueOf(USER_ROLE_NAME));
        assertEquals(actualUserRole.getId(), savedUserRole.getId());
        Role actualAdminRole = roleService.save(adminRole);
        assertNotNull(actualAdminRole);
        assertEquals(actualAdminRole.getRoleName(), Role.RoleName.valueOf(ADMIN_ROLE_NAME));
        assertEquals(actualAdminRole.getId(), savedAdminRole.getId());
    }

    @Test
    public void save_exception_NotOk() {
        Role userRole = new Role(Role.RoleName.valueOf(USER_ROLE_NAME));
        Mockito.when(roleDao.save(userRole)).thenThrow(DataProcessingException.class);
        assertThrows(DataProcessingException.class, () -> roleService.save(userRole));
    }

    @Test
    void getRoleByName_Ok() {
        Role savedUserRole = new Role(Role.RoleName.valueOf(USER_ROLE_NAME));
        savedUserRole.setId(1L);
        Mockito.when(roleDao.getRoleByName(USER_ROLE_NAME)).thenReturn(Optional.of(savedUserRole));
        Role actualUserRole = roleService.getRoleByName(USER_ROLE_NAME);
        assertNotNull(actualUserRole);
        assertEquals(actualUserRole.getRoleName(), Role.RoleName.valueOf(USER_ROLE_NAME));
        assertEquals(actualUserRole.getId(), savedUserRole.getId());
        Role savedAdminRole = new Role(Role.RoleName.valueOf(ADMIN_ROLE_NAME));
        savedAdminRole.setId(2L);
        Mockito.when(roleDao.getRoleByName(ADMIN_ROLE_NAME)).thenReturn(Optional.of(savedAdminRole));
        Role actualAdminRole = roleService.getRoleByName(ADMIN_ROLE_NAME);
        assertNotNull(actualAdminRole);
        assertEquals(actualAdminRole.getRoleName(), Role.RoleName.valueOf(ADMIN_ROLE_NAME));
        assertEquals(actualAdminRole.getId(), savedAdminRole.getId());
    }

    @Test
    void getRoleByName_notExists_notOk() {
        Mockito.when(roleDao.getRoleByName(NONEXIST_ROLE_NAME)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> roleService.getRoleByName(NONEXIST_ROLE_NAME));
    }
}

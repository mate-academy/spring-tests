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
    void saveRole_Ok() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        role.setId(1L);
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Role actual = roleService.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role, actual);
    }

    @Test
    void saveRoleByAdmin_Ok() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.ADMIN);
        role.setId(1L);
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Role actual = roleService.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role, actual);
    }

    @Test
    void getRoleByValidAdminName_Ok() {
        String validRoleName = "ADMIN";
        Role admin = new Role();
        admin.setRoleName(Role.RoleName.ADMIN);
        admin.setId(1L);
        Mockito.when(roleDao.getRoleByName(validRoleName)).thenReturn(Optional.of(admin));
        Role actual = roleService.getRoleByName(validRoleName);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(admin, actual);
    }

    @Test
    void getRoleByUser_Ok() {
        String validRoleName = "USER";
        Role user = new Role();
        user.setRoleName(Role.RoleName.USER);
        user.setId(1L);
        Mockito.when(roleDao.getRoleByName(validRoleName)).thenReturn(Optional.of(user));
        Role actual = roleService.getRoleByName(validRoleName);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void getRoleByInvalidRoleName_NoSuchElementException_notOk() {
        String invalidRoleName = "CLIENT";
        Mockito.when(roleDao.getRoleByName(invalidRoleName)).thenReturn(Optional.ofNullable(null));
        try {
            roleService.getRoleByName(invalidRoleName);
        } catch (NoSuchElementException e) {
            return;
        }
        Assertions.fail("Expected to receive NoSuchElementException");
    }
}

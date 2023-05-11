package mate.academy.service.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private RoleService roleService;
    private RoleDao roleDao;
    private Role role;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        role = new Role();
        role.setRoleName(Role.RoleName.USER);
    }

    @Test
    void save_Ok() {
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Role actual = roleService.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.getId(), role.getId());
    }

    @Test
    void getRoleByName_Ok() {
        Mockito.when(roleDao.getRoleByName(role.getRoleName().name()))
                .thenReturn(Optional.of(new Role(Role.RoleName.USER)));
        Role actual = roleService.getRoleByName(role.getRoleName().name());
        Assertions.assertEquals(actual.getRoleName().name(), role.getRoleName().name());
    }

    @Test
    void getRoleByName_DataProcessingException_NotOk() {
        Mockito.when(roleDao.getRoleByName(null))
                .thenThrow(DataProcessingException.class);
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleService.getRoleByName(null));
    }
}

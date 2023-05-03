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
    private static Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setRoleName(Role.RoleName.USER);
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Mockito.when(roleDao.getRoleByName(Role.RoleName.USER.name()))
                .thenReturn(Optional.of(role));
    }

    @Test
    void save_ok() {
        Role actual = roleService.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getRoleName().name(), actual.getRoleName().name());
    }

    @Test
    void getRoleByName_ok() {
        Role actual = roleService.getRoleByName(Role.RoleName.USER.name());
        Assertions.assertNotNull(actual);
    }

    @Test
    void getRoleByName_roleNameIsIncorrect_notOk() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName("INCORRECT"));
    }
}

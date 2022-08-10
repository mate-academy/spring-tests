package mate.academy.service.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private static final Long ID_1 = 1L;
    private RoleService roleService;
    private Role role;

    @BeforeEach
    void setUp() {
        RoleDao roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        role = new Role(Role.RoleName.USER);
        role.setId(ID_1);
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Mockito.when(roleDao.getRoleByName(role.getRoleName().name()))
                .thenReturn(Optional.of(role));
    }

    @Test
    void save_validData_ok() {
        Role actual = roleService.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_validData_ok() {
        Role actual = roleService.getRoleByName(role.getRoleName().name());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getId(), actual.getId());
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void getROleByName_notValidRoleName_notOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> roleService.getRoleByName("Not valid Role name"));
    }

    @Test
    void getROleByName_nullRoleName_notOk() {
        Assertions.assertThrows(RuntimeException.class,
                () -> roleService.getRoleByName(null));
    }
}

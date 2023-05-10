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
    private static final Long ID = 1L;
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
    void save_ok() {
        Mockito.when(roleDao.save(role)).thenReturn(new Role(ID, Role.RoleName.USER));

        Role actual = roleService.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(ID, actual.getId());
    }

    @Test
    void getRoleByName_ok() {
        Mockito.when(roleDao.getRoleByName(role.getRoleName().name()))
                .thenReturn(Optional.of(new Role(ID, Role.RoleName.USER)));

        Role actual = roleService.getRoleByName(Role.RoleName.USER.name());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(ID, actual.getId());
    }

    @Test
    void getRoleByName_notOk() {
        Mockito.when(roleDao.getRoleByName("ADMIN"))
                .thenReturn(Optional.of(new Role(ID, Role.RoleName.USER)));

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            roleService.getRoleByName(Role.RoleName.USER.name());
        }, "NoSuchElementException expected");
    }
}

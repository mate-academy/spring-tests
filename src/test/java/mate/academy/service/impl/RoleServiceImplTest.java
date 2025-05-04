package mate.academy.service.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private RoleServiceImpl roleService;
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
        Mockito.when(roleDao.save(role)).thenReturn(role);

        Role actual = roleService.save(role);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role, actual);
    }

    @Test
    void getRoleByName_ok() {
        Mockito.when(roleDao.getRoleByName("USER")).thenReturn(Optional.of(role));

        Role actual = roleService.getRoleByName("USER");

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role, actual);
    }

    @Test
    void getRoleByNonExistName_ok() {
        Mockito.when(roleDao.getRoleByName("USER")).thenReturn(Optional.of(role));

        try {
            roleService.getRoleByName("USEROK");
        } catch (RuntimeException e) {
            return;
        }
        Assertions.fail("Excepted to receive RuntimeException");
    }
}

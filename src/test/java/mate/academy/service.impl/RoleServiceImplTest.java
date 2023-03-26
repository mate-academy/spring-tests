package mate.academy.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private static RoleService roleService;
    private static Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setRoleName(Role.RoleName.USER);
        RoleDao roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Mockito.when(roleDao.getRoleByName("USER"))
                .thenReturn(Optional.ofNullable(role));
    }

    @Test
    void save_correctRole_ok() {
        Role actual = roleService.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getRoleName().name(), actual.getRoleName().name());
    }

    @Test
    void getRoleByName_roleExists_ok() {
        Role role = roleService.getRoleByName("USER");
        Assertions.assertNotNull(role);
    }

    @Test
    void getRoleByName_roleDoesntExist_notOk() {
        try {
            roleService.getRoleByName("TESTER");
        } catch (NoSuchElementException e) {
            Assertions.assertEquals("No value present", e.getLocalizedMessage());
            return;
        }
        Assertions.fail("Expected to receive NoSuchElementException.");
    }
}

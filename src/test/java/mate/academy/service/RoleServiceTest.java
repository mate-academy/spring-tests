package mate.academy.service;

import java.util.NoSuchElementException;
import java.util.Optional;import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceTest {
    private RoleService roleService;
    private RoleDao roleDao;
    private Role userRole;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        userRole = new Role(Role.RoleName.USER);
    }

    @Test
    void save_ok() {
        Mockito.when(roleDao.save(userRole)).thenReturn(userRole);
        Role actual = roleService.save(userRole);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(userRole.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_ok() {
        Mockito.when(roleDao.getRoleByName(userRole.getRoleName().name()))
                .thenReturn(Optional.of(userRole));
        Role actual = roleService.getRoleByName(userRole.getRoleName().name());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(userRole.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_notOk() {
        try {
            roleService.getRoleByName("ADMIN");
        } catch (NoSuchElementException e) {
            Assertions.assertEquals("No value present", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive NoSuchElementException");
    }
}

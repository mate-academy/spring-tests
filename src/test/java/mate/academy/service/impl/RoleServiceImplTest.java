package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
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
        Mockito.when(roleDao.getRoleByName("USER"))
                .thenReturn(Optional.ofNullable(role));
    }

    @Test
    void saveCorrectRole_Ok() {
        Role actual = roleService.save(role);
        assertNotNull(actual);
        assertEquals(role.getRoleName().name(), actual.getRoleName().name());
    }

    @Test
    void getRoleByExistName_Ok() {
        Role role = roleService.getRoleByName("USER");
        assertNotNull(role);
    }

    @Test
    void getRoleByNotExistName_NotOk() {
        try {
            roleService.getRoleByName("GUEST");
        } catch (NoSuchElementException e) {
            assertEquals("No value present", e.getLocalizedMessage());
            return;
        }
        fail("Expected to receive NoSuchElementException");
    }
}

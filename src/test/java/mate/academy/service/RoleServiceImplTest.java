package mate.academy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private RoleDao roleDao;
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_Ok() {
        Role roleUser = new Role(Role.RoleName.USER);
        Mockito.when(roleDao.save(roleUser)).thenReturn(roleUser);
        Role actual = roleService.save(roleUser);
        assertNotNull(actual);
        assertEquals(actual.getRoleName(), roleUser.getRoleName());
    }

    @Test
    void save_notOk() {
        Role roleUser = new Role(Role.RoleName.USER);
        Role roleAdmin = new Role(Role.RoleName.ADMIN);
        Mockito.when(roleDao.save(roleUser)).thenReturn(roleAdmin);
        Role actual = roleService.save(roleUser);
        assertNotNull(actual);
        assertNotEquals(actual.getRoleName(), roleUser.getRoleName());
    }

    @Test
    void getRoleByName_getUserRole_Ok() {
        Role roleUser = new Role(Role.RoleName.USER);
        Mockito.when(roleDao.getRoleByName("USER")).thenReturn(Optional.of(roleUser));
        Role actual = roleService.getRoleByName("USER");
        assertNotNull(actual);
        assertEquals(actual.getRoleName(), roleUser.getRoleName());
    }

    @Test
    void getRoleByName_roleNotExist_notOk() {
        try {
            roleService.getRoleByName("USER");
        } catch (NoSuchElementException e) {
            assertEquals("No value present", e.getMessage());
            return;
        }
        fail("Expected - NoSuchElementException: No value present");
    }
}

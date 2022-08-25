package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class RoleServiceImplTest {
    private static RoleDao roleDao;
    private static RoleService roleService;

    @BeforeAll
    static void beforeAll() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_Ok() {
        Role expected = new Role(Role.RoleName.USER);
        Mockito.when(roleDao.save(expected)).thenReturn(expected);
        Role actual = roleService.save(expected);
        assertEquals(expected.getRoleName().name(), actual.getRoleName().name());
    }

    @Test
    void save_roleIsNull_NotOk() {
        Mockito.when(roleDao.save(null)).thenReturn(null);
        assertNull(roleService.save(null));
    }

    @Test
    void getRoleByName_Ok() {
        Role.RoleName userRole = Role.RoleName.USER;
        Mockito.when(roleDao.getRoleByName(userRole.name()))
                .thenReturn(Optional.of(new Role(userRole)));
        Role actual = roleService.getRoleByName(userRole.name());
        assertEquals(userRole.name(), actual.getRoleName().name());
    }

    @Test
    void getRoleByName_roleNotExists_NotOk() {
        Mockito.when(roleDao.getRoleByName("MOD")).thenThrow(NoSuchElementException.class);
        assertThrows(NoSuchElementException.class, () -> roleService.getRoleByName("MOD"),
                "Method should throw NoSuchElementException if there is no role "
                        + "with such name");
    }
}

package mate.academy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private static RoleDao roleDao;
    private static RoleService roleService;

    @BeforeAll
    static void beforeAll() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void getRoleByName_ok() {
        Role userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        Mockito.when(roleDao.getRoleByName(userRole.getRoleName().name()))
                .thenReturn(Optional.of(userRole));
        Role actual = roleService.getRoleByName(userRole.getRoleName().name());
        assertEquals(actual.getRoleName().name(), userRole.getRoleName().name());
    }

    @Test
    void getRoleName_roleNotExist_notOk() {
        Mockito.when(roleDao.getRoleByName("GOD"))
                .thenThrow(NoSuchElementException.class);
        assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName("GOD"),
                "Should receive NoSuchElementException");

    }

    @Test
    void save_ok() {
        Role expected = new Role(Role.RoleName.USER);
        Mockito.when(roleDao.save(expected)).thenReturn(expected);
        Role actual = roleService.save(expected);
        assertEquals(expected.getRoleName().name(), actual.getRoleName().name());
    }

    @Test
    void save_roleIsNull_notOk() {
        Mockito.when(roleDao.save(null)).thenThrow(DataProcessingException.class);
        assertThrows(DataProcessingException.class, () -> roleService.save(null),
                "Should receive DataProcessingException");
    }
}

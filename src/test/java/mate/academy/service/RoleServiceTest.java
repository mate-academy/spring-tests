package mate.academy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceTest {
    private static final String USER_ROLE = "USER";
    private static final String INVALID_ROLE = "INVALID ROLENAME";
    private static RoleService roleService;
    private static RoleDao roleDao;
    private static Role role;

    @BeforeAll
    static void beforeAll() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        role = new Role(Role.RoleName.USER);
    }

    @Test
    void save_Ok() {
        Mockito.when(roleService.save(role)).thenReturn(role);
        Role actual = roleService.save(role);
        assertNotNull(role);
        assertEquals(role,actual);
    }

    @Test
    void getRoleByName_Ok() {
        Mockito.when(roleDao.getRoleByName(USER_ROLE)).thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(USER_ROLE);
        assertNotNull(actual);
        assertEquals(role, actual);
    }

    @Test
    void getRoleByName_nonExistRole_noSuchElementException() {
        assertThrows(NoSuchElementException.class, () -> roleService.getRoleByName(INVALID_ROLE));
    }
}

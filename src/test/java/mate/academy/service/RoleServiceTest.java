package mate.academy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class RoleServiceTest {
    private static final long NONE_ID = 0L;
    private static RoleService roleService;
    private static RoleDao roleDao;
    private static Role userRole;

    @BeforeAll
    static void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        userRole = new Role();
    }

    @BeforeEach
    void beforeEach() {
        userRole.setRoleName(Role.RoleName.USER);
        userRole.setId(NONE_ID);
        Mockito.when(roleDao.save(userRole)).thenReturn(userRole);
        Mockito.when(roleDao.getRoleByName(userRole.getRoleName().name()))
                .thenReturn(Optional.of(userRole));
    }

    @Test
    void save_Ok() {
        Role actual = roleService.save(userRole);
        assertNotNull(actual, "You must return Role object");
    }

    @Test
    void findByUsername_Ok() {
        assertEquals(userRole, roleService.getRoleByName(userRole.getRoleName().name()),
                "You must find role with the same name");
    }

    @Test
    void findByUsername_NotOk() {
        assertThrows(RuntimeException.class, () ->
                roleService.getRoleByName("NOT_REAL_USER_ROLE"),
                "When you can't find role, you must throw runtime exception");
    }
}

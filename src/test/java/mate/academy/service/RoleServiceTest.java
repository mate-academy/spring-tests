package mate.academy.service;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

class RoleServiceTest {
    private static RoleDao roleDao;
    private static Role adminRole;
    private static Role userRole;
    private static RoleService roleService;

    @BeforeAll
    static void beforeAll() {
        roleDao = Mockito.mock(RoleDao.class);
        adminRole = new Role();
        adminRole.setRoleName(Role.RoleName.ADMIN);
        userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_Ok() {
        Mockito.when(roleDao.save(adminRole)).thenReturn(adminRole);
        Role actual = roleService.save(adminRole);
        assertNotNull(actual);
        assertEquals(Role.RoleName.ADMIN,actual.getRoleName());
    }

    @Test
    void getRoleByName_Ok() {
        Mockito.when(roleDao.getRoleByName(userRole.getRoleName().toString()))
                .thenReturn(Optional.ofNullable(userRole));
        Role actual = roleService.getRoleByName(userRole.getRoleName().toString());
        assertNotNull(actual);
        assertEquals(userRole,actual);
    }
}
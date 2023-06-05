package mate.academy.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private static final String ROLE_USER = "USER";
    private static final String ROLE_GOD = "GOD";
    private static RoleDao roleDao;
    private static RoleService roleService;
    private static Role role;

    @BeforeAll
    static void setUpBeforeClass() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        role = new Role(Role.RoleName.USER);
    }

    @Test
    void save_ok() {
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Role actual = roleService.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role, actual);
    }

    @Test
    void getRoleByName_existRole_ok() {
        Mockito.when(roleDao.getRoleByName(ROLE_USER)).thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(ROLE_USER);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role, actual);
    }
    
    @Test
    void getRoleByName_notExistRole_noSuchElementException() {
        Mockito.when(roleDao.getRoleByName(ROLE_GOD)).thenThrow(NoSuchElementException.class);
        Assertions.assertThrows(NoSuchElementException.class, () -> 
                roleService.getRoleByName(ROLE_GOD));
    }
}

package mate.academy.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private static final String ROLE_NAME = "USER";
    private RoleService roleService;
    private RoleDao roleDao;
    private Role role;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDaoImpl.class);
        role = new Role(Role.RoleName.USER);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_ok() {
        Role expectedRole = new Role(Role.RoleName.valueOf(ROLE_NAME));
        expectedRole.setId(1L);
        Mockito.when(roleDao.save(role)).thenReturn(expectedRole);
        Role savedRole = roleService.save(role);
        Assertions.assertNotNull(savedRole);
        Assertions.assertEquals(expectedRole.getId(), savedRole.getId());
        Assertions.assertEquals(expectedRole.getRoleName(), savedRole.getRoleName());
    }

    @Test
    void getRoleByName_ok() {
        Role expectedRole = new Role(Role.RoleName.valueOf(ROLE_NAME));
        expectedRole.setId(1L);
        Mockito.when(roleDao.getRoleByName(ROLE_NAME))
                .thenReturn(Optional.of(expectedRole));
        Role finedRole = roleService.getRoleByName(ROLE_NAME);
        Assertions.assertNotNull(finedRole);
        Assertions.assertEquals(expectedRole.getId(), finedRole.getId());
        Assertions.assertEquals(expectedRole.getRoleName(), finedRole.getRoleName());
    }

    @Test
    void getRoleByName_notExistRole_notOk() {
        Mockito.when(roleDao.getRoleByName(ROLE_NAME)).thenReturn(Optional.empty());
        try {
            Role finedRole = roleService.getRoleByName(ROLE_NAME);
        } catch (NoSuchElementException e) {
            Assertions.assertEquals("No value present", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive NoSuchElementException");
    }
}

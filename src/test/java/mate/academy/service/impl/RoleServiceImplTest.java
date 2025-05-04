package mate.academy.service.impl;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private static final String ROLE_NAME = "USER";
    private Role newRole;
    private RoleDao roleDao;
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        newRole = new Role(Role.RoleName.valueOf(ROLE_NAME));
    }

    @Test
    void saveRole_ok() {
        Role savedRole = newRole;
        savedRole.setId(1L);
        Mockito.when(roleDao.save(any())).thenReturn(savedRole);
        Role actualRole = roleService.save(newRole);
        Assertions.assertNotNull(actualRole);
        Assertions.assertEquals(savedRole.getId(), actualRole.getId());
        Assertions.assertEquals(savedRole.getRoleName(), actualRole.getRoleName());
    }

    @Test
    void getRoleByName_ok() {
        Mockito.when(roleDao.getRoleByName(ROLE_NAME)).thenReturn(Optional.of(newRole));
        Role actualRole = roleService.getRoleByName(ROLE_NAME);
        Assertions.assertNotNull(actualRole);
        Assertions.assertEquals(ROLE_NAME, actualRole.getRoleName().name());
    }

    @Test
    void getRoleByName_NotExistRoleName_Exception() {
        Mockito.when(roleDao.getRoleByName(any())).thenReturn(Optional.empty());
        try {
            roleService.getRoleByName(ROLE_NAME);
        } catch (Exception exception) {
            Assertions.assertEquals("No value present", exception.getMessage());
            return;
        }
        Assertions.fail("There is NoSuchElementException expected!");
    }
}

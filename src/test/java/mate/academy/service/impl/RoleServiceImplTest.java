package mate.academy.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private RoleService roleService;
    private RoleDao roleDao;
    private Role role;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);

        role = new Role();
        role.setId(1L);
        role.setRoleName(Role.RoleName.ADMIN);
    }

    @Test
    void save_Ok() {
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Role actual = roleService.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getId(), actual.getId());
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_existRoleName_Ok() {
        String existRoleName = "ADMIN";
        Mockito.when(roleDao.getRoleByName(existRoleName)).thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(existRoleName);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getId(), actual.getId());
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_nonExistRoleName_NotOk() {
        String nonExistRoleName = "MODERATOR";
        Mockito.when(roleDao.getRoleByName(nonExistRoleName)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName(nonExistRoleName),
                "Expected to receive NoSuchElementException "
                        + "while trying to get non-existent roleName.\n");
    }
}

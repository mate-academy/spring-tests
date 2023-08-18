package mate.academy.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class RoleServiceImplTest {
    private static final String testRoleName = "USER";
    private static RoleService roleService;
    private static RoleDao roleDao;
    private static Role testRole;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        testRole = new Role(Role.RoleName.USER);
    }

    @Test
    void saveRole_roleSaved_Ok() {
        Mockito.when(roleDao.save(testRole)).thenReturn(testRole);
        Role actual = roleService.save(testRole);
        Assertions.assertEquals(actual.getRoleName(),testRole.getRoleName(),
                "The actual role doesn't match the expected role");
    }

    @Test
    void getRole_getRoleByName_Ok() {
        Mockito.when(roleDao.getRoleByName(testRoleName)).thenReturn(Optional.of(testRole));
        Role roleByName = roleService.getRoleByName(testRoleName);
        Assertions.assertEquals(roleByName.getRoleName().name(), testRoleName,
                "The actual role name doesn't match the expected role name");
    }

    @Test
    void getRole_getRoleByNullName_NotOk() {
        Mockito.when(roleDao.getRoleByName(null)).thenThrow(NoSuchElementException.class);
        Assertions.assertThrows(NoSuchElementException.class, () ->
                        roleService.getRoleByName(null),
                "NoSuchElementException expected");
    }

    @Test
    void getRoleByName_GetEmptyRole_NotOk() {
        Mockito.when(roleDao.getRoleByName("")).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () ->
                        roleService.getRoleByName(""),
                "NoSuchElementException expected");
    }
}

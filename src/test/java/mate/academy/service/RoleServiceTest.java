package mate.academy.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceTest {
    private static RoleService roleService;
    private static RoleDao roleDao;
    private static Role roleUser;

    @BeforeAll
    static void beforeAll() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        roleUser = new Role(Role.RoleName.USER);
    }

    @Test
    void save_Ok() {
        Mockito.when(roleDao.save(roleUser)).thenReturn(roleUser);
        Role save = roleService.save(roleUser);
        Assertions.assertNotNull(save);
    }

    @Test
    void getRoleByName_Ok() {
        Mockito.when(roleDao.getRoleByName(roleUser.getRoleName().name()))
                .thenReturn(Optional.of(roleUser));
        Role roleByName = roleService.getRoleByName(roleUser.getRoleName().name());
        Assertions.assertNotNull(roleByName);
    }

    @Test
    void getRoleByName_NotOk() {
        Mockito.when(roleDao.getRoleByName(roleUser.getRoleName().name()))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName(roleUser.getRoleName().name()));
    }
}

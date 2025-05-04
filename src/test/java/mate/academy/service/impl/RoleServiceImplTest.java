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
    private static final String EXIST_ROLE = "USER";
    private static final String NON_EXIST_ROLE = "CUSTOMER";
    private static Role role;
    private static RoleDao roleDao;
    private static RoleService roleService;

    @BeforeAll
    static void beforeAll() {
        role = new Role();
        role.setRoleName(Role.RoleName.USER);
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_validRole_Ok() {
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Role actual = roleService.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getId(), actual.getId());
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_roleExists_Ok() {
        Mockito.when(roleDao.getRoleByName(EXIST_ROLE)).thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(role.getRoleName().name());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getId(), actual.getId());
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_roleNotExists_notOk() {
        Mockito.when(roleDao.getRoleByName(NON_EXIST_ROLE))
                            .thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName(NON_EXIST_ROLE),
                "Expected to receive NoSuchElementException");
    }
}

package mate.academy.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import mate.academy.util.UserUtilForTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private RoleService roleService;
    private RoleDao roleDao;
    private UserUtilForTest userUtil;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        userUtil = new UserUtilForTest();
    }

    @Test
    void save_ok() {
        Role expected = userUtil.getUserRole();
        Mockito.when(roleDao.save(expected)).thenReturn(expected);
        Role actual = roleService.save(expected);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(userUtil.getUserRole().getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_ok() {
        Role expected = userUtil.getUserRole();
        Mockito.when(roleDao.getRoleByName(expected.getRoleName().name())).thenReturn(Optional.of(expected));
        Role actual = roleService.getRoleByName(expected.getRoleName().name());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_notValidName_notOk() {
        Mockito.when(roleDao.getRoleByName(userUtil.getAdminRole().getRoleName().name()))
                .thenReturn(Optional.empty());
        try {
            roleService.getRoleByName(userUtil.getAdminRole().getRoleName().name());
        } catch (NoSuchElementException e) {
            Assertions.assertEquals("No value present", e.getMessage());
            return;
        }
        Assertions.fail();
    }
}
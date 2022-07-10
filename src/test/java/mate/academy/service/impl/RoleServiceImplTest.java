package mate.academy.service.impl;

import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Optional;

class RoleServiceImplTest {
    private static final String ROLE_USER_STRING = "USER";
    private static final Role ROLE_USER_CLASS = new Role(Role.RoleName.USER);
    private RoleService roleService;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_Ok() {
        Mockito.when(roleService.save(ROLE_USER_CLASS)).thenReturn(ROLE_USER_CLASS);
        Role actual = roleService.save(ROLE_USER_CLASS);
        Assertions.assertNotNull(actual);
    }

    @Test
    void getRoleByName_ok() {
        Mockito.when(roleDao.getRoleByName(ROLE_USER_STRING)).thenReturn(Optional.of(ROLE_USER_CLASS));
        Role actual = roleService.getRoleByName(ROLE_USER_STRING);
        Assertions.assertNotNull(actual);
    }
}

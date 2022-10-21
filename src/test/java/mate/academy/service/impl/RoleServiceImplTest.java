package mate.academy.service.impl;

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

    private Role roleIn;
    private Role roleOut;

    private RoleDao roleDao;
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDaoImpl.class);
        roleService = new RoleServiceImpl(roleDao);
        roleIn = new Role(Role.RoleName.USER);
        roleOut = new Role(Role.RoleName.USER);
        roleOut.setId(1L);
    }

    @Test
    void save_roleUser_OK() {
        Mockito.when(roleDao.save(roleIn)).thenReturn(roleOut);
        Role actual = roleService.save(roleIn);
        Assertions.assertEquals(actual,roleOut);
    }

    @Test
    void getRoleByName_roleUser_OK() {
        Mockito.when(roleDao.getRoleByName("USER")).thenReturn(Optional.of(roleOut));
        Role actual = roleService.getRoleByName("USER");
        Assertions.assertEquals(actual,roleOut);
    }
}

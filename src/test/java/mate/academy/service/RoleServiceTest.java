package mate.academy.service;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.model.Role.RoleName;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceTest {
    private RoleService roleService;
    private RoleDao roleDao;
    private RoleName roleName;
    private Role userRole;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        roleName = RoleName.USER;
        userRole = new Role(roleName);
    }

    @Test
    void save_Ok() {
        Mockito.when(roleDao.save(any())).thenReturn(userRole);
        Role actual = roleService.save(userRole);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals("USER", actual.getRoleName().name());
    }

    @Test
    void getRoleByName() {
        Mockito.when(roleDao.getRoleByName(any())).thenReturn(Optional.of(userRole));
        Role actual = roleService.getRoleByName(roleName.name());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals("USER", actual.getRoleName().name());
    }
}

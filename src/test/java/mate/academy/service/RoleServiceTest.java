package mate.academy.service;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceTest {
    private RoleDao roleDao;
    private RoleService roleService;
    private Role role;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);

        role = new Role();
        role.setRoleName(Role.RoleName.USER);
    }

    @Test
    void save_Ok() {
        Mockito.when(roleDao.save(role))
                .thenReturn(role);

        Role actual = roleService.save(role);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role, actual);
    }

    @Test
    void getByRoleName_ok() {
        Mockito.when(roleDao.save(role))
                .thenReturn(role);

        Role savedRole = roleService.save(role);
        String name = savedRole.getRoleName().name();

        Mockito.when(roleDao.getRoleByName(name))
                .thenReturn(Optional.of(savedRole));
        Role actual = roleService.getRoleByName(name);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(savedRole, actual);
    }
}

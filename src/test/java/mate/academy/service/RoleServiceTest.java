package mate.academy.service;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;

class RoleServiceTest {
    private RoleService roleService;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save() {
        Role roleUser = new Role(Role.RoleName.USER);
        Mockito.when(roleDao.save(roleUser)).thenReturn(roleUser);
        Role savedRole = roleService.save(roleUser);
        Assertions.assertNotNull(savedRole);
        Assertions.assertEquals(roleUser, savedRole);
    }

    @Test
    void getRoleByName() {
        Role roleAdmin = new Role(Role.RoleName.ADMIN);
        String name = roleAdmin.getRoleName().name();
        Mockito.when(roleDao.getRoleByName(name)).thenReturn(Optional.of(roleAdmin));
        Role roleByName = roleService.getRoleByName(name);
        Assertions.assertNotNull(roleByName);
        Assertions.assertEquals(roleAdmin.getRoleName().name(), roleByName.getRoleName().name());
    }
}
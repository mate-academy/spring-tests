package mate.academy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleServiceTest {
    private final RoleDao roleDao = mock(RoleDao.class);
    private final RoleService roleService = new RoleServiceImpl(roleDao);
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setRoleName(Role.RoleName.USER);
    }

    @Test
    void save_Ok() {
        when(roleDao.save(role))
                .thenReturn(role);

        Role actual = roleService.save(role);

        assertNotNull(actual);
        assertEquals(role, actual);
    }

    @Test
    void getByRoleName_ok() {
        when(roleDao.save(role))
                .thenReturn(role);

        Role savedRole = roleService.save(role);
        String name = savedRole.getRoleName().name();

        when(roleDao.getRoleByName(name))
                .thenReturn(Optional.of(savedRole));
        Role actual = roleService.getRoleByName(name);

        assertNotNull(actual);
        assertEquals(savedRole, actual);
    }
}

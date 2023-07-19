package mate.academy.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private static final String VALID_ROLE = "ADMIN";
    private RoleDao roleDao;
    private RoleService roleService;
    private Role role;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        role = new Role();
        role.setRoleName(Role.RoleName.ADMIN);
    }

    @Test
    void save_Ok() {
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Role actual = roleService.save(role);
        Assertions.assertEquals(VALID_ROLE, actual.getRoleName().name());
    }

    @Test
    void getRoleByName_Ok() {
        Mockito.when(roleDao.getRoleByName(Role.RoleName.ADMIN.name()))
                .thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(VALID_ROLE);
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_notOk() {
        Mockito.when(roleDao.getRoleByName(VALID_ROLE)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> {
            roleService.getRoleByName(VALID_ROLE);
        });
    }
}

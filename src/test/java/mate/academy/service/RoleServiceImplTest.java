package mate.academy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleServiceImplTest {
    private static final Role role = new Role(Role.RoleName.USER);
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        RoleDao roleDao = mock(RoleDao.class);
        when(roleDao.save(role))
                .thenReturn(new Role(Role.RoleName.USER));
        when(roleDao.getRoleByName(role.getRoleName().name()))
                .thenReturn(Optional.of(role));
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_Ok() {
        Role actual = roleService.save(role);
        assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_Ok() {
        Role actual = roleService.getRoleByName(role.getRoleName().name());
        assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_NotOk() {
        assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName("MANAGER"));
    }
}

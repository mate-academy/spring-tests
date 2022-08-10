package mate.academy.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private static RoleService roleService;

    @BeforeEach
    void setUp() {
        RoleDao roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        Role role = new Role(Role.RoleName.USER);
        role.setId(1L);
        Mockito.when(roleDao.save(Mockito.any(Role.class))).thenReturn(role);
        Mockito.when(roleDao.save(null)).thenThrow(DataProcessingException.class);
        Mockito.when(roleDao.getRoleByName("USER")).thenReturn(Optional.of(role));
        Mockito.when(roleDao.getRoleByName("WRONG_ROLE")).thenReturn(Optional.empty());
    }

    @Test
    public void save_ok() {
        Role role = new Role(Role.RoleName.USER);
        roleService.save(role);
    }

    @Test
    public void save_null_notOk() {
        Assertions.assertThrows(DataProcessingException.class, () -> roleService.save(null));
    }

    @Test
    public void getRoleByName_ok() {
        Role role = roleService.getRoleByName("USER");
        Assertions.assertEquals("USER", role.getRoleName().name());
        Assertions.assertEquals(1L, role.getId());
    }

    @Test
    public void getRoleByName_wrongRole_notOk() {
        Assertions.assertThrows(NoSuchElementException.class, () ->
                roleService.getRoleByName("WRONG_ROLE"));
    }
}

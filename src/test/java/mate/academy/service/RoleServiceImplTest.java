package mate.academy.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private RoleService roleService;
    private RoleDao roleDao;
    private Role role;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        role = new Role(Role.RoleName.USER);
    }

    @Test
    void save_isSuccessful_ok() {
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Role actual = roleService.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role, actual);
    }

    @Test
    void getRoleByName_isSuccessful_ok() {
        Mockito.when(roleDao.getRoleByName(role.getRoleName().name()))
                .thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(role.getRoleName().name());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getRoleName().name(),
                actual.getRoleName().name());
    }

    @Test
    void getRoleByName_incorrectRoleName_notOk() {
        Assertions.assertThrows(NoSuchElementException.class, () ->
                roleService.getRoleByName("role"));
    }
}

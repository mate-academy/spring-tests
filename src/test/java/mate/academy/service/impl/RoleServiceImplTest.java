package mate.academy.service.impl;

import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {
    private RoleServiceImpl roleService;

    @Mock
    private RoleDaoImpl roleDao;

    @BeforeEach
    void setUp() {
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_Ok() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        when(roleDao.save(role)).thenReturn(role);
        Assertions.assertEquals(role.getRoleName(), roleService.save(role).getRoleName());
    }

    @Test
    void save_Not_Ok_NullValue() {
        Role role = null;
        Assertions.assertNull(roleService.save(role));
    }

    @Test
    void getRoleByName_Ok() {
        Role user = new Role();
        user.setRoleName(Role.RoleName.USER);
        when(roleDao.getRoleByName(Role.RoleName.USER.name())).thenReturn(Optional.of(user));
        Assertions.assertEquals(user.getRoleName(),
                roleService.getRoleByName("USER").getRoleName());
        Role admin = new Role();
        admin.setRoleName(Role.RoleName.ADMIN);
        when(roleDao.getRoleByName(Role.RoleName.ADMIN.name())).thenReturn(Optional.of(admin));
        Assertions.assertEquals(admin.getRoleName(),
                roleService.getRoleByName("ADMIN").getRoleName());
    }

    @Test
    void getRoleByName_NotOk_Throw_Exception() {
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName("admin"));
    }
}

package mate.academy.service;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {
    private RoleService roleService;
    @Mock
    private RoleDao roleDao;
    private Role userRole;

    @BeforeEach
    void setUp() {
        userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void roleService_save_Ok() {
        Mockito.when(roleDao.save(any())).thenReturn(userRole);
        Role actual = roleService.save(userRole);
        Assertions.assertEquals(userRole.getRoleName(), actual.getRoleName());
    }

    @Test
    void roleService_getRoleByName() {
        Mockito.when(roleDao.getRoleByName(any())).thenReturn(Optional.of(userRole));
        Role actual = roleService.getRoleByName(userRole.getRoleName().name());
        Assertions.assertEquals(userRole, actual);
    }
}

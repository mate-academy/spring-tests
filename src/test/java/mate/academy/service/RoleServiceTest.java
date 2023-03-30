package mate.academy.service;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceTest {
    private static final Role TEST_ROLE = new Role(Role.RoleName.ADMIN);
    private RoleService roleService;
    private RoleDao roleDao;
    
    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }
    
    @Test
    void save_Ok() {
        Mockito.when(roleDao.save(TEST_ROLE)).thenReturn(TEST_ROLE);
        Role actual = roleService.save(TEST_ROLE);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.getRoleName(), TEST_ROLE.getRoleName());
    }
    
    @Test
    void getRoleByName_Ok() {
        Mockito.when(roleDao.getRoleByName(TEST_ROLE.getRoleName().name()))
                .thenReturn(Optional.of(TEST_ROLE));
        Role actual = roleService.getRoleByName(TEST_ROLE.getRoleName().name());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.getRoleName(), TEST_ROLE.getRoleName());
    }
    
    @Test
    void getRoleByName_IncorrectRoleName_notOk() {
        Mockito.when(roleDao.getRoleByName(any())).thenReturn(Optional.empty());
        Assertions.assertThrows(RuntimeException.class, () ->
                roleService.getRoleByName(Role.RoleName.USER.name()));
    }
}

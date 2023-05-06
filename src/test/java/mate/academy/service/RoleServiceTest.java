package mate.academy.service;

import java.util.NoSuchElementException;
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
    @Mock
    private RoleDao roleDao;
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_successSave_ok() {
        Role role = new Role(Role.RoleName.USER);
        Mockito.when(roleDao.save(role)).thenReturn(new Role(1L, Role.RoleName.USER));
        Role actual = roleService.save(role);
        Assertions.assertNotNull(actual, "Role must be not null");
        Assertions.assertEquals(actual.getId(), 1L,
                "Id should be 1, but actual is " + actual.getId());
    }

    @Test
    void getRoleByName_roleExist_ok() {
        Mockito.when(roleDao.getRoleByName("USER"))
                .thenReturn(Optional.of(new Role(1L, Role.RoleName.USER)));
        Role actual = roleService.getRoleByName("USER");
        Assertions.assertNotNull(actual, "Role must be not null");
        Assertions.assertEquals(actual.getRoleName(), Role.RoleName.USER,
                "RoleNames don't match. Actual is " + actual.getRoleName().name()
                        + " but must be " + Role.RoleName.USER.name());
    }

    @Test
    void getRoleByName_roleDontExist_ok() {
        Mockito.when(roleDao.getRoleByName("ADMIN")).thenThrow(NoSuchElementException.class);
        Assertions.assertThrows(NoSuchElementException.class,
                () -> roleService.getRoleByName("ADMIN"),
                "Method should throw NoSuchElementException");
    }
}

package mate.academy.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class RoleServiceImplTest {
    private RoleDao roleDao;
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_Ok() {
        Role role = new Role(Role.RoleName.ADMIN);
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Role actual = roleService.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role, actual);
    }

    @Test
    void getRoleByName_Ok() {
        Role role = new Role(Role.RoleName.ADMIN);
        Mockito.when(roleDao.getRoleByName(role.getRoleName().name()))
                .thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(role.getRoleName().name());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role, actual);
    }

    @Test
    void getRoleByName_WrongRoleName_NotOk() {
        String WrongRoleName = "WRONG_ROLE";
        Mockito.when(roleDao.getRoleByName(WrongRoleName)).thenThrow(
                new DataProcessingException("Couldn't get role by role name: " + WrongRoleName));
        Throwable exception = Assertions.assertThrows(DataProcessingException.class, () -> {
            roleService.getRoleByName(WrongRoleName);},
                "NoSuchElementException was expected");
        Assertions.assertEquals("Couldn't get role by role name: " + WrongRoleName,
                exception.getLocalizedMessage());
    }
}

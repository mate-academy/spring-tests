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

class RoleServiceTest {
    private RoleService roleService;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void saveRole_Ok() {
        Role role = new Role(Role.RoleName.USER);
        role.setId(1L);
        Mockito.when(roleDao.save(Mockito.any())).thenReturn(role);
        Role actual = roleService.save(role);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }

    @Test
    void saveRole_DaoSaveException_NotOk() {
        Role role = new Role(Role.RoleName.USER);
        Mockito.when(roleDao.save(Mockito.any())).thenThrow(
                new DataProcessingException("Can't create entity: " + role, new Exception()));
        try {
            roleService.save(role);
        } catch (Exception e) {
            Assertions.assertEquals("Can't create entity: " + role, e.getMessage());
            return;
        }
        Assertions.fail("Incorrect role should throw DataProcessingException");
    }

    @Test
    void getRoleByName_Ok() {
        String roleName = "USER";
        Role role = new Role(Role.RoleName.USER);
        role.setId(1L);
        Mockito.when(roleDao.getRoleByName(roleName)).thenReturn(Optional.of(role));

        Role actual = roleService.getRoleByName(roleName);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getId(), actual.getId());
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_ThrowException() {
        Mockito.when(roleDao.getRoleByName(Mockito.anyString()))
                .thenThrow(new NoSuchElementException("No value present"));
        try {
            roleService.getRoleByName(Mockito.anyString());
        } catch (NoSuchElementException e) {
            Assertions.assertEquals("No value present", e.getMessage());
            return;
        }
        Assertions.fail("Unavailable role should throw NoSuchElementException");
    }
}

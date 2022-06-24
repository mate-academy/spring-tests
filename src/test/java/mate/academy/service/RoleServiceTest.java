package mate.academy.service;

import mate.academy.dao.RoleDao;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.NoSuchElementException;
import java.util.Optional;

class RoleServiceTest {
    private RoleService roleService;
    private RoleDao roleDao;
    private String correctRoleName;
    private String incorrectRoleName;
    private Long identifier;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDaoImpl.class);
        roleService = new RoleServiceImpl(roleDao);
        correctRoleName = Role.RoleName.USER.name();
        incorrectRoleName = "SASHA";
        identifier = 1L;
    }

    @Test
    void save_Ok() {
        Role role = new Role(Role.RoleName.USER);
        role.setId(identifier);
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Role actual = roleService.save(role);
        Assertions.assertNotNull(actual, "Role must not be null for: " + role);

    }

    @Test
    void getRoleByName_Ok() {
        Role role = new Role(Role.RoleName.USER);
        role.setId(identifier);
        Mockito.when(roleDao.getRoleByName(correctRoleName)).thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(correctRoleName);
        Assertions.assertNotNull(actual, "Role must not be null for role name: " + correctRoleName);
    }

    @Test
    void getRoleByName_NotOk() {
        Role role = new Role(Role.RoleName.USER);
        role.setId(identifier);
        Mockito.when(roleDao.getRoleByName(incorrectRoleName)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () -> {roleService.getRoleByName(incorrectRoleName);},
                "NoSuchElementException expected for role name: " + incorrectRoleName);
    }
}
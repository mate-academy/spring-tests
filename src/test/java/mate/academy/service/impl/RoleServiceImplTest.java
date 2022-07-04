package mate.academy.service.impl;

import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.NoSuchElementException;
import java.util.Optional;

class RoleServiceImplTest {
    private RoleDao roleDao;
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_ok() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        Mockito.when(roleDao.save(role)).thenReturn(role);
        role.setId(1L);

        Role savedRole = roleService.save(role);
        Assertions.assertNotNull(savedRole);
        Assertions.assertEquals(savedRole.getRoleName(), role.getRoleName());
        Assertions.assertEquals(savedRole.getId(), role.getId());
    }

    @Test
    void getRoleByName_ok() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        Optional<Role> optionalRole = Optional.of(role);
        String roleName = Role.RoleName.USER.name();

        Mockito.when(roleDao.getRoleByName(roleName)).thenReturn(optionalRole);

        Role roleByName = roleService.getRoleByName(roleName);
        Assertions.assertNotNull(roleByName);
        Assertions.assertEquals(roleByName.getRoleName(), role.getRoleName());
    }

    @Test
    void getRoleByName_wrongName_notOk() {
        String wrongRoleName = "USOR";

        Mockito.when(roleDao.getRoleByName(wrongRoleName)).thenReturn(Optional.empty());

        try {
            roleService.getRoleByName(wrongRoleName);
        } catch (NoSuchElementException e) {
            Assertions.assertEquals("No value present", e.getMessage());
            return;
        }
        Assertions.fail("Expected to receive NoSuchElementException");
    }
}
package mate.academy.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private RoleServiceImpl roleService;
    private RoleDao roleDao;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setRoleName(Role.RoleName.USER);
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
        Mockito.when(roleDao.save(role)).thenReturn(role);
        Mockito.when(roleDao.getRoleByName("USER")).thenReturn(Optional.ofNullable(role));
    }

    @Test
    void save_ok() {
        Role actual = roleService.save(role);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role.getRoleName(), actual.getRoleName());
    }

    @Test
    void findByName_notExistName_throwException() {
        try {
            roleService.getRoleByName("BOSS");
        } catch (NoSuchElementException ex) {
            Assertions.assertEquals(ex.getLocalizedMessage(), "No value present");
            return;
        }
        Assertions.fail("Excepted to receive NoSuchElementException");
    }

    @Test
    void findByName_correctName_isOk() {
        Role userRole = roleService.getRoleByName("USER");
        Assertions.assertNotNull(userRole);
        Assertions.assertEquals(userRole, role);
    }
}

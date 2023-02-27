package mate.academy.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

class RoleServiceImplTest {
    private RoleServiceImpl roleServiceImpl;
    private Role roleRegister;
    private Role roleReturn;
    private Role incorrectRoleReturn;

    @BeforeEach
    void setUp() {
        RoleDao roleDao = Mockito.mock(RoleDao.class);
        roleServiceImpl = new RoleServiceImpl(roleDao);
        roleRegister = new Role(Role.RoleName.ADMIN);
        roleReturn = new Role(Role.RoleName.ADMIN);
        roleReturn.setId(1L);
        incorrectRoleReturn = new Role(Role.RoleName.USER);
        incorrectRoleReturn.setId(2L);
        when(roleDao.save(roleRegister)).thenReturn(roleReturn);
        when(roleDao.getRoleByName("ADMIN")).thenReturn(Optional.of(roleReturn));
        when(roleDao.getRoleByName("MANAGER")).thenReturn(Optional.empty());
    }

    @Test
    void save_correctData_ok() {
        assertEquals(roleReturn, roleServiceImpl.save(roleRegister));
    }

    @Test
    void save_inCorrectData_notOk() {
        assertNotEquals(incorrectRoleReturn, roleServiceImpl.save(roleRegister));
    }

    @Test
    void getRoleByName_correctRole_ok() {
        Role role = roleServiceImpl.getRoleByName("ADMIN");
        assertEquals(1L, role.getId());
    }

    @Test
    void getRoleByName_inCorrectRole_notOk() {
        try {
            roleServiceImpl.getRoleByName("MANAGER");
        } catch (NoSuchElementException e) {
            assertEquals("No value present", e.getMessage());
        }
    }
}
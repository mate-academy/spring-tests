package mate.academy.service;

import static mate.academy.model.Role.RoleName.ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceTest {
    private RoleDao roleDaoMock;
    private RoleService roleService;
    private Role role;

    @BeforeEach
    void setUp() {
        roleDaoMock = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDaoMock);
        role = new Role(ADMIN);
    }

    @Test
    void save_ValidRole_Ok() {
        Mockito.when(roleDaoMock.save(role)).thenReturn(role);
        Role actual = roleService.save(role);
        assertNotNull(actual);
        assertEquals(role, actual);
    }

    @Test
    void save_RoleIsNull_NotOk() {
        assertThrows(RuntimeException.class, () -> {roleService.save(null);});
    }

    @Test
    void getRoleByName_ValidRoleName_Ok() {
        Mockito.when(roleDaoMock.getRoleByName(ADMIN.name())).thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(ADMIN.name());
        assertNotNull(actual);
        assertEquals(role, actual);
    }

    @Test
    void getRoleByName_RoleNameIsNull_NotOk() {
        assertThrows(RuntimeException.class, () -> {roleService.getRoleByName(null);});
    }
}
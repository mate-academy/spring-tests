package mate.academy.service;

import static mate.academy.model.Role.RoleName.ADMIN;
import static mate.academy.model.Role.RoleName.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {
    private RoleService roleService;
    @Mock
    private RoleDao roleDao;
    private Role role;
    private Role savedRole;

    @BeforeEach
    void setUp() {
        roleService = new RoleServiceImpl(roleDao);
        role = new Role();
        role.setRoleName(ADMIN);
        savedRole = new Role();
        savedRole.setId(1L);
        savedRole.setRoleName(ADMIN);
    }

    @Test
    void save_validUser_ok() {
        when(roleDao.save(role)).thenReturn(savedRole);
        Role actual = roleService.save(role);
        assertEquals(actual, savedRole);
    }

    @Test
    void getRoleByName_existingRole_ok() {
        when(roleDao.getRoleByName(ADMIN.name())).thenReturn(Optional.of(savedRole));
        Role actual = roleService.getRoleByName(ADMIN.name());
        assertEquals(actual, savedRole);
    }

    @Test
    void getRoleByName_notExistingRole_notOk() {
        lenient().when(roleDao.getRoleByName(ADMIN.name())).thenReturn(Optional.of(savedRole));
        assertThrows(NoSuchElementException.class, () -> roleService.getRoleByName(USER.name()));
    }
}

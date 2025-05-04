package mate.academy.service;

import static mate.academy.model.Role.RoleName.ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        assertEquals(actual, savedRole,
                "Method should return saved role '%s'"
                        .formatted(savedRole));
    }

    @Test
    void getRoleByName_existingRole_ok() {
        when(roleDao.getRoleByName(role.getRoleName().name())).thenReturn(Optional.of(savedRole));
        Role actual = roleService.getRoleByName(role.getRoleName().name());
        assertEquals(actual, savedRole,
                "Method should return role '%s' but returned '%s' for roleName '%s'"
                        .formatted(savedRole, actual, role.getRoleName().name()));
    }

    @Test
    void getRoleByName_notExistingRole_notOk() {
        String notExistingRole = "ROLE_NOT_EXIST";
        when(roleDao.getRoleByName(notExistingRole)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> roleService.getRoleByName(notExistingRole),
                "Method should throw '%s' for not existing role name"
                        .formatted(NoSuchElementException.class));
    }
}

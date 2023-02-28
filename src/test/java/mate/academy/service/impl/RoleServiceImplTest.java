package mate.academy.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {
    @Mock
    private RoleDao roleDao;
    @InjectMocks
    private RoleServiceImpl roleService;
    private Role role;
    private Role.RoleName roleName;

    @BeforeEach
    void setUp() {
        roleName = Role.RoleName.USER;
        role = new Role(roleName);
        role.setId(1L);
    }

    @Test
    void saveRole_ok() {
        Role inputRole = new Role(roleName);
        Mockito.when(roleDao.save(inputRole)).thenReturn(role);
        Role actual = roleService.save(inputRole);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role, actual);
    }

    @Test
    void getRoleByName_ok() {
        Mockito.when(roleDao.getRoleByName(roleName.name())).thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(roleName.name());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual, role);
    }

    @Test
    void getRoleByIncorrectName_ok() {
        Mockito.when(roleDao.getRoleByName(roleName.name())).thenReturn(Optional.empty());
        Assertions.assertEquals("No value present",
                Assertions.assertThrows(NoSuchElementException.class,
                        () -> roleService.getRoleByName(roleName.name())).getMessage());
    }
}

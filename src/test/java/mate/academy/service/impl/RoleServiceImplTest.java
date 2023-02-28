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
    void save_Ok() {
        Role inputRole = new Role(roleName);
        Mockito.when(roleDao.save(inputRole)).thenReturn(role);
        Role actual = roleService.save(inputRole);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role, actual);
    }

    @Test
    void getRoleByName_Ok() {
        Mockito.when(roleDao.getRoleByName(roleName.name())).thenReturn(Optional.of(role));
        Role actual = roleService.getRoleByName(roleName.name());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(role, actual);
    }

    @Test
    void getRoleByName_NotExistentRole_NotOk() {
        Mockito.when(roleDao.getRoleByName(roleName.name())).thenReturn(Optional.empty());
        NoSuchElementException thrown =
                Assertions.assertThrows(NoSuchElementException.class,
                        () -> roleService.getRoleByName(roleName.name()));
        Assertions.assertEquals("No value present", thrown.getMessage());
    }
}

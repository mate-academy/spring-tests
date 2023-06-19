package mate.academy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleServiceTest {
    private static final Role.RoleName ROLE_USER = Role.RoleName.USER;
    private static final String ROLE_NAME = "USER";
    private RoleService underTest;
    private RoleDao roleDao;
    private Role role;

    @BeforeEach
    void setUp() {
        roleDao = mock(RoleDao.class);
        underTest = new RoleServiceImpl(roleDao);
        role = new Role(ROLE_USER);
    }

    @Test
    void saveSuccess() {
        when(underTest.save(role)).thenReturn(role);
        Role actual = underTest.save(role);
        assertEquals(role, actual);
    }

    @Test
    void getRoleByNameSuccess() {
        when(roleDao.getRoleByName(ROLE_NAME))
                .thenReturn(Optional.ofNullable(role));
        Role actual = underTest.getRoleByName(ROLE_NAME);
        assertEquals(role, actual);
    }

    @Test
    void getRoleByNameException() {
        assertThrows(NoSuchElementException.class,
                () -> underTest.getRoleByName(ROLE_NAME));
    }
}

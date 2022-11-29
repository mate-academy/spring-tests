package mate.academy.service.impl;

import java.lang.annotation.Target;
import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RoleServiceImplTest {
    private final static Role USER = new Role();
    private static final Role USER_SAVED = new Role();
    private static final String EXISTING_ROLE_NAME = "USER";
    private static final String NONEXISTENT_ROLE_NAME = "NONEXISTENT";
    private RoleService roleService;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        USER.setRoleName(Role.RoleName.USER);
        USER_SAVED.setRoleName(Role.RoleName.USER);
        USER_SAVED.setId(1L);
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_ok() {
        Mockito.when(roleDao.save(USER)).thenReturn(USER_SAVED);
        Role saved = roleService.save(USER);
        assertNotNull(saved.getId());
    }

    @Test
    void getRoleByName_ok() {
        Mockito.when(roleDao.getRoleByName(EXISTING_ROLE_NAME)).thenReturn(Optional.of(USER_SAVED));
        assertNotNull(roleService.getRoleByName(EXISTING_ROLE_NAME));
    }

    @Test
    void getRoleByName_notOk() {
        Mockito.when(roleDao.getRoleByName(NONEXISTENT_ROLE_NAME)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class,() -> roleService.getRoleByName(NONEXISTENT_ROLE_NAME));
    }
}
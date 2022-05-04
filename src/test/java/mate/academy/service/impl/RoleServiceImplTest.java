package mate.academy.service.impl;

import mate.academy.dao.RoleDao;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import mate.academy.util.UserTestUtil;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mockito;

import java.util.Optional;

class RoleServiceImplTest {
    private static final String ROLE_NAME = "USER";
    private RoleService roleService;
    private RoleDao roleDao;

    @BeforeEach
    void setUp() {
        roleDao = Mockito.mock(RoleDao.class);
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_Ok() {
        Mockito.when(roleDao.save(any(Role.class))).thenAnswer(invocation -> {
            Role role = invocation.getArgument(0);
            role.setId(1L);
            return role;
        });
        Role expected = UserTestUtil.getUserRole();
        Role actual = UserTestUtil.getUserRole();
        roleService.save(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals(expected.getRoleName(), actual.getRoleName());
    }

    @Test
    void getRoleByName_Ok() {
        Role expected = UserTestUtil.getUserRole();
        Mockito.when(roleDao.getRoleByName(ROLE_NAME))
                .thenReturn(Optional.of(expected));
        Role actual = roleService.getRoleByName(ROLE_NAME);
        Assertions.assertEquals(expected.getRoleName(),
                actual.getRoleName());
    }

    @Test
    void getRoleByName_NotOk() {
        try {
            roleService.getRoleByName(ROLE_NAME);
            Assertions.fail("it should thrown exception");
        } catch (RuntimeException ignored) { }
    }

}
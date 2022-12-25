package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private static final String USER_ROLE_NAME = "USER";
    private static final String WRONG_USER_ROLE_NAME = "TEACHER";
    private RoleDao roleDao;
    private Role user;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{User.class, Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        user = new Role(Role.RoleName.USER);
    }

    @Test
    void saveRole_Ok() {
        Role actual = roleDao.save(user);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(user, actual);
    }

    @Test
    void saveRoleWithNullValue_NotOk() {
        Assertions.assertThrows(DataProcessingException.class,
                () -> roleDao.save(null));
    }

    @Test
    void getRoleByName_Ok() {
        roleDao.save(user);
        String expected = USER_ROLE_NAME;
        Optional<Role> actual = roleDao.getRoleByName(expected);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual.get().getRoleName().toString());
    }

    @Test
    void getRoleByNameWrongRoleName_NotOk() {
        Assertions.assertThrows(DataProcessingException.class, () ->
                roleDao.getRoleByName(WRONG_USER_ROLE_NAME));
    }
}

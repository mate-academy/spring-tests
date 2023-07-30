package mate.academy.dao.impl;

import java.util.NoSuchElementException;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;
    private Role roleUser;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        roleUser = new Role(Role.RoleName.USER);
    }

    @Test
    void saveRole_RoleSaved_Ok() {
        Role role = roleDao.save(roleUser);
        Assertions.assertNotNull(role);
        Assertions.assertEquals(role.getId(),1L,
                "The id of actual role doesn't match the expected id");
    }

    @Test
    void getRole_GetRoleByName_Ok() {
        Role actual = roleDao.save(roleUser);
        Role expected = roleDao.getRoleByName(roleUser.getRoleName().name()).get();
        Assertions.assertEquals(expected.getId(),1L);
        Assertions.assertEquals(expected.getRoleName(), actual.getRoleName(),
                "The name of actual role doesn't match the expected name");
    }

    @Test
    void getRoleByName_GetNonExistingRole_NotOk() {
        Assertions.assertThrows(DataProcessingException.class, () ->
                roleDao.getRoleByName(null).get(),
                "DataProcessingException for non-existing role expected");
    }

    @Test
    void getRoleByName_GetWrongRole_NotOk() {
        roleDao.save(roleUser);
        Assertions.assertThrows(NoSuchElementException.class, () ->
                roleDao.getRoleByName("ADMIN").get(),
                "NoSuchElementException for wrong role expected");
    }
}

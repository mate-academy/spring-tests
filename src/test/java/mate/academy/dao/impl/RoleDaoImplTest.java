package mate.academy.dao.impl;

import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.util.UserUtilForTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest{
    private static RoleDao roleDao;
    private Role admin;
    private Role user;
    private UserUtilForTest userUtil;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        admin = new Role(Role.RoleName.ADMIN);
        user = new Role(Role.RoleName.USER);
        userUtil = new UserUtilForTest();
    }

    @Test
    void saveRole_ok() {
        Role actual = roleDao.save(admin);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L,actual.getId());
    }

    @Test
    void getRoleByName_ok() {
        roleDao.save(admin);
        roleDao.save(user);
        Role admin = roleDao.getRoleByName(userUtil.getAdminRole().getRoleName().name()).get();
        Role user = roleDao.getRoleByName(userUtil.getUserRole().getRoleName().name()).get();
        Assertions.assertNotNull(admin);
        Assertions.assertNotNull(user);
        Assertions.assertEquals(1L, admin.getId());
        Assertions.assertEquals(2L, user.getId());
        Assertions.assertEquals(admin.getRoleName().name(),
                userUtil.getAdminRole().getRoleName().name());
        Assertions.assertEquals(user.getRoleName().name(),
                userUtil.getUserRole().getRoleName().name());
    }

    @Test
    void getRoleByName_dataProcessingException() {
        try {
            roleDao.getRoleByName("STUDENT");
        } catch (DataProcessingException e) {
            Assertions.assertEquals("Couldn't get role by role name: STUDENT",
                    e.getMessage());
            return;
        }
        Assertions.fail();
    }
}
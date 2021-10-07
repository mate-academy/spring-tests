package mate.academy.service.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import mate.academy.dao.RoleDao;
import mate.academy.dao.impl.AbstractTest;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import mate.academy.service.RoleService;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleServiceImplTest extends AbstractTest {
    private static final String TEST_ROLE_NAME = "ADMIN";
    private RoleService roleService;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void setUp() throws Exception {
        Class[] cArg = new Class[1];
        cArg[0] = SessionFactory.class;

        Constructor constructor = RoleDaoImpl.class.getDeclaredConstructor(cArg);
        constructor.setAccessible(true);
        Assertions.assertTrue(Modifier.isProtected(constructor.getModifiers()),
                "Constructor is not protected");

        RoleDao roleDao = (RoleDao) constructor.newInstance(getSessionFactory());
        roleService = new RoleServiceImpl(roleDao);
    }

    @Test
    void save_Ok() {
        Role roleAdmin = new Role();
        roleAdmin.setRoleName(Role.RoleName.ADMIN);
        Role actual = roleService.save(roleAdmin);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(TEST_ROLE_NAME, actual.getRoleName().name());
    }

    @Test
    void getRoleByName_Ok() {
        Role roleAdmin = new Role();
        roleAdmin.setRoleName(Role.RoleName.ADMIN);
        roleService.save(roleAdmin);
        Role actual = roleService.getRoleByName(TEST_ROLE_NAME);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
    }
}
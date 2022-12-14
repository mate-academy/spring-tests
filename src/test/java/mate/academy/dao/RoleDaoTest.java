package mate.academy.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.model.Role;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class RoleDaoTest extends AbstractTest {
    private RoleDao roleDao;
    private Role userRole;
    private Role adminRole;

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }

    @BeforeEach
    void beforeEach() {
        userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        adminRole = new Role();
        adminRole.setRoleName(Role.RoleName.ADMIN);
        roleDao = reflectiveInitializeProtectedConstructor();
    }

    @Test
    void saveOneRole_Ok() {
        Role actual = roleDao.save(userRole);
        assertNotNull(actual, "You must return Role object");
        assertEquals(1L, actual.getId(), "In you Role object you need to add Id");
    }

    @Test
    void saveTwoRoles_Ok() {
        Role firstRole = roleDao.save(userRole);
        Role secondRole = roleDao.save(adminRole);
        assertEquals(1L, firstRole.getId(), "First role in saving query must have id 1");
        assertEquals(2L, secondRole.getId(), "Second role in saving query must have id 2");
    }

    @Test
    void getRoleByName_Ok() {
        Role expected = roleDao.save(userRole);
        assertEquals(expected.getRoleName(),
                roleDao.getRoleByName(expected.getRoleName().name()).get().getRoleName(),
                "You must return role with the same name");
    }

    @Test
    void getRoleByName_NotOk() {
        assertEquals(Optional.empty(), roleDao.getRoleByName(userRole.getRoleName().name()),
                "You must return empty Optional when can't find name");
    }

    private RoleDao reflectiveInitializeProtectedConstructor() {
        RoleDao tempDao = Mockito.mock(RoleDaoImpl.class, Mockito.CALLS_REAL_METHODS);
        Constructor<? extends RoleDao> superclass;
        RoleDao initializedDao;
        try {
            superclass = tempDao.getClass().getConstructor(SessionFactory.class);
            superclass.setAccessible(true);
            initializedDao = superclass.newInstance(getSessionFactory());
        } catch (NoSuchMethodException | InvocationTargetException
                 | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return initializedDao;
    }
}

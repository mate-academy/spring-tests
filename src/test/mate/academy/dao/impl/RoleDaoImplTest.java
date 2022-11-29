package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoImplTest extends AbstractTest {
    private static final Role USER = new Role();
    private static final Role ADMIN = new Role();
    private RoleDao roleDao;
    private SessionFactory factory;

    @BeforeEach
    void setUp() {
        USER.setRoleName(Role.RoleName.USER);
        ADMIN.setRoleName(Role.RoleName.ADMIN);
        factory = getSessionFactory();
        roleDao = new RoleDaoImpl(factory);
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            session.save(USER);
            session.save(ADMIN);
            session.getTransaction().commit();
        }
    }

    @Test
    void getRoleByName_ok() {
        Optional<Role> user = roleDao.getRoleByName("USER");
        Optional<Role> admin = roleDao.getRoleByName("ADMIN");
        assertTrue(user.isPresent());
        assertTrue(admin.isPresent());
        assertEquals(Role.RoleName.USER, user.get().getRoleName());
        assertEquals(Role.RoleName.ADMIN, admin.get().getRoleName());
    }

    @Test
    void getRoleByInvalidName_notOk() {
        assertThrows(DataProcessingException.class, () -> roleDao.getRoleByName("invalidName"));
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class};
    }
}
package mate.academy.dao;

import static mate.academy.model.Role.RoleName.ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import mate.academy.AbstractTest;
import mate.academy.dao.impl.RoleDaoImpl;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleDaoTest extends AbstractTest {
    private RoleDao roleDao;
    private Role role;

    @Override
    protected Class<?>[] entities() {
        return new Class[]{Role.class};
    }

    @BeforeEach
    void setUp() {
        roleDao = new RoleDaoImpl(getSessionFactory());
        role = new Role();
        role.setRoleName(ADMIN);
    }

    @Test
    void save_Ok() {
        Role actual = roleDao.save(role);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
        assertEquals(ADMIN, actual.getRoleName());
    }

    @Test
    void save_RoleIsNull_NotOk() {
        assertThrows(DataProcessingException.class, () -> {
            roleDao.save(null);
        });
    }

    @Test
    void getRoleByName_Ok() {
        Role admin = new Role(ADMIN);
        Session session = getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(admin);
        transaction.commit();
        session.close();
        Optional<Role> optionalRole = roleDao.getRoleByName(ADMIN.name());
        assertTrue(optionalRole.isPresent());
        assertEquals(admin, optionalRole.get());
    }

    @Test
    void getRoleByName_NameIsNull_NotOk() {
        assertThrows(DataProcessingException.class, () -> {
            roleDao.getRoleByName(null);
        });
    }

    @Test
    void getRoleByName_InvalidName_NotOk() {
        assertThrows(DataProcessingException.class, () -> {
            roleDao.getRoleByName("admin");
        });
    }
}

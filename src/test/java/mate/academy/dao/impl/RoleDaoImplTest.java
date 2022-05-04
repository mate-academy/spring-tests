package mate.academy.dao.impl;

import mate.academy.dao.AbstractTest;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import mate.academy.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mockito;

import java.util.Optional;

class RoleDaoImplTest extends AbstractTest {
    private RoleDao roleDao;
    private SessionFactory sessionFactory;

    @BeforeEach
    void setUp() {
        sessionFactory = Mockito.spy(getSessionFactory());
        roleDao = new RoleDaoImpl(sessionFactory);
    }

    @Test
    void save_Ok() {
        Role role = roleDao.save(getRoleUser());
        Assertions.assertEquals(1L , role.getId());
        Assertions.assertEquals(Role.RoleName.USER, role.getRoleName());
    }

    @Test
    void saveNull_NotOk() {
        try {
            Role role = roleDao.save(null);
            Assertions.fail("it should thrown exception");
        } catch (DataProcessingException e) {
            Assertions.assertTrue(e.getMessage().contains("Can't create entity: "));
        }
    }

    @Test
    void getRoleByName_Ok() {
        Session session = Mockito.mock(Session.class);
        Mockito.when(sessionFactory.openSession())
                .thenReturn(session);
        Query query = Mockito.mock(Query.class);
        Mockito.when(session.createQuery(anyString(), eq(Role.class)))
                .thenReturn(query);
        Role role = getRoleUser();
        role.setId(777L);
        Mockito.when(query.setParameter(anyString(), eq(Role.RoleName.USER)))
                .thenReturn(query);
        Mockito.when(query.uniqueResultOptional())
                .thenReturn(Optional.of(role));
        Optional<Role> actual = roleDao
                .getRoleByName(Role.RoleName.USER.name());
        Assertions.assertFalse(actual.isEmpty());
        Assertions.assertEquals(Role.RoleName.USER, actual.get().getRoleName());
        Assertions.assertEquals(777L, actual.get().getId());

    }

    @Test
    void getRoleByNameNoInDatabase_NotOk() {
            Optional<Role> noPresentedRole = roleDao
                    .getRoleByName(Role.RoleName.ADMIN.name());
            Assertions.assertTrue(noPresentedRole.isEmpty());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class, User.class};
    }

    private Role getRoleUser() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        return role;
    }
}
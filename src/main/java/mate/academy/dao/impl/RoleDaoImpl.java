package mate.academy.dao.impl;

import java.util.Optional;
import mate.academy.dao.RoleDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.Role;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDaoImpl extends AbstractDao<Role, Long> implements RoleDao {

    @Autowired
    protected RoleDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Role.class);
    }

    @Override
    public Optional<Role> getRoleByName(String roleName) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("select r from Role r "
                    + "where r.roleName = :roleName", Role.class)
                    .setParameter("roleName", Role.RoleName.valueOf(roleName))
                    .uniqueResultOptional();
        } catch (Exception e) {
            throw new DataProcessingException("Couldn't get role by role name: "
                    + roleName, e);
        }
    }
}

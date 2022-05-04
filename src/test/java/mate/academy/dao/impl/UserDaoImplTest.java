package mate.academy.dao.impl;

import mate.academy.dao.AbstractTest;
import mate.academy.dao.UserDao;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.util.UserTestUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mockito;

import java.util.Optional;

class UserDaoImplTest extends AbstractTest {
    private UserDao userDao;
    private SessionFactory sessionFactory;

    @BeforeEach
    void setUp() {
        sessionFactory = Mockito.spy(getSessionFactory());
        userDao = new UserDaoImpl(sessionFactory);
    }

    @Test
    void findByEmail_Ok() {
        Session session = Mockito.mock(Session.class);
        Mockito.when(sessionFactory.openSession()).thenReturn(session);
        Query query = Mockito.mock(Query.class);
        Mockito.when(session.createQuery(anyString(), eq(User.class)))
                .thenReturn(query);
        Mockito.when(query.setParameter(anyString(), eq(UserTestUtil.EMAIL)))
                .thenReturn(query);
        Mockito.when(query.uniqueResultOptional())
                .thenReturn(Optional.of(UserTestUtil.getUserBob()));
        userDao.findByEmail(UserTestUtil.EMAIL);
    }

    @Test
    void findByEmailNoInDataBase_NotOk() {
        Optional<User> user = userDao
                .findByEmail(UserTestUtil.INCORRECT_EMAIL);
        Assertions.assertTrue(user.isEmpty());
    }

    @Test
    void findById_Ok() {
        Session session = Mockito.mock(Session.class);
        Mockito.when(sessionFactory.openSession()).thenReturn(session);
        User expected = UserTestUtil.getUserBob();
        expected.setId(1L);
        Mockito.when(session.get(eq(User.class), eq(1L)))
                .thenReturn(expected);
        Optional<User> actual = userDao.findById(1L);
        Assertions.assertTrue(actual.isPresent());
        User actualUser = actual.get();
        Assertions.assertEquals(expected.getEmail(), actualUser.getEmail());
        Assertions.assertEquals(expected.getPassword(), actualUser.getPassword());
        Assertions.assertEquals(expected.getId(), actualUser.getId());
        Assertions.assertEquals(UserTestUtil.getListOfStringRoles(expected),
                UserTestUtil.getListOfStringRoles(actualUser));
    }

    @Test
    void findById_NotOk() {
        Optional<User> noPresentedUser = userDao.findById(777L);
        Assertions.assertTrue(noPresentedUser.isEmpty());
    }

    @Test
    void save_Ok() {
        User expected = UserTestUtil.getUserBob();
        Session session = Mockito.mock(Session.class);
        Mockito.when(sessionFactory.openSession())
                .thenReturn(session);
        Mockito.when(session.beginTransaction())
                .thenReturn(Mockito.mock(Transaction.class));
        Mockito.when(session.save(any(User.class))).thenAnswer(invocation -> {
            Object argument = invocation.getArgument(0);
            if (argument.getClass() == User.class
                    && ((User) argument).getEmail().equals(expected.getEmail())
                    && ((User) argument).getPassword().equals(expected.getPassword())) {
                ((User) argument).setId(1L);
            }
            return 1L;
        });
        userDao.save(expected);
        Assertions.assertEquals(1L, expected.getId());
    }

    @Override
    protected Class<?>[] entities() {
        return new Class[] {Role.class, User.class};
    }
}
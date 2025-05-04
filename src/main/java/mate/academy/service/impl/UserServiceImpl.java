package mate.academy.service.impl;

import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User save(User user) {
        if (user != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userDao.save(user);
        }
        throw new RuntimeException("User can't be null");
    }

    @Override
    public Optional<User> findById(Long id) {
        if (id != null) {
            return userDao.findById(id);
        }
        throw new RuntimeException("Id can't be null");
    }

    @Override
    public Optional<User> findByEmail(String email) {
        if (email != null) {
            return userDao.findByEmail(email);
        }
        throw new RuntimeException("Email can't be null");
    }
}

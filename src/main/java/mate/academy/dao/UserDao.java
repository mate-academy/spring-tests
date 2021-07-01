package mate.academy.dao;

import java.util.Optional;
import mate.academy.model.User;

public interface UserDao {
    User save(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);
}

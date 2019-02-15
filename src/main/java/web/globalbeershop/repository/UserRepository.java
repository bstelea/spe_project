package web.globalbeershop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.globalbeershop.data.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByUsername(String username);
}

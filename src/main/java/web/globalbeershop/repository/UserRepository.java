package web.globalbeershop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import web.globalbeershop.data.Order;
import web.globalbeershop.data.User;

public interface UserRepository extends JpaRepository<User, Long>,
        QuerydslPredicateExecutor<User> {
    User findByEmail(String email);
}
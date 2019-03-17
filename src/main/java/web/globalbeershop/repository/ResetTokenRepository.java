package web.globalbeershop.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import web.globalbeershop.data.ActivationToken;
import web.globalbeershop.data.ResetToken;
import web.globalbeershop.data.User;

public interface ResetTokenRepository extends JpaRepository<ResetToken, Long>,
        QuerydslPredicateExecutor<ResetToken> {

    ResetToken findByToken(String token);

    ResetToken findByUser(User user);
}
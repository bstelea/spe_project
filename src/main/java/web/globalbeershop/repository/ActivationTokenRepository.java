package web.globalbeershop.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import web.globalbeershop.data.ActivationToken;
import web.globalbeershop.data.User;

public interface ActivationTokenRepository
        extends JpaRepository<ActivationToken, Long> {

    ActivationToken findByToken(String token);

    ActivationToken findByUser(User user);
}
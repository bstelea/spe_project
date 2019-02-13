package web.globalbeershop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.globalbeershop.data.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

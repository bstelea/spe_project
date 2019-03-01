package web.globalbeershop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.globalbeershop.data.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
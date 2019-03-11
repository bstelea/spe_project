package web.globalbeershop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import web.globalbeershop.data.Order;
import web.globalbeershop.data.OrderItem;

public interface OrderItemRepository  extends JpaRepository<OrderItem, Long>,
        QuerydslPredicateExecutor<OrderItem> {}
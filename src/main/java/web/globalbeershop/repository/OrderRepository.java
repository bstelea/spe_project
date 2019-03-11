package web.globalbeershop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.globalbeershop.data.Order;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>,
        QuerydslPredicateExecutor<Order> {}

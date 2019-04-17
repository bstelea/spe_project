package web.globalbeershop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.globalbeershop.data.Review;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>,
        QuerydslPredicateExecutor<Review> {}

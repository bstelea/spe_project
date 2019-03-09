package web.globalbeershop.data;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "order_item")
public class OrderItem {
        @Id
        @GeneratedValue
        @Column(name = "id")
        private Long id;

        @ManyToOne
        @JoinColumn(name = "order_id")
        private Order order;

        @ManyToOne
        @JoinColumn(name = "beer_id")
        private Beer beer;

        @Column(name = "quantity")
        private Integer quantity;

    public OrderItem(Order order, Beer beer, Integer quantity) {
        this.order = order;
        this.beer = beer;
        this.quantity = quantity;
    }

    public OrderItem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Beer getBeer() {
        return beer;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

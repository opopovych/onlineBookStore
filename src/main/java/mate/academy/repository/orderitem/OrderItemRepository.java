package mate.academy.repository.orderitem;

import java.util.List;
import java.util.Optional;
import mate.academy.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
    @Query("FROM OrderItem oi "
            + "LEFT JOIN FETCH oi.order o "
            + "LEFT JOIN FETCH oi.book "
            + "WHERE o.id = :orderId ")
    List<OrderItem> findAllByOrderId(Long orderId);

    @Query("FROM OrderItem oi "
            + "LEFT JOIN FETCH oi.order o "
            + "LEFT JOIN FETCH oi.book "
            + "WHERE oi.id = :id "
            + "AND o.id = :orderId ")
    Optional<OrderItem> findByIdAndOrderId(Long id, Long orderId);
}

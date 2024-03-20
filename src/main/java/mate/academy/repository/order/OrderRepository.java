package mate.academy.repository.order;

import java.util.List;
import mate.academy.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query("FROM Order o "
            + "LEFT JOIN FETCH o.orderItems "
            + "WHERE o.user.id = :userId ")
    List<Order> findAllByUserId(Long userId);
}

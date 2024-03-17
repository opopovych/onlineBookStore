package mate.academy.repository.cartitem;

import mate.academy.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Modifying
    @Query("UPDATE CartItem item "
            + "SET item.deleted = true "
            + "WHERE item.shoppingCart.id = :cartId")
    void deleteByShoppingCartId(Long cartId);
}

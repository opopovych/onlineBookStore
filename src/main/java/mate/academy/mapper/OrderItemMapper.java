package mate.academy.mapper;

import java.math.BigDecimal;
import mate.academy.config.MapperConfig;
import mate.academy.dto.order.OrderItemResponseDto;
import mate.academy.model.CartItem;
import mate.academy.model.OrderItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "quantity", source = "quantity")
    @Named("orderItemsToDto")
    OrderItemResponseDto toDto(OrderItem orderItem);

    @Mapping(target = "order", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "book.price", target = "price")
    OrderItem toOrderItem(CartItem cartItem);

    @AfterMapping
    default void getPriceOfOrderItem(@MappingTarget OrderItem orderItem, CartItem cartItem) {
        orderItem.setPrice(cartItem.getBook().getPrice().multiply(
                BigDecimal.valueOf(cartItem.getQuantity())));
    }
}

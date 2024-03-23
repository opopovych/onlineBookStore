package mate.academy.service;

import java.util.List;
import mate.academy.dto.order.OrderItemResponseDto;

public interface OrderItemService {
    List<OrderItemResponseDto> getAllByOrderId(Long orderId);

    OrderItemResponseDto getByIdAndOrderId(Long id, Long orderId);
}

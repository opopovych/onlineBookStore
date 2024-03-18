package mate.academy.service;

import java.util.List;
import mate.academy.dto.order.OrderRequestDto;
import mate.academy.dto.order.OrderResponseDto;
import mate.academy.dto.order.OrderUpdateRequestDto;

public interface OrderService {
    OrderResponseDto placeOrder(Long userId, OrderRequestDto requestDto);

    List<OrderResponseDto> getAllByUserId(Long userId);

    OrderResponseDto updateStatus(Long orderId, OrderUpdateRequestDto requestDto);
}

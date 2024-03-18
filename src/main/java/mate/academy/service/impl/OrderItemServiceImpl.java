package mate.academy.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.order.OrderItemResponseDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.OrderItemMapper;
import mate.academy.repository.orderitem.OrderItemRepository;
import mate.academy.service.OrderItemService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemMapper orderItemMapper;
    private final OrderItemRepository orderItemRepository;

    @Override
    public List<OrderItemResponseDto> getAllByOrderId(Long orderId) {
        return orderItemRepository.findAllByOrderId(orderId)
                 .stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemResponseDto getByIdAndOrderId(Long id, Long orderId) {
        return orderItemMapper.toDto(orderItemRepository.findByIdAndOrderId(id,orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order item with id: "
                + id + "is not found")));
    }
}

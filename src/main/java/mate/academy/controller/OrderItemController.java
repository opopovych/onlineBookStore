package mate.academy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.order.OrderItemResponseDto;
import mate.academy.service.OrderItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/orders")
@RequiredArgsConstructor
@Tag(name = "Order management", description = "Endpoints for managing orders")
public class OrderItemController {
    private final OrderItemService orderItemService;

    @GetMapping("/{id}/items")
    @Operation(summary = "Get all items by order id")
    public List<OrderItemResponseDto> getAllById(@PathVariable Long id) {
        return orderItemService.getAllByOrderId(id);
    }

    @GetMapping("/{id}/items/{itemId}")
    @Operation(summary = "Get item from order by id")
    public OrderItemResponseDto getItemById(@PathVariable Long id, @PathVariable Long itemId) {
        return orderItemService.getByIdAndOrderId(itemId, id);
    }
}

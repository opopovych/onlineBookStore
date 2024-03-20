package mate.academy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.order.OrderRequestDto;
import mate.academy.dto.order.OrderResponseDto;
import mate.academy.dto.order.OrderUpdateRequestDto;
import mate.academy.model.User;
import mate.academy.service.OrderItemService;
import mate.academy.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/orders")
@RequiredArgsConstructor
@Tag(name = "Order management", description = "Endpoints for managing orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @PostMapping
    @Operation(summary = "Place order", description = "Place order")
    public OrderResponseDto placeOrder(@RequestBody OrderRequestDto requestDto) {
        return orderService.placeOrder(getCurrentUserId(), requestDto);
    }

    @GetMapping
    @Operation(summary = "Get all orders", description = "Return all orders")
    public List<OrderResponseDto> getAll() {
        return orderService.getAllByUserId(getCurrentUserId());
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Change order status")
    public OrderResponseDto updateStatus(@PathVariable Long id,
            @RequestBody OrderUpdateRequestDto requestDto) {
        return orderService.updateStatus(id, requestDto);
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            return user.getId();
        }
        throw new IllegalStateException("User is not authenticated");
    }
}

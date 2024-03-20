package mate.academy.service.impl;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.order.OrderRequestDto;
import mate.academy.dto.order.OrderResponseDto;
import mate.academy.dto.order.OrderUpdateRequestDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.OrderItemMapper;
import mate.academy.mapper.OrderMapper;
import mate.academy.model.CartItem;
import mate.academy.model.Order;
import mate.academy.model.OrderItem;
import mate.academy.model.ShoppingCart;
import mate.academy.repository.cartitem.CartItemRepository;
import mate.academy.repository.order.OrderRepository;
import mate.academy.repository.orderitem.OrderItemRepository;
import mate.academy.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.repository.user.UserRepository;
import mate.academy.service.OrderService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public OrderResponseDto placeOrder(Long userId, OrderRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("shopping cart by user id : "
                        + userId + "is not found"));
        Order order = createOrder(userId, shoppingCart, requestDto);
        Set<OrderItem> orderItems = shoppingCart.getCartItems().stream()
                .map(this::convertToOrderItem)
                .collect(Collectors.toSet());
        orderItems.forEach(orderItem -> orderItem.setOrder(order));
        orderItemRepository.saveAll(orderItems);
        cartItemRepository.deleteAll(shoppingCart.getCartItems());
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderResponseDto> getAllByUserId(Long userId) {
        return orderRepository.findAllByUserId(userId).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderResponseDto updateStatus(Long orderId, OrderUpdateRequestDto requestDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Order with id '" + orderId + "' not found")
        );

        order.setStatus(requestDto.status());

        return orderMapper.toDto(orderRepository.save(order));
    }

    private BigDecimal getTotalPrice(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(cartItem -> cartItem.getBook()
                        .getPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private OrderItem convertToOrderItem(CartItem cartItem) {
        OrderItem orderItem = new OrderItem();
        orderItem.setBook(cartItem.getBook());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(cartItem.getBook().getPrice());
        return orderItem;
    }

    private Set<OrderItem> createOrderItemsSet(ShoppingCart shoppingCart, Order order) {
        return shoppingCart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = orderItemMapper.toOrderItem(cartItem);
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .peek(orderItemRepository::save)
                .collect(Collectors.toSet());
    }

    private Order createOrder(Long id, ShoppingCart shoppingCart, OrderRequestDto requestDto) {
        Order order = new Order();
        order.setUser(userRepository.getReferenceById(id));
        order.setStatus(Order.Status.PENDING);
        order.setShippingAddress(requestDto.shippingAddress());
        order.setTotal(getTotalPrice(shoppingCart.getCartItems()));
        order.setOrderDate(LocalDateTime.now());
        orderRepository.save(order);
        return order;
    }

}

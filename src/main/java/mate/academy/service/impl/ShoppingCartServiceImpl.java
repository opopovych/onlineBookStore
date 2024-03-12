package mate.academy.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.cartitem.AddItemToCartRequestDto;
import mate.academy.dto.cartitem.UpdateCartItemRequestDto;
import mate.academy.dto.shoppingcart.ShoppingCartDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.CartItemMapper;
import mate.academy.mapper.ShoppingCartMapper;
import mate.academy.model.CartItem;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.repository.cartitem.CartItemRepository;
import mate.academy.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.repository.user.UserRepository;
import mate.academy.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final CartItemMapper cartItemMapper;
    private final ShoppingCartMapper cartMapper;

    @Transactional
    @Override
    public ShoppingCartDto addBookToCartByUserId(Long userId, AddItemToCartRequestDto requestDto) {
        ShoppingCart cartFromDb = cartFromDb(userId);
        CartItem requestedItem = cartItemMapper.toModel(requestDto);
        requestedItem.setShoppingCart(cartFromDb);
        cartItemRepository.save(requestedItem);
        return cartMapper.toDto(cartFromDb(userId));
    }

    @Override
    public ShoppingCartDto getCartByUserId(Long id) {
        return cartMapper.toDto(cartFromDb(id));
    }

    @Override
    public ShoppingCartDto updateCartItemById(
            Long userId, Long cartItemId, UpdateCartItemRequestDto requestDto) {
        CartItem itemFromDb = getItemFromDb(cartItemId);
        cartItemMapper.updateFromDto(requestDto, itemFromDb);
        cartItemRepository.save(itemFromDb);
        return cartMapper.toDto(cartFromDb(userId));
    }

    @Override
    public ShoppingCartDto delete(Long userId, Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
        return cartMapper.toDto(cartFromDb(userId));
    }

    @Override
    public void clearShoppingCart(Long cartId) {
        cartItemRepository.deleteByShoppingCartId(cartId);
    }

    private CartItem getItemFromDb(Long id) {
        return cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find item by id=" + id));
    }

    private ShoppingCart cartFromDb(Long userId) {
        return shoppingCartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    ShoppingCart shoppingCart = new ShoppingCart();
                    User userFromDb = userRepository.findById(userId)
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Can't find user by id=" + userId));
                    shoppingCart.setUser(userFromDb);
                    return shoppingCartRepository.save(shoppingCart);
                });
    }
}

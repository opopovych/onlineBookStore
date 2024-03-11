package mate.academy.service.impl;

import java.util.HashSet;
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

    @Override
    public ShoppingCart createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setCartItems(new HashSet<>());
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartDto addBooksToCartByUserId(Long userId, AddItemToCartRequestDto requestDto) {
        ShoppingCart cartFromDb = getCartFromDb(userId);
        CartItem requestedItem = cartItemMapper.toModel(requestDto);
        requestedItem.setShoppingCart(cartFromDb);
        cartItemRepository.save(requestedItem);
        return cartMapper.toDto(getCartFromDb(userId));
    }

    @Override
    public ShoppingCart getCartByUserId(Long id) {
        return getCartFromDb(id);
    }

    @Override
    public ShoppingCartDto getCartDtoByUserId(Long id) {
        return cartMapper.toDto(getCartFromDb(id));
    }

    @Override
    public ShoppingCartDto updateCartItemById(
            Long userId, Long cartItemId, UpdateCartItemRequestDto requestDto) {
        CartItem itemFromDb = getItemFromDb(cartItemId);
        cartItemMapper.updateFromDto(requestDto, itemFromDb);
        cartItemRepository.save(itemFromDb);
        return cartMapper.toDto(getCartFromDb(userId));
    }

    @Override
    public ShoppingCartDto delete(Long userId, Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
        return cartMapper.toDto(getCartFromDb(userId));
    }

    @Override
    public void clearShoppingCart(Long cartId) {
        cartItemRepository.deleteByShoppingCartId(cartId);
    }

    private CartItem getItemFromDb(Long id) {
        return cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find item by id=" + id));
    }

    private ShoppingCart getCartFromDb(Long userId) {
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

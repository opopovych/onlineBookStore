package mate.academy.service;

import mate.academy.dto.cartItem.AddItemToCartRequestDto;
import mate.academy.dto.cartItem.UpdateCartItemRequestDto;
import mate.academy.dto.shoppingCart.ShoppingCartDto;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;

public interface ShoppingCartService {
    ShoppingCart createShoppingCart(User user);

    ShoppingCartDto addBooksToCartByUserId(Long userId, AddItemToCartRequestDto requestDto);

    ShoppingCartDto getCartDtoByUserId(Long id);

    ShoppingCart getCartByUserId(Long id);

    ShoppingCartDto updateCartItemById(
            Long userId, Long cartItemId, UpdateCartItemRequestDto requestDto);

    ShoppingCartDto delete(Long userId, Long cartItemId);

    void clearShoppingCart(Long cartId);
}
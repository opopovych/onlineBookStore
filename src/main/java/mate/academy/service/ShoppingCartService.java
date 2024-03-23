package mate.academy.service;

import mate.academy.dto.cartitem.AddItemToCartRequestDto;
import mate.academy.dto.cartitem.UpdateCartItemRequestDto;
import mate.academy.dto.shoppingcart.ShoppingCartDto;

public interface ShoppingCartService {

    ShoppingCartDto addBookToCartByUserId(Long userId, AddItemToCartRequestDto requestDto);

    ShoppingCartDto getCartByUserId(Long id);

    ShoppingCartDto updateCartItemById(
            Long userId, Long cartItemId, UpdateCartItemRequestDto requestDto);

    ShoppingCartDto delete(Long userId, Long cartItemId);

    void clearShoppingCart(Long cartId);
}

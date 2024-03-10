package mate.academy.dto.shoppingCart;

import mate.academy.dto.cartItem.CartItemDto;

import java.util.Set;

public record ShoppingCartDto(
        Long id,
        Long userId,
        Set<CartItemDto> cartItems
){
}
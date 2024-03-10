package mate.academy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.cartItem.AddItemToCartRequestDto;
import mate.academy.dto.cartItem.UpdateCartItemRequestDto;
import mate.academy.dto.shoppingCart.ShoppingCartDto;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Shopping cart manager", description = "Endpoints for managing shopping carts")
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Operation(summary = "Get the shopping cart",
            description = "Get user's shopping cart, show what is in it")
    @GetMapping
    ShoppingCartDto getCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.getCartDtoByUserId(user.getId());
    }

    @Operation(summary = "Add books to the shopping cart",
            description = "Add books to the user's shopping cart")
    @PostMapping
    ShoppingCartDto addBooks(Authentication authentication,
                             @RequestBody @Valid AddItemToCartRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.addBooksToCartByUserId(user.getId(), requestDto);
    }

    @Operation(summary = "Update cart item",
            description = "Update cart item in the shopping cart")
    @PutMapping("/cart-items/{cartItemId}")
    ShoppingCartDto updateItems(Authentication authentication,
                                @PathVariable Long cartItemId,
                                @RequestBody @Valid UpdateCartItemRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.updateCartItemById(user.getId(), cartItemId, requestDto);
    }

    @Operation(summary = "Delete a cart item",
            description = "Delete a specific item from the shopping cart")
    @DeleteMapping("/cart-items/{cartItemId}")
    ShoppingCartDto deleteItems(Authentication authentication,
                                @PathVariable Long cartItemId) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.delete(user.getId(), cartItemId);
    }
}
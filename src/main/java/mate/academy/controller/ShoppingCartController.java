package mate.academy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.cartitem.AddItemToCartRequestDto;
import mate.academy.dto.cartitem.UpdateCartItemRequestDto;
import mate.academy.dto.shoppingcart.ShoppingCartDto;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart manager", description = "Endpoints for managing shopping carts")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Get the shopping cart",
            description = "Get user's shopping cart, show what is in it")
    @GetMapping
    ShoppingCartDto getCart() {
        return shoppingCartService.getCartByUserId(getCurrentUserId());
    }

    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Add books to the shopping cart",
            description = "Add books to the user's shopping cart")
    @PostMapping
    ShoppingCartDto addBooks(Authentication authentication,
                             @RequestBody @Valid AddItemToCartRequestDto requestDto) {
        return shoppingCartService.addBookToCartByUserId(getCurrentUserId(), requestDto);
    }

    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Update cart item",
            description = "Update cart item in the shopping cart")
    @PutMapping("/cart-items/{cartItemId}")
    ShoppingCartDto updateItems(Authentication authentication,
                                @PathVariable Long cartItemId,
                                @RequestBody @Valid UpdateCartItemRequestDto requestDto) {
        return shoppingCartService.updateCartItemById(getCurrentUserId(), cartItemId, requestDto);
    }

    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Delete a cart item",
            description = "Delete a specific item from the shopping cart")
    @DeleteMapping("/cart-items/{cartItemId}")
    ShoppingCartDto deleteItems(Authentication authentication,
                                @PathVariable Long cartItemId) {
        return shoppingCartService.delete(getCurrentUserId(), cartItemId);
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

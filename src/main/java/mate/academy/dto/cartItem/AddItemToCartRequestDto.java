package mate.academy.dto.cartItem;

import jakarta.validation.constraints.Positive;

public record AddItemToCartRequestDto(
        Long bookId,
        @Positive
        int quantity
) {
}
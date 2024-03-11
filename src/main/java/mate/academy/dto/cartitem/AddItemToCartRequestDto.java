package mate.academy.dto.cartitem;

import jakarta.validation.constraints.Positive;

public record AddItemToCartRequestDto(
        Long bookId,
        @Positive
        int quantity
) {
}

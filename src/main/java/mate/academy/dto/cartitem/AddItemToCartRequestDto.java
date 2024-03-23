package mate.academy.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddItemToCartRequestDto(
        @NotNull
        Long bookId,
        @Positive
        @NotNull
        int quantity
) {
}

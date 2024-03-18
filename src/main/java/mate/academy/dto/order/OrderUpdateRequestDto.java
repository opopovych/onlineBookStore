package mate.academy.dto.order;

import jakarta.validation.constraints.NotBlank;
import mate.academy.model.Order;

public record OrderUpdateRequestDto(
        @NotBlank
        Order.Status status
) {
}

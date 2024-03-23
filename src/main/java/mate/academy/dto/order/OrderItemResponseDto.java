package mate.academy.dto.order;

public record OrderItemResponseDto(
        Long id,
        Long bookId,
        int quantity
) {
}

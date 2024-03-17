package mate.academy.mapper;

import mate.academy.config.MapperConfig;
import mate.academy.dto.cartitem.AddItemToCartRequestDto;
import mate.academy.dto.cartitem.CartItemDto;
import mate.academy.dto.cartitem.UpdateCartItemRequestDto;
import mate.academy.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemDto toDto(CartItem cartItem);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    CartItem toModel(AddItemToCartRequestDto requestDto);

    @Mapping(target = "book", ignore = true)
    void updateFromDto(UpdateCartItemRequestDto requestDto, @MappingTarget CartItem itemFromDb);
}

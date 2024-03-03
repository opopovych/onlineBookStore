package mate.academy.mapper;

import java.util.HashSet;
import java.util.Set;
import mate.academy.config.MapperConfig;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.dto.book.CreateBookRequestDto;
import mate.academy.model.Book;
import mate.academy.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto createBookRequestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        Set<Long> categories = new HashSet<>();
        for (Category category : book.getCategories()) {
            categories.add(category.getId());
        }
        bookDto.setCategoryIds(categories);
    }

    @AfterMapping
    default void setCategories(@MappingTarget Book book, CreateBookRequestDto createBookDto) {
        Set<Category> categories = new HashSet<>();
        for (Long categoryId : createBookDto.getCategoryIds()) {
            categories.add(new Category(categoryId));
        }
        book.setCategories(categories);
    }

    @Named(value = "bookById")
    default Book bookById(Long id) {
        Book book = new Book();
        book.setId(id);
        return book;
    }
}

package mate.academy.service;

import java.util.List;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.dto.book.BookSearchParametersDto;
import mate.academy.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto createBookRequestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    BookDto removeById(Long id);

    BookDto update(Long id, CreateBookRequestDto updateBookRequestDto);

    List<BookDto> search(BookSearchParametersDto searchParameters);

    List<BookDtoWithoutCategoryIds> findByCategoryId(Long id);
}

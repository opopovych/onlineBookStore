package mate.academy.service;

import java.util.List;
import mate.academy.dto.BookDto;
import mate.academy.dto.BookSearchParametersDto;
import mate.academy.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto createBookRequestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);

    BookDto removeById(Long id);

    BookDto update(Long id, CreateBookRequestDto updateBookRequestDto);

    List<BookDto> search(BookSearchParametersDto searchParameters);
}

package mate.academy.service.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.CreateBookRequestDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.BookMapper;
import mate.academy.model.Book;
import mate.academy.repository.book.BookRepository;
import mate.academy.service.impl.BookServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookServiceImpl bookServiceImpl;

    @Test
    @DisplayName("Save a new book with valid request - Returns saved book DTO")
    void save_ValidCreateBookRequestDto_ReturnBookDto() {
        CreateBookRequestDto requestDto = createTestCreateBookRequestDto();
        Book book = createTestBook(requestDto);
        BookDto bookDto = createTestBookDto(book);

        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto savedBook = bookServiceImpl.save(requestDto);

        assertEquals(savedBook, bookDto);
    }

    @Test
    @DisplayName("Find all books with valid pageable"
            + " - Returns all books DTOs without category IDs")
    void findAll_ValidPageable_ReturnOneBook() {
        CreateBookRequestDto requestDto = createTestCreateBookRequestDto();
        Book book = createTestBook(requestDto);
        BookDto bookDto = createTestBookDto(book);

        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(book);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> bookDtos = bookServiceImpl.findAll(pageable);

        assertEquals(1, bookDtos.size());
    }

    @Test
    @DisplayName("Find book by invalid ID - Throws EntityNotFoundException")
    void findById_WithInvalidId_ShouldThrowException() {
        Long id = 999L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> bookServiceImpl.findById(id));
    }

    @Test
    @DisplayName("Remove Book by Id")
    void removeBookById() {
        Long id = 1L;
        Book book = createTestBook(createTestCreateBookRequestDto());
        BookDto expectedBookDto = createTestBookDto(book);

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expectedBookDto);

        BookDto actualBookDto = bookServiceImpl.removeById(id);

        assertEquals(expectedBookDto, actualBookDto);
        verify(bookRepository).findById(id);
        verify(bookRepository).delete(book);
    }

    @Test
    @DisplayName("update book by valid id")
    void update_ValidIdAndRequest() {
        Long validId = 1L;
        CreateBookRequestDto requestDto = createTestCreateBookRequestDto();

        Book updated = new Book();
        updated.setId(validId);
        updated.setTitle("updated title");
        updated.setAuthor("updated author");
        updated.setPrice(BigDecimal.valueOf(2));
        updated.setIsbn("12233434344");
        BookDto expectedDto = createTestBookDto(updated);

        Book book = createTestBook(requestDto);
        when(bookMapper.toModel(requestDto)).thenReturn(updated);
        when(bookRepository.save(updated)).thenReturn(updated);
        when(bookRepository.findById(validId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(updated)).thenReturn(expectedDto);

        BookDto actual = bookServiceImpl.update(validId, requestDto);
        assertEquals(expectedDto, actual);
    }

    private Book createTestBook(CreateBookRequestDto requestDto) {
        Book book = new Book();
        book.setTitle(requestDto.getTitle());
        book.setAuthor(requestDto.getAuthor());
        book.setPrice(requestDto.getPrice());
        book.setIsbn(requestDto.getIsbn());
        return book;
    }

    private BookDto createTestBookDto(Book modelBook) {
        BookDto bookDto = new BookDto();
        bookDto.setTitle(modelBook.getTitle());
        bookDto.setAuthor(modelBook.getAuthor());
        bookDto.setIsbn(modelBook.getIsbn());
        bookDto.setPrice(modelBook.getPrice());
        return bookDto;
    }

    private CreateBookRequestDto createTestCreateBookRequestDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Title 1");
        requestDto.setAuthor("Author 1");
        requestDto.setPrice(BigDecimal.valueOf(20));
        requestDto.setIsbn("1234567891");
        return requestDto;
    }
}

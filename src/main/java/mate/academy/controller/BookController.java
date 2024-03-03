package mate.academy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.BookSearchParametersDto;
import mate.academy.dto.book.CreateBookRequestDto;
import mate.academy.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book management",description = "Endpoints for managing books")
@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping
    @Operation(summary = "Get all books", description = "Get all books")
    public List<BookDto> getAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by id", description = "Get book by id")
    public BookDto getById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @Operation(summary = "Create book", description = "Create book")
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto createBookRequestDto) {
        return bookService.save(createBookRequestDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update book", description = "Update book")
    public BookDto updateBook(
            @PathVariable Long id,
            @RequestBody CreateBookRequestDto updateBookRequestDto) {
        if (id == null) {
            throw new RuntimeException("Can't find book by id - " + id);
        }
        return bookService.update(id, updateBookRequestDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Remove book by id", description = "remove book by id")
    public BookDto removeBook(@PathVariable Long id) {
        return bookService.removeById(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Search books by parameters", description = "Search books by parameters")
    public List<BookDto> searchBooks(BookSearchParametersDto searchParameters) {
        return bookService.search(searchParameters);
    }

}

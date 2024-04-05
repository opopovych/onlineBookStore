package mate.academy.controller.book;

import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.CreateBookRequestDto;
import mate.academy.service.BookService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookTestController {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookService bookService;

    @BeforeAll
    static void beforeAll(@Autowired DataSource dataSource,
                          @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/category/add-category-to-categories-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/book/add-two-books-to-books-table.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/book/remove-books-and-related-"
                            + "rows-from-books-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/category/remove-category.sql")
            );
        }
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Create book to DB")
    void createBook_ValidRequestDto_Success() throws Exception {
        CreateBookRequestDto book = createTestCreateBookRequestDto();
        BookDto expected = createTestBookDto(book);
        Mockito.when(bookService.save(createTestCreateBookRequestDto())).thenReturn(expected);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/books")).andReturn();
        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Get all books")
    void getAll_AllBooksInCatalog_Success() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        List<BookDto> expectedBooks = getBookDtoListMock();
        Mockito.when(bookService.findAll(pageable)).thenReturn(expectedBooks);
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/books")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("page", "0")
                                .param("size", "10")
                )
                .andExpect(status().isOk())
                .andReturn();
        BookDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), BookDto[].class);
        Assertions.assertEquals(2, actual.length);
        Assertions.assertEquals(expectedBooks, Arrays.stream(actual).toList());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Get book by id")
    void getById_ReturnById_Success() throws Exception {
        BookDto expected = new BookDto();
        expected.setId(1L);
        expected.setTitle("Test Title 1");
        expected.setAuthor("Test Author 1");
        expected.setIsbn("1234567891");
        expected.setPrice(BigDecimal.valueOf(100L));
        expected.setDescription("Test description 1");
        Long id = 1L;
        Mockito.when(bookService.findById(id)).thenReturn(expected);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/books/{id}", id)
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(actual, expected, "id");
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @DisplayName("Update book by id")
    void updateBookById_ValidRequestDto_Success() throws Exception {
        Long idPassed = 2L;
        CreateBookRequestDto requestDto = createTestCreateBookRequestDto();
        BookDto expected = createTestBookDto(requestDto);
        Mockito.when(bookService.update(idPassed, requestDto)).thenReturn(expected);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/books/" + idPassed)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);

        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void removeBook_ValidId_Success() throws Exception {
        long idPassed = 5L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/books/" + idPassed)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/books/" + idPassed)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    private CreateBookRequestDto createTestCreateBookRequestDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Title 1")
                .setAuthor("Author 1")
                .setPrice(BigDecimal.valueOf(20))
                .setIsbn("12345644891");
        return requestDto;
    }

    private BookDto createTestBookDto(CreateBookRequestDto requestDto) {
        BookDto bookDto = new BookDto()
                .setId(1L)
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setIsbn(requestDto.getIsbn())
                .setPrice(requestDto.getPrice());
        return bookDto;
    }

    private List<BookDto> getBookDtoListMock() {
        List<BookDto> bookDtoList = new ArrayList<>();
        bookDtoList.add(new BookDto()
                .setId(2L)
                .setTitle("Test Title 1")
                .setAuthor("Test Author 1")
                .setIsbn("3456457896")
                .setPrice(BigDecimal.valueOf(100L))
                .setDescription("Test description 1"));
        bookDtoList.add(new BookDto()
                .setId(3L)
                .setTitle("Test Title 2")
                .setAuthor("Test Author 2")
                .setIsbn("3456457896")
                .setPrice(BigDecimal.valueOf(100L))
                .setDescription("Test description 2"));
        return bookDtoList;

    }
}

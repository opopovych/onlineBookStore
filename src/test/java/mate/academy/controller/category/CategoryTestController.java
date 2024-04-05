package mate.academy.controller.category;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.dto.category.CategoryDto;
import mate.academy.dto.category.CreateCategoryRequestDto;
import mate.academy.service.CategoryService;
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
public class CategoryTestController {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CategoryService categoryService;

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
                    new ClassPathResource("database/category/remove-category.sql")
            );
        }
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Save category to DB")
    void createCategory_ValidRequestDto_Success() throws Exception {
        CreateCategoryRequestDto requestDto = createTestCreateCategoryRequestDto();
        CategoryDto expected = createTestCategoryDto(requestDto);
        Mockito.when(categoryService.save(createTestCreateCategoryRequestDto()))
                .thenReturn(expected);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/categories")).andReturn();
        CategoryDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryDto.class);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Get all categories")
    void getAll_AllCategoriesInCatalog_Success() throws Exception {
        List<CategoryDto> expectedCategories = getListCategoryDto();
        Mockito.when(categoryService.findAll()).thenReturn(expectedCategories);
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), CategoryDto[].class);
        Assertions.assertEquals(2, actual.length);
        Assertions.assertEquals(expectedCategories, Arrays.stream(actual).toList());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    @DisplayName("Get category by id")
    void getById_ReturnById_Success() throws Exception {
        Long id = 1L;
        CategoryDto expected = new CategoryDto()
                .setName("New Category")
                .setDescription("Description");
        Mockito.when(categoryService.getById(id)).thenReturn(expected);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/categories/{id}", id)
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryDto.class);
        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(actual, expected, "id");
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @DisplayName("Update category by id")
    void update_ValidRequestDto_Success() throws Exception {
        Long id = 2L;
        CreateCategoryRequestDto requestDto = createTestCreateCategoryRequestDto();
        CategoryDto expected = createTestCategoryDto(requestDto);
        Mockito.when(categoryService.update(id, requestDto)).thenReturn(expected);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/categories/" + id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void deleteById_ValidId_EmptyList() throws Exception {
        long id = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/categories/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    private CreateCategoryRequestDto createTestCreateCategoryRequestDto() {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto()
                .setName("CategoryName")
                .setDescription("CategoryDescription");
        return requestDto;
    }

    private CategoryDto createTestCategoryDto(CreateCategoryRequestDto requestDto) {
        CategoryDto categoryDto = new CategoryDto()
                .setName(requestDto.getName())
                .setDescription(requestDto.getDescription());

        return categoryDto;
    }

    private List<CategoryDto> getListCategoryDto() {
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        categoryDtoList.add(new CategoryDto()
                .setName("First Category")
                .setDescription("First Description"));
        categoryDtoList.add(new CategoryDto()
                .setName("Second Category")
                .setDescription("Second Description"));
        return categoryDtoList;
    }
}

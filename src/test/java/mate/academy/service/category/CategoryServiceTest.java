package mate.academy.service.category;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;
import mate.academy.dto.category.CategoryDto;
import mate.academy.dto.category.CreateCategoryRequestDto;
import mate.academy.mapper.CategoryMapper;
import mate.academy.model.Category;
import mate.academy.repository.category.CategoryRepository;
import mate.academy.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Find category by valid ID - Returns category DTO")
    void findAll_ValidCategories_ReturnAllCategories() {
        CreateCategoryRequestDto requestDto = createCreateCategoryRequestDto();
        Category category = createCategory(requestDto);
        CategoryDto categoryDto = createCategoryDto(category);

        Mockito.when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        Mockito.when(categoryRepository.findAll()).thenReturn(List.of(category));

        assertEquals(List.of(categoryDto), categoryService.findAll());
        Mockito.verify(categoryRepository, Mockito.times(1)).findAll();
        Mockito.verify(categoryMapper, Mockito.times(1)).toDto(category);
    }

    @Test
    @DisplayName("Find category by valid ID - Throws EntityNotFoundException")
    void getById_ValidId_ReturnCategoryById() {
        Long id = 1L;
        CreateCategoryRequestDto requestDto = createCreateCategoryRequestDto();
        Category category = createCategory(requestDto);
        CategoryDto categoryDto = createCategoryDto(category);

        Mockito.when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        Mockito.when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        assertEquals(categoryDto, categoryService.getById(id));
        Mockito.verify(categoryRepository, Mockito.times(1)).findById(id);
        Mockito.verify(categoryMapper, Mockito.times(1)).toDto(category);
    }

    @Test
    @DisplayName("Save new category with valid request - Returns saved category DTO")
    void save_ValidRequest_ReturnCategoryDto() {
        CreateCategoryRequestDto requestDto = createCreateCategoryRequestDto();
        Category category = createCategory(requestDto);
        CategoryDto categoryDto = createCategoryDto(category);

        Mockito.when(categoryRepository.findByName(categoryDto.getName()))
                .thenReturn(Optional.empty());
        Mockito.when(categoryMapper.toEntity(requestDto)).thenReturn(category);
        Mockito.when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        Mockito.when(categoryRepository.save(category)).thenReturn(category);

        assertEquals(categoryDto, categoryService.save(requestDto));
        Mockito.verify(categoryRepository, Mockito.times(1)).findByName(categoryDto.getName());
        Mockito.verify(categoryMapper, Mockito.times(1)).toEntity(requestDto);
        Mockito.verify(categoryMapper, Mockito.times(1)).toDto(category);
        Mockito.verify(categoryRepository, Mockito.times(1)).save(category);
    }

    @Test
    @DisplayName("Update category by valid ID and request")
    void update_ValidRequest_ReturnCategoryDto() {
        CreateCategoryRequestDto requestDto = createCreateCategoryRequestDto();

        CreateCategoryRequestDto updatedDto = createCreateCategoryRequestDto();
        updatedDto.setId(1L);
        updatedDto.setName("Updated Name");
        updatedDto.setDescription("updated description");

        Category updatedCategory = createCategory(updatedDto);
        CategoryDto updatedCategoryDto = createCategoryDto(updatedCategory);

        Mockito.when(categoryMapper.toEntity(updatedDto)).thenReturn(updatedCategory);
        Mockito.when(categoryMapper.toDto(updatedCategory)).thenReturn(updatedCategoryDto);
        Mockito.when(categoryRepository.save(updatedCategory)).thenReturn(updatedCategory);
        Long id = 1L;
        assertEquals(updatedCategoryDto, categoryService.update(id, updatedDto));
    }

    private CreateCategoryRequestDto createCreateCategoryRequestDto() {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setId(1L);
        requestDto.setName("First Category");
        requestDto.setDescription("First description");
        return requestDto;
    }

    private Category createCategory(CreateCategoryRequestDto requestDto) {
        Category category = new Category();
        category.setId(requestDto.getId());
        category.setName(requestDto.getName());
        category.setDescription(requestDto.getDescription());
        return category;
    }

    private CategoryDto createCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());
        return categoryDto;
    }
}

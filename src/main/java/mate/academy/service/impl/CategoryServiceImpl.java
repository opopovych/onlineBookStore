package mate.academy.service.impl;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import mate.academy.dto.category.CategoryDto;
import mate.academy.dto.category.CreateCategoryRequestDto;
import mate.academy.exception.EntityAlreadyPresentException;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.CategoryMapper;
import mate.academy.model.Category;
import mate.academy.repository.category.CategoryRepository;
import mate.academy.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public List findAll() {
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category : categoryRepository.findAll()) {
            categoryDtos.add(categoryMapper.toDto(category));
        }
        return categoryDtos;
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryMapper.toDto(categoryRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Category not found by id: " + id)));
    }

    @SneakyThrows
    @Override
    public CategoryDto save(CreateCategoryRequestDto categoryDto) {
        if (categoryRepository.findByName(categoryDto.getName()).isPresent()) {
            throw new EntityAlreadyPresentException("Category with such name is already exists: "
                    + categoryDto.getName());
        }
        Category category = categoryMapper.toEntity(categoryDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto update(Long id, CreateCategoryRequestDto categoryDto) {
        //getById(id);
        Category category = categoryMapper.toEntity(categoryDto);
        category.setId(id);
        categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}

package mate.academy.dto.category;

import lombok.Data;

@Data
public class CreateCategoryRequestDto {
    private Long id;
    private String name;
    private String description;
}

package mate.academy.dto.book;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class BookDtoWithoutCategoryIds {
    private String title;
    private String author;
    private BigDecimal price;
    private String description;
    private String isbn;
    private String coverImage;

}

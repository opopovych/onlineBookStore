package mate.academy.dto;

public final class BookSearchParametersDto {
    private final String[] titles;
    private final String[] authors;

    public BookSearchParametersDto(String[] titles, String[] authors) {
        this.titles = titles;
        this.authors = authors;
    }

    public String[] titles() {
        return titles;
    }

    public String[] authors() {
        return authors;
    }
}


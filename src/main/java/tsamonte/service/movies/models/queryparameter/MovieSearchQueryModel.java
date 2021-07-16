package tsamonte.service.movies.models.queryparameter;

/**
 * Query models will hold information about any accepted query parameters. There are no required query parameters.
 * For fields with limited valid choices, any invalid data passed will cause the default data to be used instead
 *
 * The class MovieSearchQueryModel will be utilized by the following endpoints:
 *      - /api/movies/search
 *
 * Request Query Fields:
 *      - title (String, optional)
 *      - year (int, optional)
 *      - director (String, optional)
 *      - genre (String, optional)
 *      - hidden (boolean, optional)
 *      - limit (int ,optional): number or results displayed; 10 (default), 25, 50, or 100
 *      - offset (int, optional): for result pagination; 0 (default) or positive multiple of limit
 *      - orderBy (String, optional): sorting parameter; "title" (default), "rating", or "year"
 *      - direction (String, optional): sorting direction; "asc" (default) or "desc"
 */
public class MovieSearchQueryModel extends CommonQueryParameters{
    private String title;
    private Integer year;
    private String director;
    private String genre;
    private Boolean hidden;

    public MovieSearchQueryModel(String title, Integer year, String director, String genre, Boolean hidden,
                                 Integer limit, Integer offset, String orderBy, String direction) {
        super(limit, offset, orderBy, direction);
        this.title = title;
        this.year = year;
        this.director = director;
        this.genre = genre;
        this.hidden = hidden;
    }

    public String getTitle() { return title; }

    public Integer getYear() { return year; }

    public String getDirector() { return director; }

    public String getGenre() { return genre; }

    public Boolean showHidden() { return hidden; }
}

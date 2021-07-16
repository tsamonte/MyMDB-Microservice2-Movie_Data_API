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
public class MovieSearchQueryModel {
    private String title;
    private Integer year;
    private String director;
    private String genre;
    private Boolean hidden;
    private Integer limit = 10; // can be 10, 25, 50, or 100
    private Integer offset = 0; // can be 0 or positive multiple of limit
    private String orderBy = "title"; // can be "title", "rating", or "year"
    private String direction = "asc"; // can be "asc" or "desc"

    public MovieSearchQueryModel(String title, Integer year, String director, String genre, Boolean hidden,
                                 Integer limit, Integer offset, String orderBy, String direction) {
        this.title = title;
        this.year = year;
        this.director = director;
        this.genre = genre;
        this.hidden = hidden;
        if(limit != null && limitIsValid(limit)) this.limit = limit;
        if(offset != null && offsetIsValid(offset)) this.offset = offset;
        if(orderBy != null && orderByIsValid(orderBy)) this.orderBy = orderBy;
        if(direction != null && directionIsValid(direction)) this.direction = direction;
    }

    public String getTitle() { return title; }

    public Integer getYear() { return year; }

    public String getDirector() { return director; }

    public String getGenre() { return genre; }

    public Boolean showHidden() { return hidden; }

    public Integer getLimit() { return limit; }

    public Integer getOffset() { return offset; }

    public String getOrderBy() { return orderBy; }

    public String getDirection() { return direction; }

    /**
     * The following four functions check if their respective field is valid
     *
     * @return true if data is valid, false if not
     */
    private boolean limitIsValid(int passedLimit) {
        return passedLimit == 10 || passedLimit == 25 || passedLimit == 50 || passedLimit == 100;
    }

    private boolean offsetIsValid(int passedOffset) {
        return passedOffset == 0 || passedOffset % limit == 0;
    }

    private boolean orderByIsValid(String passedOrderBy) {
        passedOrderBy.toLowerCase();
        return passedOrderBy.equalsIgnoreCase("title") || passedOrderBy.equalsIgnoreCase("rating") || passedOrderBy.equalsIgnoreCase("year");
    }

    private boolean directionIsValid(String passedDirection) {
        return passedDirection.equalsIgnoreCase("asc") || passedDirection.equalsIgnoreCase("desc");
    }

    @Override
    public String toString() {
        return "MovieSearchQueryModel{" +
                "title='" + title + '\'' +
                ", year=" + year +
                ", director='" + director + '\'' +
                ", genre='" + genre + '\'' +
                ", hidden=" + hidden +
                ", limit=" + limit +
                ", offset=" + offset +
                ", orderBy='" + orderBy + '\'' +
                ", direction='" + direction + '\'' +
                '}';
    }
}

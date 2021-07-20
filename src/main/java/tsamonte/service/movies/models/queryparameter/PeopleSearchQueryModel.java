package tsamonte.service.movies.models.queryparameter;

/**
 * Query models will hold information about any accepted query parameters. There are no required query parameters.
 * For fields with limited valid choices, any invalid data passed will cause the default data to be used instead
 *
 * The class PeopleSearchQueryModel will be utilized by the following endpoints:
 *      - /api/movies/people/search
 *
 * Request Query Fields:
 *      - name (String, optional)
 *      - birthday (String, optional)
 *      - movie_title (String, optional): movie person is in
 *      - limit (int ,optional): number or results displayed; 10 (default), 25, 50, or 100
 *      - offset (int, optional): for result pagination; 0 (default) or positive multiple of limit
 *      - orderBy (String, optional): sorting parameter; "title" (default), "rating", or "year"
 *      - direction (String, optional): sorting direction; "asc" (default) or "desc"
 */
public class PeopleSearchQueryModel extends CommonQueryParameters {
    private String name;
    private String birthday;
    private String movie_title;

    public PeopleSearchQueryModel(String name, String birthday, String movie_title,
                                  Integer limit, Integer offset, String orderBy, String direction) {
        super(limit, offset, orderBy, direction);
        this.name = name;
        this.birthday = birthday;
        this.movie_title = movie_title;
    }

    public String getName() { return name; }

    public String getBirthday() { return birthday; }

    public String getMovie_title() { return movie_title; }
}

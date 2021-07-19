package tsamonte.service.movies.models.queryparameter;

/**
 * Query models will hold information about any accepted query parameters.
 * For fields with limited valid choices, any invalid data passed will cause the default data to be used instead
 *
 * The class PeopleQueryModel will be utilized by the following endpoints:
 *      - /api/movies/people
 *
 * Request Query Fields:
 *      - name (String, required)
 *      - limit (int ,optional): number or results displayed; 10 (default), 25, 50, or 100
 *      - offset (int, optional): for result pagination; 0 (default) or positive multiple of limit
 *      - orderBy (String, optional): sorting parameter; "title" (default), "rating", or "year"
 *      - direction (String, optional): sorting direction; "asc" (default) or "desc"
 */
public class PeopleQueryModel extends CommonQueryParameters {
    private String name;
    private Boolean hidden; // recall: if user has insufficient privilege, hidden movies should not be shown. Keep track of this here.

    public PeopleQueryModel(String name,
                            Integer limit, Integer offset, String orderBy, String direction,
                            Boolean hidden) {
        super(limit, offset, orderBy, direction);
        this.name = name;
        this.hidden = hidden;
    }

    public String getName() { return name; }

    public Boolean showHidden() { return hidden; }
}

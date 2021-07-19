package tsamonte.service.movies.models.queryparameter;

import java.util.ArrayList;

/**
 * Query models will hold information about any accepted query parameters. There are no required query parameters.
 * For fields with limited valid choices, any invalid data passed will cause the default data to be used instead
 *
 * The class MovieBrowseQueryModel will be utilized by the following endpoints:
 *      - /api/movies/browse/{phrase}
 *
 * Path Parameter Fields:
 *      - phrase (string, required): comma-delimited phrase containing keywords to search on.
 *
 * Request Query Fields:
 *      - limit (int ,optional): number or results displayed; 10 (default), 25, 50, or 100
 *      - offset (int, optional): for result pagination; 0 (default) or positive multiple of limit
 *      - orderBy (String, optional): sorting parameter; "title" (default), "rating", or "year"
 *      - direction (String, optional): sorting direction; "asc" (default) or "desc"
 */
public class MovieBrowseQueryModel extends CommonQueryParameters {
    private ArrayList<String> keywords;
    private Boolean hidden; // recall: if user has insufficient privilege, hidden movies should not be shown. Keep track of this here.

    public MovieBrowseQueryModel(ArrayList<String> keywords,
                                 Integer limit, Integer offset, String orderBy, String direction,
                                 Boolean hidden) {
        super(limit, offset, orderBy, direction);
        this.keywords = keywords;
        this.hidden = hidden;
    }

    public ArrayList<String> getKeywords() { return keywords; }

    public Boolean showHidden() { return hidden; }
}

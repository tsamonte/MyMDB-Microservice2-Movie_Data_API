package tsamonte.service.movies.models.queryparameter;

/**
 * Query models will hold information about any accepted query parameters. There are no required query parameters.
 * For fields with limited valid choices, any invalid data passed will cause the default data to be used instead.
 *
 * The query parameters in this base class are common among all searches that require query models
 *
 * The class CommonQueryParameters will be utilized by the following endpoints:
 *      - /api/movies/search
 *      - /api/movies/browse/{phrase}
 *      - /api/movies/people
 *      - /api/movies/people/search
 *
 * Request Query Fields:
 *      - limit (int ,optional): number or results displayed; 10 (default), 25, 50, or 100
 *      - offset (int, optional): for result pagination; 0 (default) or positive multiple of limit
 *      - orderBy (String, optional): sorting parameter; "title" (default), "rating", or "year"
 *      - direction (String, optional): sorting direction; "asc" (default) or "desc"
 */
public class CommonQueryParameters {
    private Integer limit = 10; // can be 10, 25, 50, or 100
    private Integer offset = 0; // can be 0 or positive multiple of limit
    private String orderBy = "title"; // can be "title", "rating", or "year"
    private String direction = "asc"; // can be "asc" or "desc"

    public CommonQueryParameters(Integer limit, Integer offset, String orderBy, String direction) {
        if(limit != null && limitIsValid(limit)) this.limit = limit;
        if(offset != null && offsetIsValid(offset)) this.offset = offset;
        if(orderBy != null && orderByIsValid(orderBy)) this.orderBy = orderBy;
        if(direction != null && directionIsValid(direction)) this.direction = direction;
    }

    public Integer getLimit() { return limit; }

    public Integer getOffset() { return offset; }

    public String getOrderBy() { return orderBy; }

    public String getDirection() { return direction; }

    public void setOrderBy(String orderBy) { this.orderBy = orderBy; }

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
        return passedOrderBy.equalsIgnoreCase("title") || passedOrderBy.equalsIgnoreCase("rating") || passedOrderBy.equalsIgnoreCase("year");
    }

    private boolean directionIsValid(String passedDirection) {
        return passedDirection.equalsIgnoreCase("asc") || passedDirection.equalsIgnoreCase("desc");
    }
}

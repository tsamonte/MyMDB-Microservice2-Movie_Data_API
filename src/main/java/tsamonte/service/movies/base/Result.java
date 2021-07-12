package tsamonte.service.movies.base;

import javax.ws.rs.core.Response;

/**
 * Enum Result encapsulates an API response's result code, message, and HTTP Status Code
 */
public enum Result {
    // Common error results
    JSON_PARSE_EXCEPTION (-3, "JSON Parse Exception.", Response.Status.BAD_REQUEST),
    JSON_MAPPING_EXCEPTION (-2, "JSON Mapping Exception.", Response.Status.BAD_REQUEST),
    INTERNAL_SERVER_ERROR (-1, "Internal Server Error.", Response.Status.INTERNAL_SERVER_ERROR),

    // Service-specific success results
    MOVIES_FOUND (210, "Found movie(s) with search parameters.", Response.Status.OK),
    NO_MOVIES_FOUND (211, "No movies found with search parameters.", Response.Status.OK),
    PEOPLE_FOUND (212, "Found people with search parameters.", Response.Status.OK),
    NO_PEOPLE_FOUND (213, "No people found with search parameters.", Response.Status.OK);

    private final int resultCode;
    private final String message;
    private final Response.Status status;

    Result(int resultCode, String message, Response.Status status) {
        this.resultCode = resultCode;
        this.message = message;
        this.status = status;
    }

    public int getResultCode() { return resultCode; }

    public String getMessage() { return message; }

    public Response.Status getStatus() { return status; }
}

package tsamonte.service.movies.models.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The class ThumbnailRequestModel will be utilized by the following endpoints:
 *      - /api/movies/thumbnail
 *
 * Request Model:
 *      - movie_ids(String[], required)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ThumbnailRequestModel {
    @JsonProperty(value = "movie_ids", required = true)
    private String[] movie_ids;

    @JsonCreator

    public ThumbnailRequestModel(@JsonProperty(value = "movie_ids", required = true) String[] movie_ids) {
        this.movie_ids = movie_ids;
    }

    @JsonProperty(value = "movie_ids")
    public String[] getMovie_ids() {
        return movie_ids;
    }
}

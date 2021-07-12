package tsamonte.service.movies.models.response.movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import tsamonte.service.movies.base.Result;
import tsamonte.service.movies.database.model.movie.SearchBrowseModel;
import tsamonte.service.movies.models.response.BaseResponseModel;

/**
 * The class SearchBrowseResponseModel will be utilized by the following endpoints:
 *      - /api/movies/search
 *      - /api/movies/browse
 *      - /api/movies/people
 *
 * Response Model:
 *      - resultCode (int, required)
 *      - message (String, required)
 *      - movies (SearchBrowseModel[], required)
 *          - movie_id (String, required)
 *          - title (String, required)
 *          - year (int, required)
 *          - director (String, required)
 *          - rating (float, required)
 *          - backdrop_path (String, optional)
 *          - poster_path (String, optional)
 *          - hidden (boolean, optional)
 */
public class SearchBrowseResponseModel extends BaseResponseModel {
    @JsonProperty(value = "movies", required = true)
    private SearchBrowseModel[] movies;

    @JsonCreator
    public SearchBrowseResponseModel(Result result,
                                     @JsonProperty(value = "movies", required = true) SearchBrowseModel[] movies) {
        super(result);
        this.movies = movies;
    }

    @JsonProperty(value = "movies")
    public SearchBrowseModel[] getMovies() { return movies; }
}

package tsamonte.service.movies.models.response.movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import tsamonte.service.movies.base.Result;
import tsamonte.service.movies.database.model.movie.MovieGetModel;
import tsamonte.service.movies.models.response.BaseResponseModel;

/**
 * The class MovieGetResponseModel will be utilized by the following endpoints:
 *      - /api/movies/get/{movie_id}
 *
 * Response Model:
 *      - resultCode (int, required)
 *      - message (String, required)
 *      - movies (MovieGetModel[], required)
 *          - movie_id (String, required)
 *          - title (String, required)
 *          - year (int, required)
 *          - director (String, required)
 *          - rating (float, required)
 *          - num_votes (int, required)
 *          - budget (String, optional)
 *          - revenue (String, optional)
 *          - overview (String, optional)
 *          - backdrop_path (String, optional)
 *          - poster_path (String, optional)
 *          - hidden (boolean, optional)
 *      - genres (GenreModel[], required)
 *          - genre_id (int, required)
 *          - name (String, required)
 *      - people (PersonNameModel[], required)
 *          - person_id (int, required)
 *          - name (String, required)
 */
public class MovieGetResponseModel extends BaseResponseModel {
    @JsonProperty(value = "movie", required = true)
    private MovieGetModel movie;

    @JsonCreator
    public MovieGetResponseModel(Result result,
                                 @JsonProperty(value = "movie", required = true) MovieGetModel movie) {
        super(result);
        this.movie = movie;
    }

    @JsonProperty(value = "movie")
    public MovieGetModel getMovie() {
        return movie;
    }
}

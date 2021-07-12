package tsamonte.service.movies.database.model.movie;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class SearchBrowseModel models a portion of the movie table in the database.
 * This class will be used when retrieving a movie's movie id, title, year, director, rating, backdrop path, poster path, and hidden value.
 * All of these fields will be necessary when searching or browsing for movies.
 *
 * Relevant endpoints:
 *  - /api/movies/search
 *  - /api/movies/browse/{phrase}
 *  - /api/movies/people
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchBrowseModel {
    @JsonProperty(value = "movie_id", required = true)
    private String movie_id;

    @JsonProperty(value = "title", required = true)
    private String title;

    @JsonProperty(value = "year", required = true)
    private int year;

    @JsonProperty(value = "director", required = true)
    private String director;

    @JsonProperty(value = "rating", required = true)
    private float rating;

    @JsonProperty(value = "backdrop_path")
    private String backdrop_path;

    @JsonProperty(value = "poster_path")
    private String poster_path;

    @JsonProperty(value = "hidden")
    private Boolean hidden;

    @JsonCreator
    public SearchBrowseModel(@JsonProperty(value = "movie_id", required = true) String movie_id,
                             @JsonProperty(value = "title", required = true) String title,
                             @JsonProperty(value = "year", required = true) int year,
                             @JsonProperty(value = "director", required = true) String director,
                             @JsonProperty(value = "rating", required = true) float rating,
                             @JsonProperty(value = "backdrop_path") String backdrop_path,
                             @JsonProperty(value = "poster_path") String poster_path,
                             @JsonProperty(value = "hidden") Boolean hidden) {
        this.movie_id = movie_id;
        this.title = title;
        this.year = year;
        this.director = director;
        this.rating = rating;
        this.backdrop_path = backdrop_path;
        this.poster_path = poster_path;
        this.hidden = hidden;
    }

    /**
     * Instead of taking fields directly, this constructor will take an entire MovieModel object, which models
     * the movie table of the database. This constructor will extract the necessary search/browse response fields
     *
     * @param movieModel Object modeling an entire row of the movie table in the database
     */
    public SearchBrowseModel(MovieModel movieModel) {
        this.movie_id = movieModel.getMovie_id();
        this.title = movieModel.getTitle();
        this.year = movieModel.getYear();
        this.director = movieModel.getDirector();
        this.rating = movieModel.getRating();
        this.backdrop_path = movieModel.getPoster_path();
        this.poster_path = movieModel.getPoster_path();
        this.hidden = movieModel.getHidden();
    }

    @JsonProperty(value = "movie_id")
    public String getMovie_id() {
        return movie_id;
    }

    @JsonProperty(value = "title")
    public String getTitle() {
        return title;
    }

    @JsonProperty(value = "year")
    public int getYear() {
        return year;
    }

    @JsonProperty(value = "director")
    public String getDirector() {
        return director;
    }

    @JsonProperty(value = "rating")
    public float getRating() {
        return rating;
    }

    @JsonProperty(value = "backdrop_path")
    public String getBackdrop_path() {
        return backdrop_path;
    }

    @JsonProperty(value = "poster_path")
    public String getPoster_path() {
        return poster_path;
    }

    @JsonProperty(value = "hidden")
    public Boolean isHidden() {
        return hidden;
    }
}

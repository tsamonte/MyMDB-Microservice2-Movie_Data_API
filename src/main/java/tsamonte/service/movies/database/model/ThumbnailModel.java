package tsamonte.service.movies.database.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class ThumbnailModel models a portion of the movie table in the database.
 * This class will be used when retrieving a movie's movie id, title, backdrop path, and poster path.
 * All of these fields will be necessary for retrieving thumbnails only.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ThumbnailModel {
    @JsonProperty(value = "movie_id", required = true)
    private String movie_id;

    @JsonProperty(value = "title", required = true)
    private String title;

    @JsonProperty(value = "backdrop_path")
    private String backdrop_path;

    @JsonProperty(value = "poster_path")
    private String poster_path;

    @JsonCreator
    public ThumbnailModel(@JsonProperty(value = "movie_id", required = true) String movie_id,
                          @JsonProperty(value = "title", required = true) String title,
                          @JsonProperty(value = "backdrop_path") String backdrop_path,
                          @JsonProperty(value = "poster_path") String poster_path) {
        this.movie_id = movie_id;
        this.title = title;
        this.backdrop_path = backdrop_path;
        this.poster_path = poster_path;
    }

    // TODO: add constructor that accepts a full MovieModel object that models the movie table in the db
//    public ThumbnailModel(MovieModel MovieModel) {}

    @JsonProperty(value = "movie_id")
    public String getMovie_id() {
        return movie_id;
    }

    @JsonProperty(value = "title")
    public String getTitle() {
        return title;
    }

    @JsonProperty(value = "backdrop_path")
    public String getBackdrop_path() {
        return backdrop_path;
    }

    @JsonProperty(value = "poster_path")
    public String getPoster_path() {
        return poster_path;
    }
}

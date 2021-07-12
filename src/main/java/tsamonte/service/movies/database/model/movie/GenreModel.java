package tsamonte.service.movies.database.model.movie;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class GenreModel models the structure of the genre table in the database.
 * This class will be used when retrieving entire rows from the database.
 *
 * Relevant endpoints:
 *  - /api/movies/get/{movie_id}
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenreModel {
    @JsonProperty(value = "genre_id", required = true)
    private int genre_id;

    @JsonProperty(value = "name", required = true)
    private String name;

    @JsonCreator
    public GenreModel(@JsonProperty(value = "genre_id", required = true) int genre_id,
                      @JsonProperty(value = "name", required = true) String name) {
        this.genre_id = genre_id;
        this.name = name;
    }

    @JsonProperty(value = "genre_id")
    public int getGenre_id() {
        return genre_id;
    }

    @JsonProperty(value = "name")
    public String getName() {
        return name;
    }
}

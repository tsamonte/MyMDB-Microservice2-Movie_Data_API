package tsamonte.service.movies.database.model.movie;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class PersonNameModel models a portion of the person table in the database.
 * This class will be used when retrieving a person's person_id and name.
 * These fields will be necessary when retrieving a movie for the user.
 *
 * Relevant endpoints:
 *  - /api/movies/get/{movie_id}
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonNameModel {
    @JsonProperty(value = "person_id", required = true)
    private int person_id;

    @JsonProperty(value = "name", required = true)
    private String name;

    @JsonCreator
    public PersonNameModel(@JsonProperty(value = "person_id", required = true) int person_id,
                       @JsonProperty(value = "name", required = true) String name) {
        this.person_id = person_id;
        this.name = name;
    }

    @JsonProperty(value = "person_id")
    public int getPerson_id() {
        return person_id;
    }

    @JsonProperty(value = "name")
    public String getName() {
        return name;
    }
}

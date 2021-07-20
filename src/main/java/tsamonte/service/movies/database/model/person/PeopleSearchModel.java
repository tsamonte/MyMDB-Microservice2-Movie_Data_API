package tsamonte.service.movies.database.model.person;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class PeopleSearchModel models a portion of the person table in the database.
 * This class will be used when retrieving a person's person id, name, birthday, popularity, and profile path.
 * All of these fields will be necessary when searching or browsing for people.
 *
 * Relevant endpoints:
 *  - /api/movies/people/search
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PeopleSearchModel {
    @JsonProperty(value = "person_id", required = true)
    private int person_id;

    @JsonProperty(value = "name", required = true)
    private String name;

    @JsonProperty(value = "birthday")
    private String birthday;

    @JsonProperty(value = "popularity")
    private Float popularity;

    @JsonProperty(value = "profile_path")
    private String profile_path;

    @JsonCreator
    public PeopleSearchModel(@JsonProperty(value = "person_id", required = true) int person_id,
                             @JsonProperty(value = "name", required = true) String name,
                             @JsonProperty(value = "birthday") String birthday,
                             @JsonProperty(value = "popularity") Float popularity,
                             @JsonProperty(value = "profile_path") String profile_path) {
        this.person_id = person_id;
        this.name = name;
        this.birthday = birthday;
        this.popularity = popularity;
        this.profile_path = profile_path;
    }

    /**
     * Instead of taking fields directly, this constructor will take an entire PersonModel object, which models
     * the person table of the database. This constructor will extract the necessary response fields
     *
     * @param personModel Object modeling an entire row of the movie table in the database
     */
    public PeopleSearchModel(PersonModel personModel) {
        this.person_id = personModel.getPerson_id();
        this.name = personModel.getName();
        this.birthday = personModel.getBirthday();
        this.popularity = personModel.getPopularity();
        this.profile_path = personModel.getProfile_path();
    }

    @JsonProperty(value = "person_id")
    public int getPerson_id() {
        return person_id;
    }

    @JsonProperty(value = "name")
    public String getName() {
        return name;
    }

    @JsonProperty(value = "birthday")
    public String getBirthday() { return birthday; }

    @JsonProperty(value = "popularity")
    public Float getPopularity() { return popularity; }

    @JsonProperty(value = "profile_path")
    public String getProfile_path() { return profile_path; }
}

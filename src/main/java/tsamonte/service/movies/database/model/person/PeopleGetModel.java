package tsamonte.service.movies.database.model.person;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class PeopleGetModel models the structure of the people table in the database, except all foreign keys are replaced with the relevant
 * information from its respective table.
 * All of these fields will be necessary when retrieving a person's details.
 *
 * Relevant endpoints:
 *  - /api/movies/people/get/{person_id}
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PeopleGetModel extends PeopleSearchModel {
    @JsonProperty(value = "gender")
    private String gender;

    @JsonProperty(value = "deathday")
    private String deathday;

    @JsonProperty(value = "biography")
    private String biography;

    @JsonProperty(value = "birthplace")
    private String birthplace;

    @JsonCreator
    public PeopleGetModel(@JsonProperty(value = "person_id", required = true) int person_id,
                          @JsonProperty(value = "name", required = true) String name,
                          @JsonProperty(value = "gender") String gender,
                          @JsonProperty(value = "birthday") String birthday,
                          @JsonProperty(value = "deathday") String deathday,
                          @JsonProperty(value = "biography") String biography,
                          @JsonProperty(value = "birthplace") String birthplace,
                          @JsonProperty(value = "popularity") Float popularity,
                          @JsonProperty(value = "profile_path") String profile_path) {
        super(person_id, name, birthday, popularity, profile_path);
        this.gender = gender;
        this.deathday = deathday;
        this.biography = biography;
        this.birthplace = birthplace;
    }

    /**
     * Instead of taking fields directly, this constructor will take an entire PersonModel object, which models
     * the person table of the database. This constructor will extract the necessary response fields
     *
     * @param personModel Object modeling an entire row of the movie table in the database
     */
    public PeopleGetModel(PersonModel personModel) {
        super(personModel.getPerson_id(), personModel.getName(), personModel.getBirthday(), personModel.getPopularity(), personModel.getProfile_path());
        this.gender = personModel.getGender();
        this.deathday = personModel.getDeathday();
        this.biography = personModel.getBiography();
        this.birthplace = personModel.getBirthplace();
    }

    @JsonProperty(value = "gender")
    public String getGender() { return gender; }

    @JsonProperty(value = "deathday")
    public String getDeathday() { return deathday; }

    @JsonProperty(value = "biography")
    public String getBiography() { return biography; }

    @JsonProperty(value = "birthplace")
    public String getBirthplace() { return birthplace; }
}

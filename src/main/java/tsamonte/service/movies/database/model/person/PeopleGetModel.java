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
public class PeopleGetModel {
    @JsonProperty(value = "person_id", required = true)
    private int person_id;

    @JsonProperty(value = "name", required = true)
    private String name;

    @JsonProperty(value = "gender")
    private String gender;

    @JsonProperty(value = "birthday")
    private String birthday;

    @JsonProperty(value = "deathday")
    private String deathday;

    @JsonProperty(value = "biography")
    private String biography;

    @JsonProperty(value = "birthplace")
    private String birthplace;

    @JsonProperty(value = "popularity")
    private Float popularity;

    @JsonProperty(value = "profile_path")
    private String profile_path;

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
        this.person_id = person_id;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.deathday = deathday;
        this.biography = biography;
        this.birthplace = birthplace;
        this.popularity = popularity;
        this.profile_path = profile_path;
    }

    /**
     * Instead of taking fields directly, this constructor will take an entire PersonModel object, which models
     * the person table of the database. This constructor will extract the necessary response fields
     *
     * @param personModel Object modeling an entire row of the movie table in the database
     */
    public PeopleGetModel(PersonModel personModel) {
        this.person_id = personModel.getPerson_id();
        this.name = personModel.getName();
        this.gender = personModel.getGender();
        this.birthday = personModel.getBirthday();
        this.deathday = personModel.getDeathday();
        this.biography = personModel.getBiography();
        this.birthplace = personModel.getBirthplace();
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

    @JsonProperty(value = "gender")
    public String getGender() { return gender; }

    @JsonProperty(value = "birthday")
    public String getBirthday() { return birthday; }

    @JsonProperty(value = "deathday")
    public String getDeathday() { return deathday; }

    @JsonProperty(value = "biography")
    public String getBiography() { return biography; }

    @JsonProperty(value = "birthplace")
    public String getBirthplace() { return birthplace; }

    @JsonProperty(value = "popularity")
    public Float getPopularity() { return popularity; }

    @JsonProperty(value = "profile_path")
    public String getProfile_path() { return profile_path; }
}

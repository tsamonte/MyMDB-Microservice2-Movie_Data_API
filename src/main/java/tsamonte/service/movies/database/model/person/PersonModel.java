package tsamonte.service.movies.database.model.person;

import tsamonte.service.movies.database.access.GenderRecords;

/**
 * Class PersonModel models the structure of the person table in the database.
 * This class will be used when retrieving entire rows from the database.
 */
public class PersonModel {
    // the following values will be non-null
    private int person_id;
    private String name;

    // the following values can be null
    private Integer gender_id;
    private String birthday;
    private String deathday;
    private String biography;
    private String birthplace;
    private Float popularity;
    private String profile_path;

    public PersonModel(int person_id, String name, Integer gender_id,
                       String birthday, String deathday, String biography,
                       String birthplace, Float popularity, String profile_path) {
        this.person_id = person_id;
        this.name = name;
        this.gender_id = gender_id;
        this.birthday = birthday;
        this.deathday = deathday;
        this.biography = biography;
        this.birthplace = birthplace;
        this.popularity = popularity;
        this.profile_path = profile_path;
    }

    public int getPerson_id() {
        return person_id;
    }

    public String getName() {
        return name;
    }

    public Integer getGender_id() {
        return gender_id;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getDeathday() {
        return deathday;
    }

    public String getBiography() {
        return biography;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public Float getPopularity() {
        return popularity;
    }

    public String getProfile_path() {
        return profile_path;
    }

    /**
     * Retrieves the director's name based on this MovieModel's director_id
     * @return director's name retrieved from database
     */
    public String getGender() {
        return GenderRecords.retrieve(this.gender_id);
    }
}

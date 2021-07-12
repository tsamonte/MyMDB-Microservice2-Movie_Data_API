package tsamonte.service.movies.models.response.people;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import tsamonte.service.movies.base.Result;
import tsamonte.service.movies.database.model.person.PeopleGetModel;
import tsamonte.service.movies.models.response.BaseResponseModel;

/**
 * The class PeopleGetResponseModel will be utilized by the following endpoints:
 *      - /api/movies/people/get/{person_id}
 *
 * Response Model:
 *      - resultCode (int, required)
 *      - message (String, required)
 *      - Person (PeopleGetModel[], required)
 *          - person_id (int, required)
 *          - name (String, required)
 *          - gender (String, optional)
 *          - birthday (String, optional)
 *          = deathday (String, optional)
 *          - biography (String, optional)
 *          - birthplace (String, optional)
 *          - popularity (float, optional)
 *          - profile_path (String, optional)
 */
public class PeopleGetResponseModel extends BaseResponseModel {
    @JsonProperty(value = "person", required = true)
    private PeopleGetModel person;

    @JsonCreator
    public PeopleGetResponseModel(Result result,
                                  @JsonProperty(value = "person", required = true) PeopleGetModel person) {
        super(result);
        this.person = person;
    }

    @JsonProperty(value = "person")
    public PeopleGetModel getPerson() {
        return person;
    }
}

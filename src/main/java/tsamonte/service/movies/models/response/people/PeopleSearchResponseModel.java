package tsamonte.service.movies.models.response.people;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import tsamonte.service.movies.base.Result;
import tsamonte.service.movies.database.model.person.PeopleSearchModel;
import tsamonte.service.movies.models.response.BaseResponseModel;

/**
 * The class PeopleSearchResponseModel will be utilized by the following endpoints:
 *      - /api/movies/people/search
 *
 * Response Model:
 *      - resultCode (int, required)
 *      - message (String, required)
 *      - people (PeopleSearchModel[], required)
 *          - person_id (int, required)
 *          - name (String, required)
 *          - birthday (String, optional)
 *          - popularity (float, optional)
 *          - profile_path (String, optional)
 */
public class PeopleSearchResponseModel extends BaseResponseModel {
    @JsonProperty(value = "people", required = true)
    private PeopleSearchModel[] people;

    @JsonCreator
    public PeopleSearchResponseModel(Result result,
                                     @JsonProperty(value = "people", required = true) PeopleSearchModel[] people) {
        super(result);
        this.people = people;
    }

    @JsonProperty(value = "people")
    public PeopleSearchModel[] getPeople() {
        return people;
    }
}

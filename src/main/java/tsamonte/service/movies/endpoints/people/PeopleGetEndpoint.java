package tsamonte.service.movies.endpoints.people;

import tsamonte.service.movies.base.Result;
import tsamonte.service.movies.database.access.PersonRecords;
import tsamonte.service.movies.database.model.person.PeopleGetModel;
import tsamonte.service.movies.logger.ServiceLogger;
import tsamonte.service.movies.models.response.people.PeopleGetResponseModel;
import tsamonte.service.movies.models.response.people.PeopleSearchResponseModel;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;

/**
 * GET /api/movies/people/get/{person_id}
 *
 * Request Header Fields:
 *      - email
 *      - session_id
 *      - transaction_id
 *
 * Path Parameter Fields:
 *      - person_id (int, required):
 *
 * Response Model:
 *      - resultCode (int, required)
 *      - message (String, required)
 *      - people (PeopleSearchModel[], required)
 *          - person_id (int, required)
 *          - name (String, required)
 *          - gender (String, optional)
 *          - birthday (String, optional)
 *          - deathday (String, optional)
 *          - biography (String, optional)
 *          - birthplace (String, optional)
 *          - popularity (float, optional)
 *          - profile_path (String, optional)
 */
@Path("people")
public class PeopleGetEndpoint {
    @Path("/get/{person_id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response peopleGetResponse(@Context HttpHeaders headers, @PathParam("person_id") int person_id) {
        PeopleGetResponseModel responseModel;
        try {
            PeopleGetModel result = PersonRecords.retrieveAllPersonData(person_id);

            // resultCode = 212; 200 OK; "Found people with search parameters."
            if(result != null) {
                responseModel = new PeopleGetResponseModel(Result.PEOPLE_FOUND, result);
            }
            // resultCode = 213; 200 OK; "No people found with search parameters."
            else {
                responseModel = new PeopleGetResponseModel(Result.NO_PEOPLE_FOUND, null);
            }

            // headers to pass into response
            HashMap<String,String> headerMap = new HashMap<String,String>();
            headerMap.put("email", headers.getHeaderString("email"));
            headerMap.put("session_id", headers.getHeaderString("session_id"));
            headerMap.put("transaction_id", headers.getHeaderString("transaction_id"));

            ServiceLogger.LOGGER.info(responseModel.getMessage());
            return responseModel.buildResponse(headerMap);
        }
        catch (Exception e) {
            e.printStackTrace();
            responseModel = new PeopleGetResponseModel(Result.INTERNAL_SERVER_ERROR, null);
            return responseModel.buildResponse();
        }
    }
}

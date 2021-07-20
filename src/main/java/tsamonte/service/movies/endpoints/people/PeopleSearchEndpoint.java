package tsamonte.service.movies.endpoints.people;

import tsamonte.service.movies.base.Result;
import tsamonte.service.movies.database.access.PersonRecords;
import tsamonte.service.movies.database.model.person.PeopleSearchModel;
import tsamonte.service.movies.logger.ServiceLogger;
import tsamonte.service.movies.models.queryparameter.PeopleSearchQueryModel;
import tsamonte.service.movies.models.response.people.PeopleSearchResponseModel;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;

/**
 * GET /api/movies/people/search
 *
 * Request Header Fields:
 *      - email
 *      - session_id
 *      - transaction_id
 *
 * Request Query Fields:
 *      - name (String, optional)
 *      - birthday (String, optional)
 *      - movie_title (String, optional)
 *      - limit (int ,optional): number or results displayed; 10 (default), 25, 50, or 100
 *      - offset (int, optional): for result pagination; 0 (default) or positive multiple of limit
 *      - orderBy (String, optional): sorting parameter; "name" (default), "birthday", or "popularity"
 *      - direction (String, optional): sorting direction; "asc" (default) or "desc"
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
@Path("people")
public class PeopleSearchEndpoint {
    @Path("search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response peopleSearchResponse(@Context HttpHeaders headers, @QueryParam("name") String name,
                                         @QueryParam("birthday") String birthday, @QueryParam("movie_title") String movie_title,
                                         @QueryParam("limit") Integer limit, @QueryParam("offset") Integer offset,
                                         @QueryParam("orderBy") String orderBy, @QueryParam("direction") String direction) {
        PeopleSearchResponseModel responseModel;
        try {
            PeopleSearchQueryModel queryModel = new PeopleSearchQueryModel(name, birthday, movie_title, limit, offset, orderBy, direction);
            PeopleSearchModel[] results = PersonRecords.retrieve(queryModel);

            // resultCode = 212; 200 OK; "Found people with search parameters."
            if(results != null && results.length > 0) {
                responseModel = new PeopleSearchResponseModel(Result.PEOPLE_FOUND, results);
            }
            // resultCode = 213; 200 OK; "No people found with search parameters."
            else {
                responseModel = new PeopleSearchResponseModel(Result.NO_PEOPLE_FOUND, results);
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
            responseModel = new PeopleSearchResponseModel(Result.INTERNAL_SERVER_ERROR, null);
            return responseModel.buildResponse();
        }
    }
}

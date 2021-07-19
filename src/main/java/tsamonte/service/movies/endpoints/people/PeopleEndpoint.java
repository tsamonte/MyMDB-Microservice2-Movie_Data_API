package tsamonte.service.movies.endpoints.people;

import tsamonte.service.movies.base.Result;
import tsamonte.service.movies.database.access.MovieRecords;
import tsamonte.service.movies.database.model.movie.SearchBrowseModel;
import tsamonte.service.movies.idmcaller.IdmCaller;
import tsamonte.service.movies.idmcaller.PrivilegeResponseModel;
import tsamonte.service.movies.logger.ServiceLogger;
import tsamonte.service.movies.models.queryparameter.PeopleQueryModel;
import tsamonte.service.movies.models.response.movies.SearchBrowseResponseModel;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;

/**
 * GET /api/movies/people
 *
 * Request Header Fields:
 *      - email
 *      - session_id
 *      - transaction_id
 *
 * Request Query Fields:
 *      - name (String, required)
 *      - limit (int ,optional): number or results displayed; 10 (default), 25, 50, or 100
 *      - offset (int, optional): for result pagination; 0 (default) or positive multiple of limit
 *      - orderBy (String, optional): sorting parameter; "title" (default), "rating", or "year"
 *      - direction (String, optional): sorting direction; "asc" (default) or "desc"
 *
 * Response Model:
 *      - resultCode (int, required)
 *      - message (String, required)
 *      - movies (SearchBrowseModel[], required)
 *          - movie_id (String, required)
 *          - title (String, required)
 *          - year (int, required)
 *          - director (String, required)
 *          - rating (float, required)
 *          - backdrop_path (String, optional)
 *          - poster_path (String, optional)
 *          - hidden (boolean, optional)
 */
@Path("people")
public class PeopleEndpoint {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response peopleResponse(@Context HttpHeaders headers, @QueryParam("name") String name,
                                   @QueryParam("limit") Integer limit, @QueryParam("offset") Integer offset,
                                   @QueryParam("orderBy") String orderBy, @QueryParam("direction") String direction) {
        SearchBrowseResponseModel responseModel;

        try {
            // if name is blank or empty, no movies will be found, so don't initiate any db logic
            // resultCode = 211; 200 OK; "No movies found with search parameters."
            if(name == null || name.isEmpty()) {
                responseModel = new SearchBrowseResponseModel(Result.NO_MOVIES_FOUND, null);
            }
            else {
                // Call /api/idm/privilege from Identity Management API
                PrivilegeResponseModel privilegeResponse = IdmCaller.callIDMPrivilege(headers.getHeaderString("email"));

                // if user has insufficient privilege or user is not found, hidden should always be null; else, hidden should remain true
                Boolean hidden = true;
                if (privilegeResponse.getResultCode() == PrivilegeResponseModel.INSUFFICIENT || privilegeResponse.getResultCode() == PrivilegeResponseModel.NOT_FOUND) {
                    hidden = null;
                }

                PeopleQueryModel queryModel = new PeopleQueryModel(name, limit, offset, orderBy, direction, hidden);

                SearchBrowseModel[] movies = MovieRecords.retrieve(queryModel);

                // resultCode = 210; 200 OK; "Found movie(s) with search parameters."
                if(movies != null && movies.length > 0) {
                    responseModel = new SearchBrowseResponseModel(Result.MOVIES_FOUND, movies);
                }
                // resultCode = 211; 200 OK; "No movies found with search parameters."
                else {
                    responseModel = new SearchBrowseResponseModel(Result.NO_MOVIES_FOUND, movies);
                }
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
            responseModel = new SearchBrowseResponseModel(Result.INTERNAL_SERVER_ERROR, null);
            return responseModel.buildResponse();
        }
    }
}

package tsamonte.service.movies.endpoints.movies;

import tsamonte.service.movies.base.Result;
import tsamonte.service.movies.database.access.MovieRecords;
import tsamonte.service.movies.database.model.movie.SearchBrowseModel;
import tsamonte.service.movies.idmcaller.IdmCaller;
import tsamonte.service.movies.idmcaller.PrivilegeResponseModel;
import tsamonte.service.movies.logger.ServiceLogger;
import tsamonte.service.movies.models.queryparameter.MovieBrowseQueryModel;
import tsamonte.service.movies.models.response.movies.SearchBrowseResponseModel;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * GET /api/movies/browse/{phrase}
 *
 * Request Header Fields:
 *      - email
 *      - session_id
 *      - transaction_id
 *
 * Path Parameter Fields:
 *      - phrase (string, required): comma-delimited phrase containing keywords to search on.
 *
 * Request Query Fields:
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
@Path("browse")
public class BrowseEndpoint {
    @Path("{phrase}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response browseResponse(@Context HttpHeaders headers, @PathParam("phrase") String phrase,
                                   @QueryParam("limit") Integer limit, @QueryParam("offset") Integer offset,
                                   @QueryParam("orderBy") String orderBy, @QueryParam("direction") String direction) {
        SearchBrowseResponseModel responseModel;

        try {
            // Call /api/idm/privilege from Identity Management API
            PrivilegeResponseModel privilegeResponse = IdmCaller.callIDMPrivilege(headers.getHeaderString("email"), 4);

            // if user has insufficient privilege or user is not found, hidden should always be null; else, hidden should remain true
            Boolean hidden = true;
            if (privilegeResponse.getResultCode() == PrivilegeResponseModel.INSUFFICIENT || privilegeResponse.getResultCode() == PrivilegeResponseModel.NOT_FOUND) {
                hidden = null;
            }

            // Keywords are passed in as a comma-delimited phrase. Extract keywords here
            ArrayList<String> keywords = new ArrayList<String>(Arrays.asList(phrase.split(",")));

            // Build query model to search
            MovieBrowseQueryModel queryModel = new MovieBrowseQueryModel(keywords, limit, offset, orderBy, direction, hidden);

            SearchBrowseModel[] movies = MovieRecords.retrieve(queryModel);

            // resultCode = 210; 200 OK; "Found movie(s) with search parameters."
            if(movies != null && movies.length > 0) {
                responseModel = new SearchBrowseResponseModel(Result.MOVIES_FOUND, movies);
            }
            // resultCode = 211; 200 OK; "No movies found with search parameters."
            else {
                responseModel = new SearchBrowseResponseModel(Result.NO_MOVIES_FOUND, movies);
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

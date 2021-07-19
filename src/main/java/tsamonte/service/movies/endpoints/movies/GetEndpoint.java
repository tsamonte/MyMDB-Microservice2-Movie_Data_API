package tsamonte.service.movies.endpoints.movies;

import tsamonte.service.movies.base.Result;
import tsamonte.service.movies.database.access.MovieRecords;
import tsamonte.service.movies.database.model.movie.MovieGetModel;
import tsamonte.service.movies.idmcaller.IdmCaller;
import tsamonte.service.movies.idmcaller.PrivilegeResponseModel;
import tsamonte.service.movies.logger.ServiceLogger;
import tsamonte.service.movies.models.response.movies.MovieGetResponseModel;
import tsamonte.service.movies.models.response.movies.SearchBrowseResponseModel;

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
 * POST /api/movies/get/{movie_id}
 *
 * Request Header Fields:
 *      - email
 *      - session_id
 *      - transaction_id
 *
 * Path Parameter Fields:
 *      - movie_id (string, required):
 *
 * Response Model:
 *      - resultCode (int, required)
 *      - message (String, required)
 *      - movie (MovieGetModel, required)
 *          - movie_id (String, required)
 *          - title (String, required)
 *          - year (int, required)
 *          - director (String, required)
 *          - rating (float, required)
 *          - num_votes (int, required)
 *          - budget (String, optional)
 *          - revenue (String, optional)
 *          - overview (String, optional)
 *          - backdrop_path (String, optional)
 *          - poster_path (String, optional)
 *          - hidden (boolean, optional)
 *          - genres (GenreModel[]. required)
 *              - genre_id (int, required)
 *              - name (String, required)
 *          - people (PersonNameModel[], required)
 *              - person_id (int, required)
 *              - name (String, required)
 */
@Path("get")
public class GetEndpoint {
    @Path("{movie_id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response movieGetResponse(@Context HttpHeaders headers, @PathParam("movie_id") String movie_id) {
        MovieGetResponseModel responseModel;

        try {
            // Call /api/idm/privilege from Identity Management API
            PrivilegeResponseModel privilegeResponse = IdmCaller.callIDMPrivilege(headers.getHeaderString("email"));

            // if user has insufficient privilege or user is not found, do not show movie
//            Boolean hidden = true;
//            if (privilegeResponse.getResultCode() == PrivilegeResponseModel.INSUFFICIENT || privilegeResponse.getResultCode() == PrivilegeResponseModel.NOT_FOUND) {
//                hidden = null;
//            }

            if(privilegeResponse.getResultCode() == PrivilegeResponseModel.SUFFICIENT) {
                MovieGetModel movie = MovieRecords.retrieveAllMovieData(movie_id);

                // resultCode = 210; 200 OK; "Found movie(s) with search parameters."
                if(movie != null) {
                    responseModel = new MovieGetResponseModel(Result.MOVIES_FOUND, movie);
                }
                // resultCode = 211; 200 OK; "No movies found with search parameters."
                else {
                    responseModel = new MovieGetResponseModel(Result.NO_MOVIES_FOUND, null);
                }
            }
            // If requester has insufficient privilege to view the movie, return Case 211
            // resultCode = 211; 200 OK; "No movies found with search parameters."
            else {
                responseModel = new MovieGetResponseModel(Result.NO_MOVIES_FOUND, null);
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
            responseModel = new MovieGetResponseModel(Result.INTERNAL_SERVER_ERROR, null);
            return responseModel.buildResponse();
        }
    }
}

package tsamonte.service.movies.endpoints.movies;

import tsamonte.service.movies.base.Result;
import tsamonte.service.movies.database.access.MovieRecords;
import tsamonte.service.movies.database.model.movie.SearchBrowseModel;
import tsamonte.service.movies.idmcaller.IdmCaller;
import tsamonte.service.movies.idmcaller.PrivilegeResponseModel;
import tsamonte.service.movies.logger.ServiceLogger;
import tsamonte.service.movies.models.queryparameter.MovieSearchQueryModel;
import tsamonte.service.movies.models.response.movies.SearchBrowseResponseModel;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;

@Path("search")
public class SearchEndpoint {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchResponse(@Context HttpHeaders headers, @QueryParam("title") String title,
                                   @QueryParam("year") Integer year, @QueryParam("director") String director,
                                   @QueryParam("genre") String genre, @QueryParam("hidden") Boolean hidden,
                                   @QueryParam("limit") Integer limit, @QueryParam("offset") Integer offset,
                                   @QueryParam("orderBy") String orderBy, @QueryParam("direction") String direction) {
        SearchBrowseResponseModel responseModel;

        try {
            // Call /api/idm/privilege from Identity Management API
            PrivilegeResponseModel privilegeResponse = IdmCaller.callIDMPrivilege(headers.getHeaderString("email"));

            // if user has insufficient privilege or user is not found, hidden should always be null; else, keep the original value of hidden
            if (privilegeResponse.getResultCode() == PrivilegeResponseModel.INSUFFICIENT || privilegeResponse.getResultCode() == PrivilegeResponseModel.NOT_FOUND) {
                hidden = null;
            }

            // Build query model to search
            MovieSearchQueryModel queryModel = new MovieSearchQueryModel(title, year, director, genre, hidden, limit,
                    offset, orderBy, direction);

            SearchBrowseModel[] movies = MovieRecords.retrieve(queryModel);

            // resultCode = 210; 200 OK; "Found movie(s) with search parameters."
            if(movies != null) {
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
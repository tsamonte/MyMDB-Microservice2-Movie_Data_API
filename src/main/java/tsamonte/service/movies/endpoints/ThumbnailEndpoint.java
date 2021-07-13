package tsamonte.service.movies.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import tsamonte.service.movies.base.Result;
import tsamonte.service.movies.database.access.MovieRecords;
import tsamonte.service.movies.database.model.movie.ThumbnailModel;
import tsamonte.service.movies.logger.ServiceLogger;
import tsamonte.service.movies.models.request.ThumbnailRequestModel;
import tsamonte.service.movies.models.response.movies.ThumbnailResponseModel;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;

/**
 * POST /api/movies/thumbnail
 *
 * Request Model:
 *      - movie_ids(String[], required)
 *
 * Response Model:
 *      - resultCode (int)
 *      - message (string)
 *      - thumbnails (ThumbnailModel[], required)
 *          - movie_id (String, required)
 *          - title (String, required)
 *          - backdrop_path (String, required)
 *          - poster_path (String, required)
 */
@Path("thumbnail")
public class ThumbnailEndpoint {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response thumbnailResponse(@Context HttpHeaders headers, String jsonText) {
        ThumbnailRequestModel requestModel;
        ThumbnailResponseModel responseModel;
        ObjectMapper mapper = new ObjectMapper();

        try {
            requestModel = mapper.readValue(jsonText, ThumbnailRequestModel.class);
        }
        catch (IOException e) {
            e.printStackTrace();

            // resultCode = -1; 500 Internal Server Error; "Internal server error."
            responseModel = new ThumbnailResponseModel(Result.INTERNAL_SERVER_ERROR, null);
            return responseModel.buildResponse();
        }

        ThumbnailModel[] thumbnails = MovieRecords.retrieveThumbnails(requestModel.getMovie_ids());

        // resultCode = 210; 200 OK; "Found movie(s) with search parameters."
        if(thumbnails != null && thumbnails.length > 0) {
            responseModel = new ThumbnailResponseModel(Result.MOVIES_FOUND, thumbnails);
        }
        // resultCode = 211; 200 OK; "No movies found with search parameters."
        else {
            responseModel = new ThumbnailResponseModel(Result.NO_MOVIES_FOUND, thumbnails);
        }

        // headers to pass into response
        HashMap<String,String> headerMap = new HashMap<String,String>();
        headerMap.put("email", headers.getHeaderString("email"));
        headerMap.put("session_id", headers.getHeaderString("session_id"));
        headerMap.put("transaction_id", headers.getHeaderString("transaction_id"));

        ServiceLogger.LOGGER.info(responseModel.getMessage());
        return responseModel.buildResponse(headerMap);
    }
}

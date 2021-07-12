package tsamonte.service.movies.models.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import tsamonte.service.movies.base.Result;
import tsamonte.service.movies.database.model.ThumbnailModel;

/**
 * The class ThumbnailResponseModel will be utilized by the following endpoints:
 *      - /api/movies/thumbnail
 *
 * Response Model:
 *      - resultCode (int, required)
 *      - message (String, required)
 *      - thumbnails (ThumbnailModel[], required)
 *          - movie_id (String, required)
 *          - title (String, required)
 *          - backdrop_path (String, required)
 *          - poster_path (String, required)
 */
public class ThumbnailResponseModel extends BaseResponseModel {
    @JsonProperty(value = "thumbnails", required = true)
    private ThumbnailModel[] thumbnails;

    @JsonCreator
    public ThumbnailResponseModel(Result result,
                                  @JsonProperty(value = "thumbnails", required = true) ThumbnailModel[] thumbnails) {
        super(result);
        this.thumbnails = thumbnails;
    }

    @JsonProperty(value = "thumbnails")
    public ThumbnailModel[] getThumbnails() {
        return thumbnails;
    }
}

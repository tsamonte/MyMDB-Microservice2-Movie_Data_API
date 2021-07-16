package tsamonte.service.movies.idmcaller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The class PrivilegeResponseModel will be utilized when calling the following endpoints from the Identity Management API:
 *      - /api/idm/privilege
 *
 * Response Model:
 *      - resultCode (int, required)
 *      - message (String, required)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PrivilegeResponseModel {
    @JsonProperty(value = "resultCode", required = true)
    private int resultCode;

    @JsonProperty(value = "message", required = true)
    private String message;

    @JsonIgnore
    public static final int NOT_FOUND = 14;

    @JsonIgnore
    public static final int SUFFICIENT = 140;

    @JsonIgnore
    public static final int INSUFFICIENT = 141;

    @JsonCreator
    public PrivilegeResponseModel(@JsonProperty(value = "resultCode", required = true) int resultCode,
                                  @JsonProperty(value = "message", required = true) String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    @JsonProperty(value = "resultCode")
    public int getResultCode() {
        return resultCode;
    }

    @JsonProperty(value = "message")
    public String getMessage() {
        return message;
    }
}

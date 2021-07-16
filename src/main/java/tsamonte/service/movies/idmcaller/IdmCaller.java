package tsamonte.service.movies.idmcaller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.jackson.JacksonFeature;
import tsamonte.service.movies.MoviesService;
import tsamonte.service.movies.logger.ServiceLogger;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

public class IdmCaller {
    /**
     * Makes a post request to another service.
     *
     * @param servicePath The path of the other service (i.e. "http://hostname:port/api/idm")
     * @param endpointPath The path of the endpoint being called (i.e. "/privilege")
     * @param requestModel An object modeling the request json sent to the endpoint
     * @return An object modeling the response json received from the endpoint
     */
    private static PrivilegeResponseModel makePost(String servicePath, String endpointPath, PrivilegeRequestModel requestModel) {
        PrivilegeResponseModel responseModel = null;

        // Create a client
        ServiceLogger.LOGGER.info("Building client...");
        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);

        // Create a WebTarget to sent a request
        ServiceLogger.LOGGER.info("Building WebTarget...");
        WebTarget webTarget = client.target(servicePath).path(endpointPath);

        // Create an InvocationBuilder to create the HTTP request
        ServiceLogger.LOGGER.info("Starting InvocationBuilder...");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        // Send the request and save it to a response
        ServiceLogger.LOGGER.info("Sending request...");
        Response response = invocationBuilder.post(Entity.entity(requestModel, MediaType.APPLICATION_JSON));
        ServiceLogger.LOGGER.info("Request sent.");

        ServiceLogger.LOGGER.info("Received status " + response.getStatus());

        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonText = response.readEntity(String.class);
            responseModel = mapper.readValue(jsonText, PrivilegeResponseModel.class);
            ServiceLogger.LOGGER.info("Successfully mapped response to POJO.");
        }
        catch (IOException e) {
            ServiceLogger.LOGGER.warning("Unable to map response to POJO");
        }

        return responseModel;
    }

    /**
     * Makes a post request to the Identity Management API privilege endpoint:
     *  - /api/idm/privilege
     *
     * @param email The email address of the user making the request
     * @return An object modeling the response json received from the privilege endpoint
     */
    public static PrivilegeResponseModel callIDMPrivilege(String email) {
        String servicePath = MoviesService.getIdmConfigs().getScheme() + MoviesService.getIdmConfigs().getHostName() + ":" + MoviesService.getIdmConfigs().getPort() + MoviesService.getIdmConfigs().getPath();
        String endpointPath = MoviesService.getIdmConfigs().getPrivilegePath();

        PrivilegeRequestModel requestModel = new PrivilegeRequestModel(email, 4);

        PrivilegeResponseModel privilegeResponse = IdmCaller.makePost(servicePath, endpointPath, requestModel);

        return privilegeResponse;
    }
}

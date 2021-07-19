package tsamonte.service.movies.database.access;

import tsamonte.service.movies.MoviesService;
import tsamonte.service.movies.database.model.movie.PersonNameModel;
import tsamonte.service.movies.logger.ServiceLogger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Database functions pertaining to the person table in the database
 */
public class PersonRecords {


    // ================================================COMMON FUNCTIONS================================================
    /**
     * Retrieves all people associated to a movie using the movie's movie_id
     *
     * @param movie_id A movie's movie_id
     * @return An array of objects modeling a row in the person table of the database
     */
    public static PersonNameModel[] retrieveMany(String movie_id) {
        try {
            ArrayList<PersonNameModel> people = new ArrayList<PersonNameModel>();

            String query = "SELECT p.person_id, p.name" +
                    " FROM movie AS m INNER JOIN person_in_movie AS pim ON m.movie_id = pim.movie_id" +
                    " INNER JOIN person AS p ON pim.person_id = p.person_id" +
                    " WHERE m.movie_id = ?";

            PreparedStatement ps = MoviesService.getCon().prepareStatement(query);
            ps.setString(1, movie_id);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            while(rs.next()) {
                people.add(new PersonNameModel(rs.getInt("person_id"),
                        rs.getString ("name")));
            }

            PersonNameModel[] resultsToArray = new PersonNameModel[people.size()];
            resultsToArray = people.toArray(resultsToArray);
            return resultsToArray;
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve person records.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves a director's name based on the passed in person_id
     *
     * @param person_id An int identifying a person in the database
     * @return The name of the person associated with the person_id
     */
    public static String retrieveDirectorName(int person_id) {
        try {
            String result = null;

            String query = "SELECT name" +
                    " FROM person" +
                    " WHERE person_id = ?";

            PreparedStatement ps = MoviesService.getCon().prepareStatement(query);
            ps.setInt(1, person_id);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            while(rs.next()) {
                result = rs.getString("name");
            }

            return result;
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve person records.");
            e.printStackTrace();
            return null;
        }
    }
}

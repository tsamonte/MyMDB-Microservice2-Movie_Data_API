package tsamonte.service.movies.database.access;

import tsamonte.service.movies.MoviesService;
import tsamonte.service.movies.logger.ServiceLogger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database functions pertaining to the gender table in the database
 */
public class GenderRecords {
    public static String retrieve(int gender_id) {
        if(gender_id < 0 || gender_id > 2) gender_id = 0;
        try {
            String query = "SELECT gender_name" +
                    " FROM gender" +
                    " WHERE gender_id = ?";
            PreparedStatement ps = MoviesService.getCon().prepareStatement(query);
            ps.setInt(1, gender_id);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            // only 1 result is expected, so call rs.next once
            rs.next();
            return rs.getString("gender_name");
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve genre records.");
            e.printStackTrace();
            return null;
        }
    }

}

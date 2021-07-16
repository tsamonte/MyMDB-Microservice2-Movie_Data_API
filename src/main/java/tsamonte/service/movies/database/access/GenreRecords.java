package tsamonte.service.movies.database.access;

import tsamonte.service.movies.MoviesService;
import tsamonte.service.movies.database.model.movie.GenreModel;
import tsamonte.service.movies.logger.ServiceLogger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Database functions pertaining to the genre table in the database
 */
public class GenreRecords {
    /**
     * Retrieves all genres associated to a movie using the movie's movie_id
     *
     * @param movie_id A movie's movie_id
     * @return An array of objects modeling a row in the genre table of the database
     */
    public static GenreModel[] retrieveMany(String movie_id) {
        try {
            ArrayList<GenreModel> genres = new ArrayList<GenreModel>();

            String query = "SELECT g.genre_id, g.name" +
                    " FROM movie as m INNER JOIN genre_in_movie AS gim ON m.movie_id = gim.movie_id" +
                    " INNER JOIN genre AS g ON gim.genre_id = g.genre_id" +
                    " WHERE m.movie_id = ?";

            PreparedStatement ps = MoviesService.getCon().prepareStatement(query);
            ps.setString(1, movie_id);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            while(rs.next()) {
                genres.add(new GenreModel(rs.getInt("genre_id"),
                        rs.getString("name")));
            }

            GenreModel[] resultsToArray = new GenreModel[genres.size()];
            resultsToArray = genres.toArray(resultsToArray);
            return resultsToArray;
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve genre records.");
            e.printStackTrace();
            return null;
        }
    }
}

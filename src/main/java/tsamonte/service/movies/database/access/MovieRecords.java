package tsamonte.service.movies.database.access;

import tsamonte.service.movies.MoviesService;
import tsamonte.service.movies.database.model.movie.MovieModel;
import tsamonte.service.movies.database.model.movie.ThumbnailModel;
import tsamonte.service.movies.logger.ServiceLogger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MovieRecords {
    public static MovieModel retrieve(String movie_id) {
        try {
            MovieModel result = null;

            String query = "SELECT *" +
                    " FROM movies" +
                    " WHERE movie_id = ?";
            PreparedStatement ps = MoviesService.getCon().prepareStatement(query);
            ps.setString(1, movie_id);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                result = new MovieModel(rs.getString("movie_id"),
                        rs.getString("title"),
                        rs.getInt("year"),
                        rs.getInt("director_id"),
                        rs.getFloat("rating"),
                        rs.getInt("num_votes"),
                        rs.getString("budget"),
                        rs.getString("revenue"),
                        rs.getString("overview"),
                        rs.getString("backdrop_path"),
                        rs.getString("poster_path"),
                        rs.getBoolean("hidden"));
            }

            return result;
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: unable to retrieve movie records.");
            e.printStackTrace();
            return null;
        }
    }

    public static MovieModel[] retrieveMany(String[] movie_ids) {
        try {
            ArrayList<MovieModel> results = new ArrayList<MovieModel>();

            String query = buildQuery(movie_ids.length);

            PreparedStatement ps = MoviesService.getCon().prepareStatement(query);

            for (int i = 0; i < movie_ids.length; i++) {
                ps.setString(i+1, movie_ids[i]);
            }

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            while(rs.next()) {
                results.add(new MovieModel(rs.getString("movie_id"),
                        rs.getString("title"),
                        rs.getInt("year"),
                        rs.getInt("director_id"),
                        rs.getFloat("rating"),
                        rs.getInt("num_votes"),
                        rs.getString("budget"),
                        rs.getString("revenue"),
                        rs.getString("overview"),
                        rs.getString("backdrop_path"),
                        rs.getString("poster_path"),
                        rs.getBoolean("hidden")));
            }

            MovieModel[] resultsToArray = new MovieModel[results.size()];
            resultsToArray = results.toArray(resultsToArray);
            return resultsToArray;
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: unable to retrieve movie records.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Builds a query that will be passed into a prepared statement. Depending on the passed in length, the "WHERE" clause
     * of the SQL statement will have more conditions added.
     *
     * To be used with MovieRecords.retrieveMany(String[] movie_ids)
     *
     * @param length The length of the movie_ids array passed into the retrieveMany function
     * @return A string representing a SQL query
     */
    private static String buildQuery(int length) {
        String query = "SELECT *" +
                " FROM movie" +
                " WHERE";
        if (length == 0) {
            query += " 1=1";
        } else {
            for (int i = 0; i < length; i++) {
                if (i != 0) {
                    query += " OR movie_id = ?";
                } else {
                    query += " movie_id = ?";
                }
            }
        }

        return query;
    }

    /**
     * Retrieves movies from the database and extracts the necessary data needed for the endpoint at path:
     * /api/movies/thumbnail
     *
     * @param movie_ids An array of movie ids to retrieve from database
     * @return Array of objects that contain information needed for the Thumbnail endpoint response
     */
    public static ThumbnailModel[] retrieveThumbnails(String[] movie_ids) {
        MovieModel[] movies = retrieveMany(movie_ids);
        ThumbnailModel[] results = null;

        if(movies != null) {
            results = new ThumbnailModel[movies.length];
            for (int i = 0; i < movies.length; i++) {
                results[i] = new ThumbnailModel(movies[i]);
            }
        }
        return results;
    }
}

package tsamonte.service.movies.database.access;

import tsamonte.service.movies.MoviesService;
import tsamonte.service.movies.database.model.movie.*;
import tsamonte.service.movies.logger.ServiceLogger;
import tsamonte.service.movies.models.queryparameter.MovieBrowseQueryModel;
import tsamonte.service.movies.models.queryparameter.MovieSearchQueryModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Database functions pertaining mostly to the movie table in the database
 */
public class MovieRecords {
    // ================================================/API/MOVIES/SEARCH================================================
    /**
     * Builds a MySQL query that will be passed into a prepared statement. WHERE conditions of the query will be added based
     * on what is passed in through the queryModel parameter
     *
     * Related endpoints :
     *      - /api/movies/search
     *
     * @param queryModel An object containing info about the passed in query parameters
     * @return A string representing a SQL statement before being passed into a PreparedStatement.
     */
    private static String buildQuery(MovieSearchQueryModel queryModel) {
        String SELECT = "SELECT DISTINCT m.movie_id, m.title, m.year, p.name, m.rating, m.backdrop_path, m.poster_path, m.hidden";
        String FROM = " FROM movie AS m INNER JOIN person AS p ON m.director_id = p.person_id" +
                " INNER JOIN genre_in_movie AS gim ON m.movie_id = gim.movie_id" +
                " INNER JOIN genre AS g on g.genre_id = gim.genre_id";
        String WHERE = " WHERE 1=1";
        String ORDERBY = " ORDER BY " + queryModel.getOrderBy() + " " + queryModel.getDirection();
        String LIMIT = " LIMIT " + queryModel.getLimit() + " OFFSET " + queryModel.getOffset();

        if(queryModel.getTitle() != null)       WHERE += " AND m.title LIKE ?";
        if(queryModel.getYear() != null)        WHERE += " AND m.year = ?";
        if(queryModel.getDirector() != null)    WHERE += " AND p.name LIKE ?";
        if(queryModel.getGenre() != null)       WHERE += " AND g.name LIKE ?";

        // If query param says not to show hidden, then regardless of plevel, only show rows where m.hidden = false
        if(queryModel.showHidden() == null || !queryModel.showHidden()) WHERE += " AND hidden = FALSE";

        // Secondary sort
        if(queryModel.getOrderBy().equals("title") || queryModel.getOrderBy().equals("year")) ORDERBY += ", rating DESC";
        else if(queryModel.getOrderBy().equals("rating")) ORDERBY += ", title ASC";

        return SELECT + FROM + WHERE + ORDERBY + LIMIT;
    }

    /**
     * Prepares the query to be called and returns the ResultSet retrieved from the database. Returned ResultSet can be null.
     *
     * Related endpoints :
     *      - /api/movies/search
     *
     * @param queryModel An object containing info about the passed in query parameters
     * @return ResultSet containing data retrieved from the database. Can be null.
     */
    private static ResultSet getResult(MovieSearchQueryModel queryModel) {
        try {
            String query = buildQuery(queryModel);
            PreparedStatement ps = MoviesService.getCon().prepareStatement(query);

            int psIndex = 1;
            if(queryModel.getTitle() != null) {
                ps.setString(psIndex, "%" + queryModel.getTitle() + "%");
                psIndex++;
            }
            if(queryModel.getYear() != null) {
                ps.setInt(psIndex, queryModel.getYear());
                psIndex++;
            }
            if(queryModel.getDirector() != null) {
                ps.setString(psIndex, "%" + queryModel.getDirector() + "%");
                psIndex++;
            }
            if(queryModel.getGenre() != null) {
                ps.setString(psIndex, "%" + queryModel.getGenre() + "%");
            }

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            return rs;
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve movie records.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Using the previous two functions (buildQuery and getResult), this function builds the SQL query based on the passed
     * in query model. The query is called upon the database, and the results are mapped to objects that will be returned
     * here.
     *
     * Related endpoints :
     *      - /api/movies/search
     *
     * @param queryModel An object containing info about the passed in query parameters
     * @return A list of objects that model an encapsulating field in the endpoint's response JSON
     */
    public static SearchBrowseModel[] retrieve(MovieSearchQueryModel queryModel) {
        try {
            ArrayList<SearchBrowseModel> results = new ArrayList<SearchBrowseModel>();
            ResultSet rs = getResult(queryModel);

            if(rs != null) {
                while (rs.next()) {
                    results.add(new SearchBrowseModel(rs.getString("movie_id"),
                            rs.getString("title"),
                            rs.getInt("year"),
                            rs.getString("name"),
                            rs.getFloat("rating"),
                            rs.getString("backdrop_path"),
                            rs.getString("poster_path"),
                            queryModel.showHidden() == null || !queryModel.showHidden() ? null : rs.getBoolean("hidden")
                    ));
                }
            }

            SearchBrowseModel[] resultsToArray = new SearchBrowseModel[results.size()];
            resultsToArray = results.toArray(resultsToArray);
            return resultsToArray;
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve movie records.");
            e.printStackTrace();
            return null;
        }
    }

    // ================================================/API/MOVIES/BROWSE/{PHRASE}================================================
    /**
     * Builds a MySQL query that will be passed into a prepared statement. WHERE conditions of the query will be added based
     * on what is passed in through the queryModel parameter
     *
     * Related endpoints :
     *      - /api/movies/browse/{phrase}
     *
     * @param queryModel An object containing info about the passed in query parameters
     * @return A string representing a SQL statement before being passed into a PreparedStatement.
     */
    private static String buildQuery(MovieBrowseQueryModel queryModel) {
        String SELECT = "SELECT DISTINCT m.movie_id, m.title, m.year, p.name, m.rating, m.backdrop_path, m.poster_path, m.hidden";
        String FROM = " FROM movie AS m INNER JOIN person AS p ON m.director_id = p.person_id";
        String WHERE = " WHERE 1=1";
        String ORDERBY = " ORDER BY " + queryModel.getOrderBy() + " " + queryModel.getDirection();
        String LIMIT = " LIMIT " + queryModel.getLimit() + " OFFSET " + queryModel.getOffset();

        // Add keywords to database query
        if(queryModel.getKeywords() != null && !queryModel.getKeywords().isEmpty()) {
            for(int i = 0; i < queryModel.getKeywords().size(); i++) {
                FROM += String.format(" INNER JOIN keyword_in_movie AS kim%d ON m.movie_id = kim%d.movie_id", i, i);
                FROM += String.format(" INNER JOIN keyword AS k%d ON k%d.keyword_id = kim%d.keyword_id", i, i, i);

                WHERE += String.format(" AND k%d.name = ?", i);
            }
        }

        // If query param says not to show hidden, then regardless of plevel, only show rows where m.hidden = false
        if(queryModel.showHidden() == null || !queryModel.showHidden()) WHERE += " AND hidden = FALSE";

        // Secondary sort
        if(queryModel.getOrderBy().equals("title") || queryModel.getOrderBy().equals("year")) ORDERBY += ", rating DESC";
        else if(queryModel.getOrderBy().equals("rating")) ORDERBY += ", title ASC";

        return SELECT + FROM + WHERE + ORDERBY + LIMIT;
    }

    /**
     * Prepares the query to be called and returns the ResultSet retrieved from the database. Returned ResultSet can be null.
     *
     * Related endpoints :
     *      - /api/movies/browse/{phrase}
     *
     * @param queryModel An object containing info about the passed in query parameters
     * @return ResultSet containing data retrieved from the database. Can be null.
     */
    private static ResultSet getResult(MovieBrowseQueryModel queryModel) {
        try {
            String query = buildQuery(queryModel);
            PreparedStatement ps = MoviesService.getCon().prepareStatement(query);

            ArrayList<String> keywords = queryModel.getKeywords();
            for(int i = 0; i < keywords.size(); i++) {
                ps.setString(i+1, keywords.get(i));
            }

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            return rs;
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve movie records.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Using the previous two functions (buildQuery and getResult), this function builds the SQL query based on the passed
     * in query model. The query is called upon the database, and the results are mapped to objects that will be returned
     * here.
     *
     * Related endpoints :
     *      - /api/movies/browse/{phrase}
     *
     * @param queryModel An object containing info about the passed in query parameters
     * @return A list of objects that model an encapsulating field in the endpoint's response JSON
     */
    public static SearchBrowseModel[] retrieve(MovieBrowseQueryModel queryModel) {
        try {
            ArrayList<SearchBrowseModel> results = new ArrayList<SearchBrowseModel>();
            ResultSet rs = getResult(queryModel);

            if(rs != null) {
                while (rs.next()) {
                    results.add(new SearchBrowseModel(rs.getString("movie_id"),
                            rs.getString("title"),
                            rs.getInt("year"),
                            rs.getString("name"),
                            rs.getFloat("rating"),
                            rs.getString("backdrop_path"),
                            rs.getString("poster_path"),
                            queryModel.showHidden() == null || !queryModel.showHidden() ? null : rs.getBoolean("hidden")
                    ));
                }
            }

            SearchBrowseModel[] resultsToArray = new SearchBrowseModel[results.size()];
            resultsToArray = results.toArray(resultsToArray);
            return resultsToArray;
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve movie records.");
            e.printStackTrace();
            return null;
        }
    }

    // ================================================/API/MOVIES/GET/{MOVIE_ID}================================================
    /**
     * Retrieves extensive data about a specified movie from the database and extracts the necessary fields, based on
     * the passed in movie_id
     *
     * Related endpoints :
     *      - /api/movies/get/{movie_id}
     *
     * @param movie_id A string representing a movie's unique id
     * @return An object representing data about a specified movie
     */
    public static MovieGetModel retrieveAllMovieData(String movie_id) {
        MovieGetModel result = null;
        MovieModel movieInfo = retrieve(movie_id);

        // if the movie isn't found through its id, we don't bother looking through other tables using the same movie_id
        if(movieInfo != null) {
            GenreModel[] genreInfo = GenreRecords.retrieveMany(movie_id);
            PersonNameModel[] peopleInfo = PersonRecords.retrieveMany(movie_id);
            result = new MovieGetModel(movieInfo, genreInfo, peopleInfo);
        }

        return result;
    }
    // ================================================/API/MOVIES/THUMBNAIL================================================
    /**
     * Retrieves movies from the database and extracts the necessary data needed for the thumbnail endpoint
     *
     * Related endpoints :
     *      - /api/movies/thumbnail
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
    // ================================================COMMON FUNCTIONS================================================
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

    public static MovieModel retrieve(String movie_id) {
        try {
            MovieModel result = null;

            String query = "SELECT *" +
                    " FROM movie" +
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
}

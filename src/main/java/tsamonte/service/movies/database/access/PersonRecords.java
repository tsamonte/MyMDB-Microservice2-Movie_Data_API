package tsamonte.service.movies.database.access;

import tsamonte.service.movies.MoviesService;
import tsamonte.service.movies.database.model.movie.PersonNameModel;
import tsamonte.service.movies.database.model.person.PeopleSearchModel;
import tsamonte.service.movies.logger.ServiceLogger;
import tsamonte.service.movies.models.queryparameter.PeopleSearchQueryModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Database functions pertaining to the person table in the database
 */
public class PersonRecords {
    // ================================================/API/MOVIES/PEOPLE/SEARCH================================================
    /**
     * Builds a MySQL query that will be passed into a prepared statement. WHERE conditions of the query will be added based
     * on what is passed in through the queryModel parameter
     *
     * Related endpoints :
     *      - /api/movies/people/search
     *
     * @param queryModel An object containing info about the passed in query parameters
     * @return A string representing a SQL statement before being passed into a PreparedStatement.
     */
    private static String buildQuery(PeopleSearchQueryModel queryModel) {
        String SELECT = "SELECT DISTINCT p.person_id, p.name, p.birthday, p.popularity, p.profile_path";
        String FROM = " FROM person AS p";
        String WHERE = " WHERE 1=1";
        String ORDERBY = " ORDER BY " + queryModel.getOrderBy() + " " + queryModel.getDirection();
        String LIMIT = " LIMIT " + queryModel.getLimit() + " OFFSET " + queryModel.getOffset();

        if(queryModel.getName() != null) WHERE += " AND p.name LIKE ?";
        if(queryModel.getBirthday() != null) WHERE += " AND p.birthday = ?";
        if(queryModel.getMovie_title() != null) {
            // if searching by movie title, we also have to join the movie table with the person one
            FROM += " INNER JOIN person_in_movie AS pim ON p.person_id = pim.person_id" +
                    " INNER JOIN movie AS m ON pim.movie_id = m.movie_id";
            WHERE += " AND m.title LIKE ?";
        }

        // Secondary sort
        if(queryModel.getOrderBy().equals("name") || queryModel.getOrderBy().equals("birthday")) ORDERBY += ", popularity DESC";
        else if(queryModel.getOrderBy().equals("popularity")) ORDERBY += ", name ASC";

        return SELECT + FROM + WHERE + ORDERBY + LIMIT;
    }

    /**
     * Prepares the query to be called and returns the ResultSet retrieved from the database. Returned ResultSet can be null.
     *
     * Related endpoints :
     *      - /api/movies/people/search
     *
     * @param queryModel An object containing info about the passed in query parameters
     * @return ResultSet containing data retrieved from the database. Can be null.
     */
    private static ResultSet getResult(PeopleSearchQueryModel queryModel) {
        try {
            String query = buildQuery(queryModel);
            PreparedStatement ps = MoviesService.getCon().prepareStatement(query);

            int psIndex = 1;
            if (queryModel.getName() != null) {
                ps.setString(psIndex, "%" + queryModel.getName() + "%");
                psIndex++;
            }
            if(queryModel.getBirthday() != null) {
                ps.setString(psIndex, queryModel.getBirthday());
                psIndex++;
            }
            if(queryModel.getMovie_title() != null) {
                ps.setString(psIndex, "%" + queryModel.getMovie_title() + "%");
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
     *      - /api/movies/people/search
     *
     * @param queryModel An object containing info about the passed in query parameters
     * @return A list of objects that model an encapsulating field in the endpoint's response JSON
     */
    public static PeopleSearchModel[] retrieve(PeopleSearchQueryModel queryModel) {
        try {
            ArrayList<PeopleSearchModel> results = new ArrayList<PeopleSearchModel>();
            ResultSet rs = getResult(queryModel);

            if(rs != null) {
                while (rs.next()) {
                    results.add(new PeopleSearchModel(
                            rs.getInt("person_id"),
                            rs.getString("name"),
                            rs.getString("birthday"),
                            rs.getFloat("popularity"),
                            rs.getString("profile_path")
                    ));
                }
            }

            PeopleSearchModel[] resultsToArray = new PeopleSearchModel[results.size()];
            resultsToArray = results.toArray(resultsToArray);
            return resultsToArray;
        }
        catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Query failed: Unable to retrieve movie records.");
            e.printStackTrace();
            return null;
        }
    }
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

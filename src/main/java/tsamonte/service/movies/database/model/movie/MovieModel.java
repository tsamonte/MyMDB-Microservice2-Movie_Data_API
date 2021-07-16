package tsamonte.service.movies.database.model.movie;

import tsamonte.service.movies.database.access.PersonRecords;

/**
 * Class MovieModel models the structure of the movie table in the database.
 * This class will be used when retrieving entire rows from the database.
 */
public class MovieModel {
    // the following values will be non-null
    private String movie_id;
    private String title;
    private int year;
    private int director_id;
    private float rating;
    private int num_votes;

    // the following values can be null
    private String budget;  // String to deal with very large ints
    private String revenue; // String to deal with very large ints
    private String overview;
    private String backdrop_path;
    private String poster_path;
    private Boolean hidden;

    public MovieModel(String movie_id, String title, int year, int director_id,
                      float rating, int num_votes, String budget, String revenue,
                      String overview, String backdrop_path, String poster_path, Boolean hidden) {
        this.movie_id = movie_id;
        this.title = title;
        this.year = year;
        this.director_id = director_id;
        this.rating = rating;
        this.num_votes = num_votes;
        this.budget = budget;
        this.revenue = revenue;
        this.overview = overview;
        this.backdrop_path = backdrop_path;
        this.poster_path = poster_path;
        this.hidden = hidden;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public int getDirector_id() {
        return director_id;
    }

    public float getRating() {
        return rating;
    }

    public int getNum_votes() {
        return num_votes;
    }

    public String getBudget() {
        return budget;
    }

    public String getRevenue() {
        return revenue;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public Boolean getHidden() {
        return hidden;
    }

    /**
     * Retrieves the director's name based on this MovieModel's director_id
     * @return director's name retrieved from database
     */
    public String getDirector() {
        return PersonRecords.retrieveDirectorName(this.director_id);
    }
}

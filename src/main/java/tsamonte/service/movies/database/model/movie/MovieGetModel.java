package tsamonte.service.movies.database.model.movie;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class MovieGetModel models the structure of the movie table in the database, except all foreign keys are replaced with the relevant
 * information from its respective table. There are also two additional fields: genres and people.
 * All of these fields will be necessary when retrieving a single movie's details.
 *
 * Relevant endpoints:
 *  - /api/movies/get/{movie_id}
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieGetModel {
    @JsonProperty(value = "movie_id", required = true)
    private String movie_id;

    @JsonProperty(value = "title", required = true)
    private String title;

    @JsonProperty(value = "year", required = true)
    private int year;

    @JsonProperty(value = "director", required = true)
    private String director;

    @JsonProperty(value = "rating", required = true)
    private float rating;

    @JsonProperty(value = "num_votes", required = true)
    private int num_votes;

    @JsonProperty(value = "budget")
    private String budget;

    @JsonProperty(value = "revenue")
    private String revenue;

    @JsonProperty(value = "overview")
    private String overview;

    @JsonProperty(value = "backdrop_path")
    private String backdrop_path;

    @JsonProperty(value = "poster_path")
    private String poster_path;

    @JsonProperty(value = "hidden")
    private Boolean hidden;

    @JsonProperty(value = "genres", required = true)
    private GenreModel[] genres;

    @JsonProperty(value = "people", required = true)
    private PersonNameModel[] people;

    @JsonCreator
    public MovieGetModel(@JsonProperty(value = "movie_id", required = true) String movie_id,
                         @JsonProperty(value = "title", required = true) String title,
                         @JsonProperty(value = "year", required = true) int year,
                         @JsonProperty(value = "director", required = true) String director,
                         @JsonProperty(value = "rating", required = true) float rating,
                         @JsonProperty(value = "num_votes", required = true) int num_votes,
                         @JsonProperty(value = "budget") String budget,
                         @JsonProperty(value = "revenue") String revenue,
                         @JsonProperty(value = "overview") String overview,
                         @JsonProperty(value = "backdrop_path") String backdrop_path,
                         @JsonProperty(value = "poster_path") String poster_path,
                         @JsonProperty(value = "hidden") Boolean hidden,
                         @JsonProperty(value = "genres", required = true) GenreModel[] genres,
                         @JsonProperty(value = "people", required = true)PersonNameModel[] people) {
        this.movie_id = movie_id;
        this.title = title;
        this.year = year;
        this.director = director;
        this.rating = rating;
        this.num_votes = num_votes;
        this.budget = budget;
        this.revenue = revenue;
        this.overview = overview;
        this.backdrop_path = backdrop_path;
        this.poster_path = poster_path;
        this.hidden = hidden;
        this.genres = genres;
        this.people = people;
    }

    /**
     * Instead of taking fields directly, this constructor will take an entire MovieModel object, GenreModel object,
     * and PersonaNameModel object. Necessary fields will be extracted from each object.
     *
     * @param movieModel Object modeling an entire row of the movie table in the database
     * @param genres Array of objects modeling a row of the genre table in the database
     * @param people Array of objects modeling a portion of the person table in the database
     */
    public MovieGetModel(MovieModel movieModel, GenreModel[] genres, PersonNameModel[] people) {
        this.movie_id = movieModel.getMovie_id();
        this.title = movieModel.getTitle();
        this.year = movieModel.getYear();
        this.director = movieModel.getDirector();
        this.rating = movieModel.getRating();
        this.num_votes = movieModel.getNum_votes();
        this.budget = movieModel.getBudget();
        this.revenue = movieModel.getRevenue();
        this.overview = movieModel.getOverview();
        this.backdrop_path = movieModel.getBackdrop_path();
        this.poster_path = movieModel.getPoster_path();
        this.hidden = movieModel.getHidden();
        this.genres = genres;
        this.people = people;
    }

    @JsonProperty(value = "movie_id")
    public String getMovie_id() { return movie_id; }

    @JsonProperty(value = "title")
    public String getTitle() { return title; }

    @JsonProperty(value = "year")
    public int getYear() { return year; }

    @JsonProperty(value = "director")
    public String getDirector() { return director; }

    @JsonProperty(value = "rating")
    public float getRating() { return rating; }

    @JsonProperty(value = "num_votes")
    public int getNum_votes() { return num_votes; }

    @JsonProperty(value = "budget")
    public String getBudget() { return budget; }

    @JsonProperty(value = "revenue")
    public String getRevenue() { return revenue; }

    @JsonProperty(value = "overview")
    public String getOverview() { return overview; }

    @JsonProperty(value = "backdrop_path")
    public String getBackdrop_path() { return backdrop_path; }

    @JsonProperty(value = "poster_path")
    public String getPoster_path() { return poster_path; }

    @JsonProperty(value = "hidden")
    public Boolean getHidden() { return hidden; }

    @JsonProperty(value = "genres")
    public GenreModel[] getGenres() { return genres; }

    @JsonProperty(value = "people")
    public PersonNameModel[] getPeople() { return people; }
}

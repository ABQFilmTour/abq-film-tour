package edu.cnm.deepdive.abq_film_tour.model.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import java.util.UUID;

/**
 * This class represents a production of a film or television series. It is closely tied to data
 * retrieved from IMDb through the OMDb API.
 */
@Entity(
    foreignKeys = {
        @ForeignKey(entity = FilmLocation.class, parentColumns = "film_location_id",
            childColumns = "film_location_id", onDelete = ForeignKey.CASCADE)
    }
)
public class Production {

  /**
   * The UUID of this production on the ABQ Film Tour server.
   */
  @PrimaryKey
  @Expose
  private UUID id;

  /**
   * The ID of this production in the IMDb, a 7 digit number prefixed with "tt" for "title".
   */
  @Expose
  private String imdbId;

  /**
   * A type of "movie" or "series" to specify what kind of production this is.
   */
  @Expose
  private String type;

  /**
   * The year this production was released.
   */
  @Expose
  private String releaseYear;

  /**
   * The title of the series or film.
   */
  @Expose
  private String title;

  /**
   * A brief plot summary up to 300 characters in length.
   */
  @Expose
  private String plot;

  /**
   * The OMDB url of a poster image for the associated production.
   */
  @Expose
  private String posterUrl;

  /**
   * Returns the UUID of this production on the ABQ Film Tour server.
   * @return the UUID of this production on the ABQ Film Tour server.
   */
  public UUID getId() {
    return id;
  }

  /**
   * Sets the UUID of this production on the ABQ Film Tour server.
   * @param id the UUID of this production on the ABQ Film Tour server.
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Returns the ID of this production in the IMDb, a 7 digit number prefixed with "tt" for "title".
   * @return the ID of this production in the IMDb, a 7 digit number prefixed with "tt" for "title".
   */
  public String getImdbID() {
    return imdbId;
  }

  /**
   * Sets the ID of this production in the IMDb, a 7 digit number prefixed with "tt" for "title".
   * @param imdbID the ID of this production in the IMDb, a 7 digit number prefixed with "tt" for "title".
   */
  public void setImdbID(String imdbID) {
    this.imdbId = imdbID;
  }

  /**
   * Returns the year this production was released.
   * @return the year this production was released.
   */
  public String getReleaseYear() {
    return releaseYear;
  }

  /**
   * Sets the year this production was released.
   * @param releaseYear the year this production was released.
   */
  public void setReleaseYear(String releaseYear) {
    this.releaseYear = releaseYear;
  }

  /**
   * Returns a type of "movie" or "series" to specify what kind of production this is.
   * @return a type of "movie" or "series" to specify what kind of production this is.
   */
  public String getType() {
    return type;
  }

  /**
   * Sets a type of "movie" or "series" to specify what kind of production this is.
   * @param type a type of "movie" or "series" to specify what kind of production this is.
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Returns the title of the series or film.
   * @return the title of the series or film.
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the title of the series or film.
   * @param title the title of the series or film.
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Returns a brief plot summary up to 300 characters in length.
   * @return a brief plot summary up to 300 characters in length.
   */
  public String getPlot() {
    return plot;
  }

  /**
   * Sets the plot summary.
   * @param plot a brief plot summary up to 300 characters in length.
   */
  public void setPlot(String plot) {
    this.plot = plot;
  }

  public String getPosterUrl() {
    return posterUrl;
  }

  public void setPosterUrl(String posterUrl) {
    this.posterUrl = posterUrl;
  }

}
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
   * The UUID of this production on the server.
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
   * Specifies what type the production is. The production can be of type "movie" or "series".
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
   * Returns the UUID of this production on the server.
   *
   * @return the UUID of this production on the server
   */
  public UUID getId() {
    return id;
  }

  /**
   * Sets the UUID of this production on the server.
   *
   * @param id the UUID of this production on the server.
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Returns the ID of this production in the IMDb, a 7 digit number prefixed with "tt" for
   * "title".
   *
   * @return the ID of this production in the IMDb
   */
  public String getImdbID() {
    return imdbId;
  }

  /**
   * Sets the ID of this production in the IMDb, a 7 digit number prefixed with "tt" for "title".
   *
   * @param imdbID the ID of this production in the IMDb
   */
  public void setImdbID(String imdbID) {
    this.imdbId = imdbID;
  }

  /**
   * Returns the year this production was released.
   *
   * @return the year this production was released
   */
  public String getReleaseYear() {
    return releaseYear;
  }

  /**
   * Sets the year this production was released.
   *
   * @param releaseYear the year this production was released
   */
  public void setReleaseYear(String releaseYear) {
    this.releaseYear = releaseYear;
  }

  /**
   * Returns the type of production this is. A production can be of type "movie" or "series".
   *
   * @return type of "movie" or "series" to specify what kind of production this is
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the type of production this is. A production can be of type "movie" or "series".
   *
   * @param type the type of "movie" or "series" to specify what kind of production this is
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Returns the title of the series or film.
   *
   * @return the title of the series or film
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the title of the series or film.
   *
   * @param title the title of the series or film
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Returns a brief plot summary up to 300 characters in length.
   *
   * @return a brief plot summary
   */
  public String getPlot() {
    return plot;
  }

  /**
   * Sets the plot summary up to 300 characters in length.
   *
   * @param plot a brief plot summary
   */
  public void setPlot(String plot) {
    this.plot = plot;
  }

  /**
   * Gets the url for the poster image.
   *
   * @return the poster image url
   */
  public String getPosterUrl() {
    return posterUrl;
  }

  /**
   * Sets the url for the poster image.
   *
   * @param posterUrl the poster image url
   */
  public void setPosterUrl(String posterUrl) {
    this.posterUrl = posterUrl;
  }

}
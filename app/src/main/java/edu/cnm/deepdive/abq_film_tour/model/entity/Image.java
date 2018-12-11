package edu.cnm.deepdive.abq_film_tour.model.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import com.google.gson.annotations.Expose;
import java.util.Date;
import java.util.UUID;

/**
 * This class will represent images submitted to the server to represent film locations.
 * It is currently under construction.
 */
@Entity(
    foreignKeys = {
        @ForeignKey(entity = FilmLocation.class, parentColumns = "film_location_id",
            childColumns = "film_location_id", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = User.class, parentColumns = "user_id",
            childColumns = "user_id", onDelete = ForeignKey.CASCADE)
    }
)
public class Image {

  /**
   * The UUID of this image on the server.
   */
  @Expose
  private UUID id;

  /**
   * The time and date this image was submitted.
   */
  @Expose
  private Date created;

  /**
   * The FilmLocation this image is associated with.
   */
  @Expose
  private FilmLocation filmLocation;

  /**
   * The user who submitted this image.
   */
  @Expose
  private User user;

  /**
   * A brief description of the contents of this image. (optional)
   */
  @Expose
  private String description;

  /**
   * The url this image should be located at on the central domain.
   */
  @Expose
  private String url;

  /**
   * Returns the UUID of this image on the server.
   * @return the UUID of this image on the server.
   */
  public UUID getId() {
    return id;
  }

  /**
   * Sets the UUID of this image on the server.
   * @param id the UUID of this image on the server.
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Returns the film location this image is associated with.
   * @return the FilmLocation this image is associated with.
   */
  public FilmLocation getFilmLocation() {
    return filmLocation;
  }

  /**
   * Sets the FilmLocation this image is associated with.
   * @param filmLocation the FilmLocation this image is associated with.
   */
  public void setFilmLocation(FilmLocation filmLocation) {
    this.filmLocation = filmLocation;
  }

  /**
   * Returns the user who submitted this image.
   * @return the user who submitted this image.
   */
  public User getUser() {
    return user;
  }

  /**
   * Sets the user who submitted this image.
   * @param user the user who submitted this image.
   */
  public void setUser(User user) {
    this.user = user;
  }

  /**
   * Returns the time and date this image was submitted.
   * @return the time and date this image was submitted.
   */
  public Date getCreated() {
    return created;
  }

  /**
   * Sets the time and date this image was submitted.
   * @param created the time and date this image was submitted.
   */
  public void setCreated(Date created) {
    this.created = created;
  }

  /**
   * Returns a brief description of the contents of this image.
   * @return a brief description of the contents of this image.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets brief description of the contents of this image.
   * @param description a brief description of the contents of this image.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Returns the url this image should be located at on the central domain.
   * @return the url this image should be located at on the central domain.
   */
  public String getUrl() {
    return url;
  }

  /**
   * Sets the url this image should be located at on the central domain.
   * @param url the url this image should be located at on the central domain.
   */
  public void setUrl(String url) {
    this.url = url;
  }

}
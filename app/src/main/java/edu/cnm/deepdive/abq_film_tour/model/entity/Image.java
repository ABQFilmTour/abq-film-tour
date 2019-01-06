package edu.cnm.deepdive.abq_film_tour.model.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import com.google.gson.annotations.Expose;
import java.util.Date;
import java.util.UUID;

/**
 * This class will represent images submitted to the server to represent film locations. It is
 * currently under construction.
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
   * The Google ID of the user who submitted this comment.
   */
  @Expose
  private String googleId;

  /**
   * Url to the user's Google profile image.
   */
  @Expose
  private String userPictureUrl;

  /**
   * The name of the user.
   */
  @Expose
  private String userName;

  /**
   * Flag to verify that an image has been approved by an admin and can be displayed if security
   * is tightened. Probably unnecessary for now, but better to have if we implement later.
   */
  @Expose
  private boolean approved;

  /**
   * Returns the UUID of this image on the server.
   *
   * @return the UUID of this image on the server
   */
  public UUID getId() {
    return id;
  }

  /**
   * Sets the UUID of this image on the server.
   *
   * @param id the UUID of this image on the server
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Returns the associated film location.
   *
   * @return the associated film location
   */
  public FilmLocation getFilmLocation() {
    return filmLocation;
  }

  /**
   * Sets the FilmLocation this image is associated with.
   *
   * @param filmLocation the FilmLocation this image is associated with.
   */
  public void setFilmLocation(FilmLocation filmLocation) {
    this.filmLocation = filmLocation;
  }

  /**
   * Returns the time and date this image was submitted.
   *
   * @return the time and date this image was submitted.
   */
  public Date getCreated() {
    return created;
  }

  /**
   * Sets the time and date this image was submitted.
   *
   * @param created the time and date this image was submitted.
   */
  public void setCreated(Date created) {
    this.created = created;
  }

  /**
   * Returns a brief description of the contents of this image.
   *
   * @return a brief description of the contents of this image.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets brief description of the contents of this image.
   *
   * @param description a brief description of the contents of this image.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Returns the url of this image.
   *
   * @return the url this image
   */
  public String getUrl() {
    return url;
  }

  /**
   * Sets the url of this image.
   *
   * @param url the url this image
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * Returns a boolean if the image is approved or not.
   *
   * @return the boolean approved
   */
  public boolean isApproved() {
    return approved;
  }

  /**
   * Sets boolean approved.
   *
   * @param approved boolean
   */
  public void setApproved(boolean approved) {
    this.approved = approved;
  }

  /**
   * Gets google id of the user.
   *
   * @return the google id of the user
   */
  public String getGoogleId() {
    return googleId;
  }

  /**
   * Sets google id of the user.
   *
   * @param googleId the google id of the user
   */
  public void setGoogleId(String googleId) {
    this.googleId = googleId;
  }

  public String getUserPictureUrl() {
    return userPictureUrl;
  }

  public void setUserPictureUrl(String userPictureUrl) {
    this.userPictureUrl = userPictureUrl;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

}
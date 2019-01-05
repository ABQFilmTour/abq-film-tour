package edu.cnm.deepdive.abq_film_tour.model.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import java.util.Date;
import java.util.UUID;

/**
 * This class represents a comment associated with a film location.
 */
@Entity(
    foreignKeys =  {
        @ForeignKey(entity = FilmLocation.class, parentColumns = "filmLocationId",
            childColumns = "filmLocationId", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = User.class, parentColumns = "userId",
            childColumns = "userId", onDelete = ForeignKey.CASCADE)
    }
)
public class UserComment {

  /**
   * The UUID of this comment on the server.
   */
  @PrimaryKey(autoGenerate = false)
  @Expose
  private UUID id;

  /**
   * The timestamp of when this comment was submitted.
   */
  private Date created;

  /**
   * The location associated with this comment.
   */
  @Expose
  private FilmLocation filmLocation;

  /**
   * The user who submitted this comment.
   */
  @Expose
  private User user;

  /**
   * The text of this comment, maximum of 4096 characters.
   */
  @Expose
  private String text;

  /**
   * Flag to verify that a comment has been approved by an admin and can be displayed.
   */
  @Expose
  private boolean approved;

  /**
   * The Google ID of the user who submitted this comment.
   */
  private String googleId;

  /**
   * Returns the UUID of this comment on the server.
   *
   * @return the UUID of this comment on the server
   */
  public UUID getId() {
    return id;
  }

  /**
   * Sets the UUID of this comment on the server.
   *
   * @param id the UUID of this comment on the server
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Returns the location associated with this comment.
   *
   * @return the location associated with this comment.
   */
  public FilmLocation getFilmLocation() {
    return filmLocation;
  }

  /**
   * Sets the location associated with this comment.
   *
   * @param filmLocation the location associated with this comment
   */
  public void setFilmLocation(FilmLocation filmLocation) {
    this.filmLocation = filmLocation;
  }

  /**
   * Returns the user who submitted this comment.
   *
   * @return the user who submitted this comment
   */
  public User getUser() {
    return user;
  }

  /**
   * Sets the user who submitted this comment.
   *
   * @param user the user who submitted this comment
   */
  public void setUser(User user) {
    this.user = user;
  }

  /**
   * Returns the timestamp of when this comment was submitted.
   *
   * @return the timestamp of when this comment was submitted
   */
  public Date getCreated() {
    return created;
  }

  /**
   * Sets the timestamp of when this comment was submitted.
   *
   * @param created the timestamp of when this comment was submitted
   */
  public void setCreated(Date created) {
    this.created = created;
  }

  /**
   * The text of this comment.
   *
   * @return the text of this comment
   */
  public String getText() {
    return text;
  }

  /**
   * The text of this comment.
   *
   * @param text the text of this comment
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * Returns boolean flag to verify that a comment has been approved by an admin and can be displayed.
   *
   * @return the boolean approved flag
   */
  public boolean isApproved() {
    return approved;
  }

  /**
   * Sets boolean flag to verify that a comment has been approved by an admin and can be displayed.
   *
   * @param approved the approved boolean flag
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
}
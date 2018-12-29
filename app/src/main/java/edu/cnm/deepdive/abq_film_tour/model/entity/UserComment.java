package edu.cnm.deepdive.abq_film_tour.model.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import java.util.Date;
import java.util.UUID;

/**
 * A comment associated with a film location.
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
  @Expose
  private Date created;

  /**
   * The location this comment is associated with.
   */
  @Expose
  private FilmLocation filmLocation;

  /**
   * The user who submitted this comment.
   */
  @Expose
  private User user;

  /**
   * The text content of this comment, maximum of 4096 characters.
   */
  @Expose
  private String text;

  /**
   * Flag to verify that a comment has been approved by an admin and can be displayed if security
   * is tightened. Probably unnecessary for now, but better to have if we implement later.
   */
  @Expose
  private boolean approved;

  /**
   * The Google ID of the user who submitted this comment.
   */
  private String googleId;

  /**
   * Returns the UUID of this comment on the server.
   * @return the UUID of this comment on the server.
   */
  public UUID getId() {
    return id;
  }

  /**
   * Sets the UUID of this comment on the server.
   * @param id the UUID of this comment on the server.
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Returns the location this comment is associated with.
   * @return the location this comment is associated with.
   */
  public FilmLocation getFilmLocation() {
    return filmLocation;
  }

  /**
   * Sets the location this comment is associated with.
   * @param filmLocation the location this comment is associated with.
   */
  public void setFilmLocation(FilmLocation filmLocation) {
    this.filmLocation = filmLocation;
  }

  /**
   * Returns the user who submitted this comment.
   * @return the user who submitted this comment.
   */
  public User getUser() {
    return user;
  }

  /**
   * Sets the user who submitted this comment.
   * @param user the user who submitted this comment.
   */
  public void setUser(User user) {
    this.user = user;
  }

  /**
   * Returns the timestamp of when this comment was submitted.
   * @return the timestamp of when this comment was submitted.
   */
  public Date getCreated() {
    return created;
  }

  /**
   * Sets the timestamp of when this comment was submitted.
   * @param created the timestamp of when this comment was submitted.
   */
  public void setCreated(Date created) {
    this.created = created;
  }

  /**
   * The text content of this comment.
   * @return the text content of this comment.
   */
  public String getText() {
    return text;
  }

  /**
   * The text content of this comment.
   * @param text the text content of this comment.
   */
  public void setText(String text) {
    this.text = text;
  }

  public boolean isApproved() {
    return approved;
  }

  public void setApproved(boolean approved) {
    this.approved = approved;
  }
}
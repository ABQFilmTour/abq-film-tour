package edu.cnm.deepdive.abq_film_tour.model.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import java.util.UUID;

/**
 * This class represents a user to submit data, tied to a Google account.
 */
@Entity
public class User {

  /**
   * The UUID associated with this user.
   */
  @PrimaryKey(autoGenerate = false)
  @Expose
  private UUID id;

  /**
   * The name on the user's Google account.
   */
  @Expose
  private String googleName;

  /**
   * The ID associated with the user's Google account.
   */
  @Expose
  private String googleId;

  /**
   * The user's Gmail address.
   */
  @Expose
  private String gmailAddress;

  /**
   * Returns the UUID associated with this user.
   * @return the UUID associated with this user.
   */
  public UUID getId() {
    return id;
  }

  /**
   * Sets the UUID associated with this user.
   * @param id the UUID associated with this user.
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Returns the name on the user's Google account.
   * @return the name on the user's Google account.
   */
  public String getGoogleName() {
    return googleName;
  }

  /**
   * Sets the name on the user's Google account.
   * @param googleName the name on the user's Google account.
   */
  public void setGoogleName(String googleName) {
    this.googleName = googleName;
  }

  /**
   * Returns the ID associated with the user's Google account.
   * @return the ID associated with the user's Google account.
   */
  public String getGoogleId() {
    return googleId;
  }

  /**
   * Sets the ID associated with the user's Google account.
   * @param googleId the ID associated with the user's Google account.
   */
  public void setGoogleId(String googleId) {
    this.googleId = googleId;
  }

  /**
   * Returns the user's Gmail address.
   * @return the user's Gmail address.
   */
  public String getGmailAddress() {
    return gmailAddress;
  }

  /**
   * Sets the user's Gmail address.
   * @param gmailAddress the user's Gmail address.
   */
  public void setGmailAddress(String gmailAddress) {
    this.gmailAddress = gmailAddress;
  }

}

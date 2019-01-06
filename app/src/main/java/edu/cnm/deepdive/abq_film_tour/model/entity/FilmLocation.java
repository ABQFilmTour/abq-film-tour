package edu.cnm.deepdive.abq_film_tour.model.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.UUID;

/**
 * This entity represents an individual film location. Its primary attributes are the site name and
 * the latitude and longitude coordinates, as well as the user and the production it is associated with. Some
 * fields overlap with fields in the associated production as they were included with the city's
 * data. In the event of a conflict the Production's information will take priority.
 */
@Entity(    foreignKeys =  {
    @ForeignKey(entity = User.class, parentColumns = "user_id",
        childColumns = "user_id", onDelete = ForeignKey.CASCADE),
    @ForeignKey(entity = Production.class, parentColumns = "production_id",
    childColumns = "production_id", onDelete = ForeignKey.CASCADE)
})
public class FilmLocation {

  /**
   * The UUID id of the location as it was received from the backend server.
   */
  @Expose
  private UUID id;

  /**
   * The date and time this location was submitted to the database.
   */
  @Expose
  private Date created;

  /**
   * The longitude coordinate for the location.
   */
  @Expose
  private String longCoordinate;

  /**
   * The latitude coordinate for the location.
   */
  @Expose
  private String latCoordinate;

  /**
   * The name of this location.
   */
  @Expose
  private String siteName;

  /**
   * A 7 digit ID prefixed with "tt" to correspond with the production this location was filmed for
   * on IMDb.
   */
  @Expose
  private String imdbId;

  /**
   * The title of the production associated with this entity.
   */
  @Expose
  private String title;

  /**
   * The type of the production associated with this entity.
   */
  @Expose
  private String type;

  /**
   * The address where this entity is located.
   */
  @Expose
  private String address;

  /**
   * The date this entity was shot, in epoch date format.
   */
  @Expose
  private long shootDate;

  /**
   * Information about the shoot as provided by the city of Albuquerque.
   */
  @Expose
  private String originalDetails;

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
   * The production associated with the filming at this location.
   */
  @Expose
  private Production production;

  /**
   * Flag to verify that a location has been approved by an admin and can be displayed if security
   * is tightened.
   */
  @Expose
  private boolean approved;

  /**
   * Indicates whether or not the user has bookmarked this location.
   */
  private boolean bookmarked;

  /**
   * Empty constructor for ROOM if we decide to implement a cached ROOM database.
   */
  public FilmLocation() {

  }

  /**
   * Retrieves the UUID of the location.
   *
   * @return UUID
   */
  public UUID getId() {
    return id;
  }

  /**
   * Sets the UUID of the location.
   *
   * @param id the UUID.
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Returns the name of the location.
   *
   * @return the name of the location.
   */
  public String getSiteName() {
    return siteName;
  }

  /**
   * Sets the name of the location.
   *
   * @param siteName the name of the location.
   */
  public void setSiteName(String siteName) {
    this.siteName = siteName;
  }

  /**
   * Returns the IMDb ID of the production associated with this location.
   *
   * @return the IMDb ID of the production associated with this location.
   */
  public String getImdbid() {
    return imdbId;
  }

  /**
   * Sets the IMDb id of the production associated with this location.
   *
   * @param imdbid the IMDb ID of the production associated with this location.
   */
  public void setImdbid(String imdbid) {
    this.imdbId = imdbid;
  }

  /**
   * Returns the production associated with this location.
   *
   * @return the production associated with this location.
   */
  public Production getProduction() {
    return production;
  }

  /**
   * Sets the production associated with this location.
   *
   * @param production the production associated with this location.
   */
  public void setProduction(Production production) {
    this.production = production;
  }

  /**
   * Returns the title of the production associated with this location.
   *
   * @return the title of the production associated with this location.
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the title of the production associated with this location.
   *
   * @param title the production associated with this location.
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Returns the type of the production associated with this location.
   *
   * @return the type of the production associated with this location.
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the type of the the production associated with this location.
   *
   * @param type the type of the the production associated with this location.
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Returns the provided address of the location.
   *
   * @return the provided address of the location.
   */
  public String getAddress() {
    return address;
  }

  /**
   * Sets the provided address of the location.
   *
   * @param address the provided address of the location.
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Returns the date of shooting in epoch date format.
   *
   * @return the date of shooting in epoch date format.
   */
  public long getShootDate() {
    return shootDate;
  }

  /**
   * Sets the date of shooting in epoch date format.
   *
   * @param shootDate the date of shooting in epoch date format.
   */
  public void setShootDate(long shootDate) {
    this.shootDate = shootDate;
  }

  /**
   * Returns city provided information about the time of shooting.
   *
   * @return city provided information about the time of shooting.
   */
  public String getOriginalDetails() {
    return originalDetails;
  }

  /**
   * Sets information about the time of shooting.
   *
   * @param originalDetails information about the time of shooting.
   */
  public void setOriginalDetails(String originalDetails) {
    this.originalDetails = originalDetails;
  }

  /**
   * Returns the longitude coordinate of the location.
   *
   * @return the longitude coordinate of the location.
   */
  public String getLongCoordinate() {
    return longCoordinate;
  }

  /**
   * Sets the longitude coordinate of the location.
   *
   * @param longCoordinate the longitude coordinate of the location.
   */
  public void setLongCoordinate(String longCoordinate) {
    this.longCoordinate = longCoordinate;
  }

  /**
   * Return the latitude coordinate of the location.
   *
   * @return the latitude coordinate of the location.
   */
  public String getLatCoordinate() {
    return latCoordinate;
  }

  /**
   * Sets the latitude coordinate of the location.
   *
   * @param latCoordinate the latitude coordinate of the location.
   */
  public void setLatCoordinate(String latCoordinate) {
    this.latCoordinate = latCoordinate;
  }

  /**
   * Is approved boolean.
   *
   * @return the boolean
   */
  public boolean isApproved() {
    return approved;
  }

  /**
   * Sets approved.
   *
   * @param approved the approved
   */
  public void setApproved(boolean approved) {
    this.approved = approved;
  }

  /**
   * Is bookmarked boolean.
   *
   * @return the boolean
   */
  public boolean isBookmarked() {
    return bookmarked;
  }

  /**
   * Sets bookmarked.
   *
   * @param bookmarked the bookmarked
   */
  public void setBookmarked(boolean bookmarked) {
    this.bookmarked = bookmarked;
  }

  /**
   * Toggle bookmarked.
   */
  public void toggleBookmarked() {
    this.bookmarked = !this.bookmarked;
  }

  /**
   * Gets google id.
   *
   * @return the google id
   */
  public String getGoogleId() {
    return googleId;
  }

  /**
   * Sets google id.
   *
   * @param googleId the google id
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
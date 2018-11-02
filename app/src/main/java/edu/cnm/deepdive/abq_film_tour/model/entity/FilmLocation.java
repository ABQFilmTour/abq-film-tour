package edu.cnm.deepdive.abq_film_tour.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(    foreignKeys =  {
    @ForeignKey(entity = User.class, parentColumns = "user_id",
        childColumns = "user_id", onDelete = ForeignKey.CASCADE)
})
public class FilmLocation {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "film_location_id")
  private long id;

  //These fields exist in the city data but are critical to the entity and should be the minimum
  //requirements for submitted data.
  @NonNull
  @ColumnInfo(name = "long_coor")
  private double longCoordinate;
  @NonNull
  @ColumnInfo(name = "lat_coor")
  private double latCoordinate;

  //Even if a location may not necessarily have a site name, it probably should be required if this
  //data will be displayed in a a table.
  @NonNull
  @ColumnInfo(name = "site_name")
  private String siteName;

  //Film and series listed on imdb are 7 digit ids prefixed with "tt".
  //can be accessed with www.omdbapi.com/
  @NonNull
  @ColumnInfo(name = "imdb_id")
  private String imdbid;

  //These fields exist in the city data but can be pulled from the imdb ID and may not be necessary.
  private String title;
  private String type;

  //These fields exist in the city data and may be useful, perhaps could be mentioned in a comment,
  // but do not seem critically important.
  private String address;
  @ColumnInfo(name = "shoot_date")
  private long ShootDate;
  @ColumnInfo(name = "original_details")
  private String originalDetails;

  public FilmLocation(String siteName, double longCoordinate, double latCoordinate) {
    this.siteName = siteName;
    this.longCoordinate = longCoordinate;
    this.latCoordinate = latCoordinate;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getSiteName() {
    return siteName;
  }

  public void setSiteName(String siteName) {
    this.siteName = siteName;
  }

  public String getImdbid() {
    return imdbid;
  }

  public void setImdbid(String imdbid) {
    this.imdbid = imdbid;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public long getShootDate() {
    return ShootDate;
  }

  public void setShootDate(long shootDate) {
    ShootDate = shootDate;
  }

  public String getOriginalDetails() {
    return originalDetails;
  }

  public void setOriginalDetails(String originalDetails) {
    this.originalDetails = originalDetails;
  }

  public double getLongCoordinate() {
    return longCoordinate;
  }

  public void setLongCoordinate(double longCoordinate) {
    this.longCoordinate = longCoordinate;
  }

  public double getLatCoordinate() {
    return latCoordinate;
  }

  public void setLatCoordinate(double latCoordinate) {
    this.latCoordinate = latCoordinate;
  }

}

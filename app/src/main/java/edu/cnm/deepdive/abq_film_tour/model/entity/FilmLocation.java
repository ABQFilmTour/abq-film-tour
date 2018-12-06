package edu.cnm.deepdive.abq_film_tour.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(    foreignKeys =  {
    @ForeignKey(entity = User.class, parentColumns = "user_id",
        childColumns = "user_id", onDelete = ForeignKey.CASCADE),
    @ForeignKey(entity = Production.class, parentColumns = "production_id",
    childColumns = "production_id", onDelete = ForeignKey.CASCADE)
})
public class FilmLocation {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "film_location_id")
  private long id;

  //These fields exist in the city data but are critical to the entity and should be the minimum
  //requirements for submitted data.

  @Expose
  private String longCoordinate;

  @Expose
  private String latCoordinate;

  //Even if a location may not necessarily have a site name, it probably should be required if this
  //data will be displayed in a a table.
  @Expose
  private String siteName;

  //Film and series listed on imdb are 7 digit ids prefixed with "tt".
  //can be accessed with www.omdbapi.com/
  @SerializedName("imdbId")
  @Expose
  private String imdbID;

  //These fields exist in the city data but can be pulled from the imdb ID and may not be necessary.
  @Expose
  private String title;

  @Expose
  private String type;

  //These fields exist in the city data and may be useful, perhaps could be mentioned in a comment,
  // but do not seem critically important.
  @Expose
  private String address;
  @Expose
  private long shootDate;
  @Expose
  private String originalDetails;

  @Expose
  @SerializedName("googleUser")
  private User user;

  @Expose
  private Production production;

  public FilmLocation() {

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
    return imdbID;
  }

  public void setImdbid(String imdbid) {
    this.imdbID = imdbid;
  }

  public Production getProduction() {
    return production;
  }

  public void setProduction(Production production) {
    this.production = production;
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
    return shootDate;
  }

  public void setShootDate(long shootDate) {
    this.shootDate = shootDate;
  }

  public String getOriginalDetails() {
    return originalDetails;
  }

  public void setOriginalDetails(String originalDetails) {
    this.originalDetails = originalDetails;
  }

  public String getLongCoordinate() {
    return longCoordinate;
  }

  public void setLongCoordinate(String longCoordinate) {
    this.longCoordinate = longCoordinate;
  }

  public String getLatCoordinate() {
    return latCoordinate;
  }

  public void setLatCoordinate(String latCoordinate) {
    this.latCoordinate = latCoordinate;
  }

}

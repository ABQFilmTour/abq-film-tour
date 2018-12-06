package edu.cnm.deepdive.abq_film_tour.model.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import edu.cnm.deepdive.abq_film_tour.model.entity.FilmLocation;
import java.util.Date;
import java.util.UUID;

@Entity(
    foreignKeys = {
        @ForeignKey(entity = FilmLocation.class, parentColumns = "film_location_id",
            childColumns = "film_location_id", onDelete = ForeignKey.CASCADE)
    }
)
public class Production {

  @Expose
  //"movie" or "series"
  private String type;

  @Expose
  private String releaseYear;

  //@PrimaryKey(autoGenerate = true)
  //@ColumnInfo(name="production_id")
  @Expose
  private String id;

  @Expose
  private String title;

  @Expose
  private String imdbID;

  @Expose
  private String plot;

  public String getReleaseYear() {
    return releaseYear;
  }

  public void setReleaseYear(String releaseYear) {
    this.releaseYear = releaseYear;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getImdbID() {
    return imdbID;
  }

  public void setImdbID(String imdbID) {
    this.imdbID = imdbID;
  }

  public String getPlot() {
    return plot;
  }

  public void setPlot(String plot) {
    this.plot = plot;
  }

}


package edu.cnm.deepdive.abq_film_tour.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import edu.cnm.deepdive.abq_film_tour.model.entity.FilmLocation;
import edu.cnm.deepdive.abq_film_tour.model.entity.User;
import java.util.Date;
import java.util.UUID;

@Entity(
    foreignKeys = {
        @ForeignKey(entity = FilmLocation.class, parentColumns = "film_location_id",
            childColumns = "film_location_id", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = User.class, parentColumns = "user_id",
            childColumns = "user_id", onDelete = ForeignKey.CASCADE)
    }
)
public class Image {

  @Expose
  private UUID id;

  @Expose
  private FilmLocation filmLocation;

  @Expose
  private User user;

  @Expose
  private Date created;

  @Expose
  private String description;

  @Expose
  private String url;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public FilmLocation getFilmLocation() {
    return filmLocation;
  }

  public void setFilmLocation(FilmLocation filmLocation) {
    this.filmLocation = filmLocation;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
package edu.cnm.deepdive.abq_film_tour.model.entity;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import com.google.gson.annotations.Expose;
import java.util.Date;
import java.util.UUID;

@Entity(
    foreignKeys =  {
        @ForeignKey(entity = FilmLocation.class, parentColumns = "filmLocationId",
            childColumns = "filmLocationId", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = User.class, parentColumns = "userId",
            childColumns = "userId", onDelete = ForeignKey.CASCADE)
    }
)
public class UserComment {

  @PrimaryKey(autoGenerate = false)
  @Expose
  private UUID id;

  @Expose
  private Date created;

  @Expose
  private FilmLocation filmLocation;

  @Expose
  private User user;

  @Expose
  private String text;

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

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}

package edu.cnm.deepdive.abq_film_tour.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import edu.cnm.deepdive.abq_film_tour.model.entity.FilmLocation;
import edu.cnm.deepdive.abq_film_tour.model.entity.User;

@Entity(
    foreignKeys = {
        @ForeignKey(entity = FilmLocation.class, parentColumns = "film_location_id",
            childColumns = "film_location_id", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = User.class, parentColumns = "user_id",
            childColumns = "user_id", onDelete = ForeignKey.CASCADE)
    }
)
public class Image {

  @ColumnInfo(name = "image_id")
  @PrimaryKey(autoGenerate = true)
  private long id;

  @ColumnInfo(name = "film_location_id")
  private long filmLocationId;


  @ColumnInfo(name = "user_id")
  private long userId;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getFilm_location_id() {
    return filmLocationId;
  }

  public void setFilm_location_id(long filmLocationId) {
    this.filmLocationId = filmLocationId;
  }

  public long getUser_id() {
    return userId;
  }

  public void setUser_id(long userId) {
    this.userId = userId;
  }
}


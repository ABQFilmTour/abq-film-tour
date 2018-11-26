package edu.cnm.deepdive.abq_film_tour.model.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import edu.cnm.deepdive.abq_film_tour.model.entity.FilmLocation;
import edu.cnm.deepdive.abq_film_tour.model.entity.User;
import java.util.Date;

@Entity(
    foreignKeys =  {
        @ForeignKey(entity = FilmLocation.class, parentColumns = "filmLocationId",
            childColumns = "filmLocationId", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = User.class, parentColumns = "userId",
            childColumns = "userId", onDelete = ForeignKey.CASCADE)
    }


)
public class UserComment {


  @ColumnInfo(name = "comments_id")
  @PrimaryKey(autoGenerate = true)
  private long id;

  @ColumnInfo(name = "film_location_id")
  private long filmLocationId;

  @ColumnInfo(name = "user_id")
  private long userId;

  @NonNull
  @ColumnInfo(index = true)
  private Date timestamp = new Date();

  @ColumnInfo(name = "content")
  private String content;

  @NonNull
  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(@NonNull Date timestamp) {
    this.timestamp = timestamp;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getFilmLocationId() {
    return filmLocationId;
  }

  public void setFilmLocationId(long filmLocationId) {
    this.filmLocationId = filmLocationId;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}

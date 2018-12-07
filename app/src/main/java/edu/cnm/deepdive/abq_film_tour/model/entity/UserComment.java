package edu.cnm.deepdive.abq_film_tour.model.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import com.google.gson.annotations.Expose;
import edu.cnm.deepdive.abq_film_tour.model.entity.FilmLocation;
import edu.cnm.deepdive.abq_film_tour.model.entity.User;
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

  @PrimaryKey(autoGenerate = true)
  private UUID id;

  @ColumnInfo(name = "film_location_id")
  private UUID filmLocationId;

  @ColumnInfo(name = "user_id")
  private UUID userId;

  @Expose
  private Date timestamp = new Date();

  private String content;

  @NonNull
  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(@NonNull Date timestamp) {
    this.timestamp = timestamp;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getFilmLocationId() {
    return filmLocationId;
  }

  public void setFilmLocationId(UUID filmLocationId) {
    this.filmLocationId = filmLocationId;
  }

  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}

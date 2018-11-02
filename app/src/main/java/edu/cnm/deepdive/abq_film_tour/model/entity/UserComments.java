package edu.cnm.deepdive.abq_film_tour.model.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(
    foreignKeys =  {
        @ForeignKey(entity = FilmLocation.class, parentColumns = "film_location_id",
            childColumns = "film_location_id", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = User.class, parentColumns = "user_id",
            childColumns = "user_id", onDelete = ForeignKey.CASCADE)
    }


)
public class UserComments {


  @ColumnInfo(name = "comments_id")
  @PrimaryKey(autoGenerate = true)
  private long id;

  @ColumnInfo(name = "film_location_id")
  private long film_location_id;

  @ColumnInfo(name = "user_id")
  private long user_id;

  private String content;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getFilm_location_id() {
    return film_location_id;
  }

  public void setFilm_location_id(long film_location_id) {
    this.film_location_id = film_location_id;
  }

  public long getUser_id() {
    return user_id;
  }

  public void setUser_id(long user_id) {
    this.user_id = user_id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}

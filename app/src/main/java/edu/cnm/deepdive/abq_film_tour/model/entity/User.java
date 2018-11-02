package edu.cnm.deepdive.abq_film_tour.model.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class User {

  @ColumnInfo(name = "user_id")
  @PrimaryKey(autoGenerate = true)
  private long id;

  private long login;

  private long coorLong;

  private long coorLat;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getLogin() {
    return login;
  }

  public void setLogin(long login) {
    this.login = login;
  }

  public long getCoorLong() {
    return coorLong;
  }

  public void setCoorLong(long coorLong) {
    this.coorLong = coorLong;
  }

  public long getCoorLat() {
    return coorLat;
  }

  public void setCoorLat(long coorLat) {
    this.coorLat = coorLat;
  }
}

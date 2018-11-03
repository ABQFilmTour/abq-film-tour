package edu.cnm.deepdive.abq_film_tour.model.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class User {

  @ColumnInfo(name = "user_id")
  @PrimaryKey(autoGenerate = true)
  private long id;

  private String login;

  private double coorLong;

  private double coorLat;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public double getCoorLong() {
    return coorLong;
  }

  public void setCoorLong(double coorLong) {
    this.coorLong = coorLong;
  }

  public double getCoorLat() {
    return coorLat;
  }

  public void setCoorLat(double coorLat) {
    this.coorLat = coorLat;
  }
}

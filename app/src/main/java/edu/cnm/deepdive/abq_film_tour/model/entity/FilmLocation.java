package edu.cnm.deepdive.abq_film_tour.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class FilmLocation {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "film_location_id")
  private long id;

  private String title;

  private double longCoordinate;

  private double latCoordinate;

  public FilmLocation(String title, double longCoordinate, double latCoordinate) {
    this.title = title;
    this.longCoordinate = longCoordinate;
    this.latCoordinate = latCoordinate;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public double getLongCoordinate() {
    return longCoordinate;
  }

  public void setLongCoordinate(double longCoordinate) {
    this.longCoordinate = longCoordinate;
  }

  public double getLatCoordinate() {
    return latCoordinate;
  }

  public void setLatCoordinate(double latCoordinate) {
    this.latCoordinate = latCoordinate;
  }

}

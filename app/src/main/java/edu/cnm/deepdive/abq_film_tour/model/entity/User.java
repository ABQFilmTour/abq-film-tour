package edu.cnm.deepdive.abq_film_tour.model.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class User {

  @ColumnInfo(name = "user_id")
  @PrimaryKey(autoGenerate = true)
  private long id;

  private String googleName;

  private String googleId;

  private String gmailAddress;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getGoogleName() {
    return googleName;
  }

  public void setGoogleName(String googleName) {
    this.googleName = googleName;
  }

  public String getGoogleId() {
    return googleId;
  }

  public void setGoogleId(String googleId) {
    this.googleId = googleId;
  }

  public String getGmailAddress() {
    return gmailAddress;
  }

  public void setGmailAddress(String gmailAddress) {
    this.gmailAddress = gmailAddress;
  }
}

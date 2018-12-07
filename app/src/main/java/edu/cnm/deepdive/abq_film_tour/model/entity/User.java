package edu.cnm.deepdive.abq_film_tour.model.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import java.util.UUID;

@Entity
public class User {

  @PrimaryKey(autoGenerate = false)
  @Expose
  private UUID id;

  @Expose
  private String googleName;

  @Expose
  private String googleId;

  @Expose
  private String gmailAddress;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
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

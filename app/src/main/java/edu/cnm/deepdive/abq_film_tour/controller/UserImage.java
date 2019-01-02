package edu.cnm.deepdive.abq_film_tour.controller;

public class UserImage {

  private String UserName;
  private String Description;
  private int Thumbnail;

  public UserImage() {
  }

  public UserImage(String userName, String description, int imageThumbnail) {
    UserName = userName;
    Description = description;
    Thumbnail = imageThumbnail;
  }

  public String getUserName() {
    return UserName;
  }

  public String getDescription() {
    return Description;
  }

  public int getThumbnail() {
    return Thumbnail;
  }

  public void setUserName(String userName) {
    UserName = userName;
  }

  public void setDescription(String description) {
    Description = description;
  }

  public void setThumbnail(int thumbnail) {
    Thumbnail = thumbnail;
  }
}


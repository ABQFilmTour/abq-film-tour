package edu.cnm.deepdive.abq_film_tour.model.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Production {


  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name="production_id")
  private long id;
}

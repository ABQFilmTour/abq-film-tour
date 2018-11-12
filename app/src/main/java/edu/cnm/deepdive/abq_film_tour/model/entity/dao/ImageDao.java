package edu.cnm.deepdive.abq_film_tour.model.entity.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import edu.cnm.deepdive.abq_film_tour.model.entity.Image;
import java.util.List;

@Dao
public interface ImageDao {

  @Insert(onConflict = OnConflictStrategy.FAIL)
  Long insert(Image image);

  @Query("SELECT * FROM Image WHERE film_location_id = :filmLocationId ORDER BY film_location_id")
  List<Image> selectByLocation(long filmLocationId);

  @Query("SELECT *FROM UserComments WHERE user_id = :userId ORDER BY timestamp")
  List<Image> selectByUser(long userId);

  @Delete
  int delete(Image image);
}

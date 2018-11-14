package edu.cnm.deepdive.abq_film_tour.model.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import edu.cnm.deepdive.abq_film_tour.model.entity.UserComment;
import java.util.List;

@Dao
public interface UserCommentsDao {

  @Insert(onConflict = OnConflictStrategy.FAIL)
  Long insert (UserComment userComment);

  @Query("SELECT * FROM UserComment WHERE film_location_id = :filmLocationId ORDER BY timestamp")
  List<UserComment> selectByLocation(long filmLocationId);

  @Query("SELECT *FROM UserComment WHERE user_id = :userId ORDER BY timestamp")
  List<UserComment> selectByUser(long userId);

  @Delete
  int delete(UserComment userComment);
}

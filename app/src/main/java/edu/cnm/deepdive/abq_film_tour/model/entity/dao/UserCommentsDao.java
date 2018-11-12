package edu.cnm.deepdive.abq_film_tour.model.entity.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import edu.cnm.deepdive.abq_film_tour.model.entity.UserComments;
import java.util.List;

@Dao
public interface UserCommentsDao {

  @Insert(onConflict = OnConflictStrategy.FAIL)
  Long insert (UserComments userComments);

  @Query("SELECT * FROM UserComments WHERE film_location_id = :filmLocationId ORDER BY timestamp")
  List<UserComments> selectByLocation(long filmLocationId);

  @Query("SELECT *FROM UserComments WHERE user_id = :userId ORDER BY timestamp")
  List<UserComments> selectByUser(long userId);

  @Delete
  int delete(UserComments userComments);
}

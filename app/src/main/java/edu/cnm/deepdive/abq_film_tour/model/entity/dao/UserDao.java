package edu.cnm.deepdive.abq_film_tour.model.entity.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import edu.cnm.deepdive.abq_film_tour.model.entity.User;
import java.util.List;

@Dao
public interface UserDao {

  @Insert(onConflict = OnConflictStrategy.FAIL)
  long insert(User user);

  @Query("SELECT * FROM User ORDER BY user_id")
  List<User> select();
}

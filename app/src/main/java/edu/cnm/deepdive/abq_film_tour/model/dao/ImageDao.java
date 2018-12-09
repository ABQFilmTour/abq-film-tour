package edu.cnm.deepdive.abq_film_tour.model.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import edu.cnm.deepdive.abq_film_tour.model.entity.Image;
import java.util.List;

//TODO outdated code. Needs fixing.

/**
 * The interface Image dao.
 */
@Dao
public interface ImageDao {

  /**
   * Insert long.
   *
   * @param image the image
   * @return the long
   */
  @Insert(onConflict = OnConflictStrategy.FAIL)
  Long insert(Image image);

  /**
   * List of film location ids selected by location
   *
   * @param filmLocationId
   * @return the list of film location ids selected by location
   */
  @Query("SELECT * FROM Image WHERE film_location_id = :filmLocationId ORDER BY film_location_id")
  List<Image> selectByLocation(long filmLocationId);

  /**
   * List of comments selected by user
   *
   * @param userId the user id
   * @return the list
   */
  @Query("SELECT *FROM UserComment WHERE user_id = :userId ORDER BY timestamp")
  List<Image> selectByUser(long userId);

  /**
   * Delete int.
   *
   * @param image the image
   * @return the int
   */
  @Delete
  int delete(Image image);
}

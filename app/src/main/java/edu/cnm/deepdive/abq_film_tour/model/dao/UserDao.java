package edu.cnm.deepdive.abq_film_tour.model.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import edu.cnm.deepdive.abq_film_tour.model.entity.User;
import java.util.List;

/**
 * This will be the DAO for users if we decide to implement a cached ROOM database.
 */
@Dao
public interface UserDao {

}

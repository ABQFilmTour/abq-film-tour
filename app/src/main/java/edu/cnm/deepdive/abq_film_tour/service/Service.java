package edu.cnm.deepdive.abq_film_tour.service;

import edu.cnm.deepdive.abq_film_tour.model.entity.FilmLocation;
import edu.cnm.deepdive.abq_film_tour.model.entity.Production;
import edu.cnm.deepdive.abq_film_tour.model.entity.User;
import edu.cnm.deepdive.abq_film_tour.model.entity.UserComment;
import java.util.List;
import java.util.UUID;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * The Service interface to communicate with the backend.
 */
public interface Service {

  /**
   * Gets film location.
   *
   * @param id the film location id
   * @return the film location
   */
  @GET("rest/film_locations/{id}/")
  Call<FilmLocation> getFilmLocation(@Header("Authorization") String authorization, @Path(value = "id") UUID id);

  /**
   * Gets list of locations.
   *
   * @return the locations
   */
  @GET("rest/film_locations/")
  Call<List<FilmLocation>> getLocations(@Header("Authorization") String authorization);

  /**
   * Gets list of productions.
   *
   * @return the productions
   */
  @GET("rest/productions/")
  Call<List<Production>> getProductions(@Header("Authorization") String authorization);

  @GET("rest/productions/{id}/")
  Call<String> getPosterUrl(@Header("Authorization") String authorization, @Path(value = "id") UUID id);

  /**
   * Gets user comments.
   *
   * @param id the id of the film location
   * @return the comments
   */
  @GET("rest/film_locations/{id}/user_comments/")
  Call<List<UserComment>> getComments(@Header("Authorization") String authorization, @Path(value = "id") UUID id);

  /**
   * Gets users.
   *
   * @return the users
   */
  @GET("rest/users/")
  Call<List<User>> getUsers(@Header("Authorization") String authorization);

//  @POST("rest/film_locations/")

}
package edu.cnm.deepdive.abq_film_tour.service;

import android.graphics.Bitmap;
import edu.cnm.deepdive.abq_film_tour.model.entity.FilmLocation;
import edu.cnm.deepdive.abq_film_tour.model.entity.Image;
import edu.cnm.deepdive.abq_film_tour.model.entity.Production;
import edu.cnm.deepdive.abq_film_tour.model.entity.User;
import edu.cnm.deepdive.abq_film_tour.model.entity.UserComment;
import java.util.List;
import java.util.UUID;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
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
   * Gets film location associated with a film location id.
   *
   * @param authorization the authorization token
   * @param id the film location id
   * @return the film location
   */
  @GET("rest/film_locations/{id}/")
  Call<FilmLocation> getFilmLocation(@Header("Authorization") String authorization, @Path(value = "id") UUID id);

  /**
   * Gets list of film locations.
   *
   * @param authorization the authorization token
   * @return the film locations
   */
  @GET("rest/film_locations/")
  Call<List<FilmLocation>> getLocations(@Header("Authorization") String authorization);

  /**
   * Gets list of productions.
   *
   * @param authorization the authorization token
   * @return the productions
   */
  @GET("rest/productions/")
  Call<List<Production>> getProductions(@Header("Authorization") String authorization);

  /**
   * Gets user comments associated with a film location id.
   *
   * @param authorization the authorization token
   * @param id the id of the film location
   * @return the user comments
   */
  @GET("rest/film_locations/{id}/user_comments/")
  Call<List<UserComment>> getComments(@Header("Authorization") String authorization, @Path(value = "id") UUID id);

  /**
   * Gets images associated with a film location id.
   *
   * @param authorization the authorization token
   * @param id the id of the film location
   * @return the images
   */
  @GET("rest/film_locations/{id}/images/")
  Call<List<Image>> getImages(@Header("Authorization") String authorization, @Path(value = "id") UUID id);

  /**
   * Posts a new film location to the server.
   *
   * @param authorization the authorization token
   * @param filmLocation the film location
   */
  @POST("rest/film_locations/")
  Call<FilmLocation> postFilmLocation(@Header("Authorization") String authorization, @Body FilmLocation filmLocation);

  /**
   * Posts a new user comment to the server.
   *
   * @param authorization the authorization token
   * @param userComment the new user comment
   * @param id the id of the film location
   */
  @POST("rest/film_locations/{id}/user_comments/")
  Call<UserComment> postUserComment(@Header("Authorization") String authorization, @Body UserComment userComment, @Path(value = "id") UUID id);

  /**
   * This will post a new image entity to the server. This is under construction.
   *
   * @param authorization the authorization token
   * @param image the new image entity
   * @param id the id of the film location
   */
  @POST("rest/film_locations/{id}/images")
  Call<Image> postImage(@Header("Authorization") String authorization, @Body Image image, @Path(value = "id") UUID id);

}
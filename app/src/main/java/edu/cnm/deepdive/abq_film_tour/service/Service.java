package edu.cnm.deepdive.abq_film_tour.service;

import edu.cnm.deepdive.abq_film_tour.model.entity.FilmLocation;
import edu.cnm.deepdive.abq_film_tour.model.entity.Production;
import edu.cnm.deepdive.abq_film_tour.model.entity.User;
import edu.cnm.deepdive.abq_film_tour.model.entity.UserComment;
import java.util.List;
import java.util.UUID;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {

  @GET("rest/film_locations/{id}/")
  Call<FilmLocation> getFilmLocation(@Path(value = "id") UUID id);

  @GET("rest/film_locations/")
  Call<List<FilmLocation>> getLocations();

  @GET("rest/productions/")
  Call<List<Production>> getProductions();

  /*
  @GET("rest/film_locations/{id}/user_comments")
  Call<List<UserComment>> getCommentts(@Path(value = "id")String id ;*/


/*  @GET('rest/users')
  Call<User> getUsers(); */

}

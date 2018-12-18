package edu.cnm.deepdive.abq_film_tour.service;

import android.app.Application;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.abq_film_tour.R;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The parent application class.
 */
public class FilmTourApplication extends Application {

  private static FilmTourApplication instance = null;
  private GoogleSignInClient client;
  private GoogleSignInAccount account;
  private Gson gson;
  private Retrofit retrofit;
  private Service service;

  @Override
  public void onCreate() {
    super.onCreate();
    instance = this;
    GoogleSignInOptions options = new GoogleSignInOptions.Builder(
        GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestId()
        .requestIdToken(getString(R.string.client_id))
        .build();
    setupService();
    client = GoogleSignIn.getClient(this, options);
  }

  private void setupService() {
    gson = new GsonBuilder()
        .create();
     retrofit = new Retrofit.Builder()
        .baseUrl(getString(R.string.base_url))
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build();
    service = retrofit.create(Service.class);
//    apiKey = BuildConfig.API_KEY;
  }

  /**
   * Gets instance.
   *
   * @return the instance
   */
  public static FilmTourApplication getInstance() {
    return instance;
  }

  /**
   * Gets client.
   *
   * @return the client
   */
  public GoogleSignInClient getClient() {
    return client;
  }

  /**
   * Sets client.
   *
   * @param client the client
   */
  public void setClient(GoogleSignInClient client) {
    this.client = client;
  }

  /**
   * Gets account.
   *
   * @return the account
   */
  public GoogleSignInAccount getAccount() {
    return account;
  }

  /**
   * Sets account.
   *
   * @param account the account
   */
  public void setAccount(GoogleSignInAccount account) {
    this.account = account;
  }

  /**
   * Gets gson.
   *
   * @return the gson
   */
  public Gson getGson() {
    return gson;
  }

  /**
   * Gets retrofit.
   *
   * @return the retrofit
   */
  public Retrofit getRetrofit() {
    return retrofit;
  }

  /**
   * Gets service.
   *
   * @return the service
   */
  public Service getService() {
    return service;
  }
}

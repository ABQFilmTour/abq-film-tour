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

  public static FilmTourApplication getInstance() {
    return instance;
  }

  public GoogleSignInClient getClient() {
    return client;
  }

  public void setClient(GoogleSignInClient client) {
    this.client = client;
  }

  public GoogleSignInAccount getAccount() {
    return account;
  }

  public void setAccount(GoogleSignInAccount account) {
    this.account = account;
  }

  public Gson getGson() {
    return gson;
  }

  public Retrofit getRetrofit() {
    return retrofit;
  }

  public Service getService() {
    return service;
  }
}

package edu.cnm.deepdive.abq_film_tour.service;

import android.app.Application;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class FilmTourApplication extends Application {

  private static FilmTourApplication instance = null;

  private GoogleSignInClient client;
  private GoogleSignInAccount account;

  @Override
  public void onCreate() {
    super.onCreate();
    instance = this;
    GoogleSignInOptions options = new GoogleSignInOptions.Builder(
        GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestId()
        .build();
    client = GoogleSignIn.getClient(this, options);
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
}

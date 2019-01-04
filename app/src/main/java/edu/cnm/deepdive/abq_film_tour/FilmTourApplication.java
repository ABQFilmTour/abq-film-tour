package edu.cnm.deepdive.abq_film_tour;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Application;
import android.content.Intent;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.abq_film_tour.controller.LoginActivity;
import edu.cnm.deepdive.abq_film_tour.service.Service;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The parent application class.
 */
public class FilmTourApplication extends Application {

  private static final int STATUS_CODE_ERROR = 1;
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

  /**
   * Creates an error message based on a given HTTP response code.
   */
  public String getErrorMessageFromHttpResponse(Response response) {
    String errorMessage = getString(R.string.error_http, response.code());
    if (response.code() == HTTP_UNAUTHORIZED) {
      errorMessage = getString(R.string.error_unauthorized);
    } else if (response.code() == HTTP_FORBIDDEN) {
      //TODO Figure out how to retrieve the error description (it contains ban info)
      errorMessage = getString(R.string.error_forbidden);
    }
    return errorMessage;
  }

  /**
   * Performs end of function routine based on a given error message.
   * @param errorMessage
   */
  public void handleErrorMessage(String errorMessage) {
    if (errorMessage.equals(getString(R.string.error_unauthorized))) {
      //Unauthorized, give the user a chance to sign back in.
      signOutWithAlertDialog(errorMessage);
    } else if (errorMessage.equals(getString(R.string.error_forbidden))) {
      //Forbidden, exit with a ban message.
      //TODO Figure out how to display the ban message provided in the error description
      exitWithAlertDialog(errorMessage);
    } else {
      //In any other possibility, exit just to be safe.
      exitWithAlertDialog(errorMessage);
    }
  }

  /**
   * Creates an alert dialog with a given error message and closes the program, used for cleaner
   * exception handling. Ideal for 403 as it explicitly tells the user to GTFO.
   *
   * @param errorMessage a String message to display to the user.
   */
  private void exitWithAlertDialog(String errorMessage) {
    AlertDialog.Builder alertDialog = new Builder(this, R.style.AlertDialog);
    alertDialog.setMessage(errorMessage)
        .setCancelable(false)
        .setPositiveButton(R.string.alert_exit, (dialog, which) -> System.exit(STATUS_CODE_ERROR));
    AlertDialog alert = alertDialog.create();
    alert.show();
  }

  /**
   * This method signs the Google account out of the application and returns the user to the login
   * activity.
   */
  public void signOut() {
    getClient().signOut().addOnCompleteListener((task) -> {
      setAccount(null);
      Intent intent = new Intent(this, LoginActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);
    });
  }

  /**
   * Creates an alert dialog with a given error message and signs out, used for cleaner exception
   * handling. Ideal for 401 as it invites the user to try to sign in again.
   *
   * @param errorMessage a String message to display to the user.
   */
  private void signOutWithAlertDialog(String errorMessage) {
    AlertDialog.Builder alertDialog = new Builder(this, R.style.AlertDialog);
    alertDialog.setMessage(errorMessage)
        .setCancelable(false)
        .setPositiveButton(R.string.alert_signout, (dialog, which) -> signOut());
    AlertDialog alert = alertDialog.create();
    alert.show();
  }

  private void setupService() {
    gson = new GsonBuilder()
        .create();
     retrofit = new Retrofit.Builder()
        .baseUrl(getString(R.string.base_url))
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build();
    service = retrofit.create(Service.class);
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
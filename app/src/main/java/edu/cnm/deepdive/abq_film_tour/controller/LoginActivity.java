package edu.cnm.deepdive.abq_film_tour.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import edu.cnm.deepdive.abq_film_tour.R;
import edu.cnm.deepdive.abq_film_tour.service.FilmTourApplication;


/**
 * Uses Google Sign-In to allow users to login to application.
 */
public class LoginActivity extends AppCompatActivity {

  private static final int REQUEST_CODE = 1000;

  private SignInButton signIn;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    signIn = findViewById(R.id.sign_in);
    signIn.setOnClickListener((view) -> signIn());
    if (getSupportActionBar() != null) {
      getSupportActionBar().hide();
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
    GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
    if (account != null) {
      FilmTourApplication.getInstance().setAccount(account);
      checkUser();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (requestCode == REQUEST_CODE) {
      try {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        GoogleSignInAccount account = task.getResult(ApiException.class);
        FilmTourApplication.getInstance().setAccount(account);
        checkUser();
      } catch (ApiException e) {
        Toast.makeText(this, R.string.unable_to_signin, Toast.LENGTH_LONG).show();
      }
    }
  }

  private void signIn() {
    Intent intent = FilmTourApplication.getInstance().getClient().getSignInIntent();
    startActivityForResult(intent, REQUEST_CODE);
  }

  private void switchToMain() {
    signIn.setVisibility(View.GONE);
    new Thread() {
      public void run() {
        try {
          sleep(2000);
        } catch (InterruptedException e) {
          // Do nothing
        } finally {
          Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

          startActivity(intent);

        }
      }
    }.start();
  }

  private void checkUser() {
    //TODO See if the user is already in the database and/or if they are banned
    //TODO If they are not in the database, create an entry
    //TODO If they are banned, kick their ass out
    switchToMain();
  }

}



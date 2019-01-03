package edu.cnm.deepdive.abq_film_tour.controller;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.q42.android.scrollingimageview.ScrollingImageView;
import edu.cnm.deepdive.abq_film_tour.R;
import edu.cnm.deepdive.abq_film_tour.model.entity.FilmLocation;
import edu.cnm.deepdive.abq_film_tour.model.entity.Production;
import edu.cnm.deepdive.abq_film_tour.model.entity.UserComment;
import edu.cnm.deepdive.abq_film_tour.service.FilmTourApplication;
import edu.cnm.deepdive.abq_film_tour.view.CommentAdapter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import retrofit2.Call;
import retrofit2.Response;

/**
 * The Location activity takes the location layout and passes in the {@link TextView}, {@link
 * FilmLocation}, {@link Production}, and {@link UserComment}. it extends {@link
 * AppCompatActivity#AppCompatActivity()}
 */
public class LocationActivity extends AppCompatActivity {

  private ScrollingImageView locationImage;
  private ImageView locationPosterImage;
  private TextView locationSiteName;
  private TextView locationProductionTitle;
  private TextView locationImdb;
  private TextView locationPlot;
  private TextView locationComments;
  private ListView listView;
  private FilmLocation location;
  private Production production;
  private List<UserComment> userComments;
  private String token;
  private ImageButton bookmarkButton;
  private Button yourReviewButton;
//  private Button yourImageButton;
  private SharedPreferences sharedPref;
  private Set<String> bookmarks;

  private final String LOCATION_ID_KEY = "location_id_key";
  private static final String ERROR_LOG_TAG_LOCATION_ACTIVITY = "LocationActivity";
  private static final int STATUS_CODE_ERROR = 1;
  private static final String SHARED_PREF_BOOKMARKS = "bookmarks";

  private FilmTourApplication filmTourApplication;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    //Sets up the activity and required fields
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_location);
    filmTourApplication = (FilmTourApplication) getApplication();
    token = getString(R.string.oauth2_header,
        FilmTourApplication.getInstance().getAccount().getIdToken());

    //Assigns bookmarks to set stored in SharedPref, creates a new HashSet if not available
    sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    bookmarks = sharedPref.getStringSet(SHARED_PREF_BOOKMARKS, new HashSet<>());

    //Gets info passed from the MapsActivity
    Bundle extras = getIntent().getExtras();
    assert extras != null;
    String locationID = extras.getString(LOCATION_ID_KEY);

    //Sets up layout IDs
    locationImage = findViewById(R.id.image_view_header);
    locationPosterImage = findViewById(R.id.image_view_poster);
    locationProductionTitle = findViewById(R.id.production_title_view);
    locationSiteName = findViewById(R.id.location_sitename_view);
    locationImdb = findViewById(R.id.imdb_link_view);
    locationPlot = findViewById(R.id.plot_view);
    listView = findViewById(R.id.comment_list_view);
    bookmarkButton = findViewById(R.id.bookmark_button);

    //Queries the database
    UUID locationUUID = UUID.fromString(locationID);
    new LocationTask().execute(locationUUID);

  }

  @Override
  protected void onStop() {
    super.onStop();
    //Saves bookmarks to shared preferences when activity is no longer visible.
    saveBookmarksToSharedPref(bookmarks);
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
  private void signOut() {
    FilmTourApplication app = FilmTourApplication.getInstance();
    app.getClient().signOut().addOnCompleteListener(this, (task) -> {
      app.setAccount(null);
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

  private void saveBookmarksToSharedPref(Set<String> bookmarks) {
    SharedPreferences.Editor editor = sharedPref.edit();
    editor.putStringSet(SHARED_PREF_BOOKMARKS, bookmarks);
    editor.apply();
  }

  public FilmLocation getLocation() {
    return location;
  }

  public void setLocation(FilmLocation location) {
    this.location = location;
  }

  public Production getProduction() {
    return production;
  }

  public void setProduction(Production production) {
    this.production = production;
  }

  /**
   * The Location task extends {@link AsyncTask#AsyncTask()} to grab {@link UserComment}, and {@link
   * LocationActivity} and tie them to a specific {@link FilmLocation} Returns a boolean if the
   * query was successful, displays an alert dialog and exits the app if not.
   */
  public class LocationTask extends AsyncTask<UUID, Void, Boolean> {

    private String errorMessage = getString(R.string.error_default);
    BitmapDrawable poster;

    @Override
    protected Boolean doInBackground(UUID... UUIDs) {
      boolean successfulQuery = false;
      try {
        //Retrieve the info from the server
        Call<FilmLocation> locationCall = filmTourApplication.getService()
            .getFilmLocation(token, UUIDs[0]);
        Call<List<UserComment>> commentCall = filmTourApplication.getService()
            .getComments(token, UUIDs[0]);
        Response<FilmLocation> locationCallResponse = locationCall.execute();
        Response<List<UserComment>> commentCallResponse = commentCall.execute();

        //Assign info to local values if it came through correctly
        if (locationCallResponse.isSuccessful() && commentCallResponse.isSuccessful()) {
          location = locationCallResponse.body();
          userComments = commentCallResponse.body();
          successfulQuery = true;
        } else {
          Log.d(ERROR_LOG_TAG_LOCATION_ACTIVITY, String.valueOf(locationCallResponse.code()));
          Log.d(ERROR_LOG_TAG_LOCATION_ACTIVITY, String.valueOf(commentCallResponse.code()));
          //TODO Make new error string format for both response codes
        }
      } catch (IOException e) {
        Log.d(ERROR_LOG_TAG_LOCATION_ACTIVITY, e.getMessage());
        errorMessage = getString(R.string.error_io);
      }

      return successfulQuery;
    }



    @Override
    protected void onPostExecute(Boolean successfulQuery) {
      if (successfulQuery) {
        super.onPostExecute(successfulQuery);
        //Setup basic textviews.
        production = location.getProduction();
        String locationText = location.getSiteName();
        locationSiteName.setText(locationText);
        String productionTitle = production.getTitle();
        locationProductionTitle.setText(productionTitle);
        String productionPlot = production.getPlot();
        locationPlot.setText(productionPlot);

        //Setup IMDB link
        locationImdb.setText(R.string.imdb_link);
        locationImdb.setOnClickListener(v -> {
          Uri locationImdb = Uri.parse(getString(R.string.imdb_url) + production.getImdbID());
          Intent intent = new Intent(Intent.ACTION_VIEW, locationImdb);
          startActivity(intent);
        });

        //Load poster image
        //TODO This is the "lazy method" to add an authorization header, there's a cleaner method using Modules.
        GlideUrl urlWithLazyHeader = new GlideUrl(String.format(getString(R.string.poster_url_format), getString(R.string.base_url), production.getId()),
            new LazyHeaders.Builder()
            .addHeader("Authorization", token)
            .build());
        Glide.with(LocationActivity.this)
              .load(urlWithLazyHeader)
              .into(locationPosterImage);
        //TODO Default image in case there's no poster?

        //Setup bookmark button
        bookmarkButton.setOnClickListener(v -> {
          String locationId = location.getId().toString();
          if (bookmarks.contains(locationId)) {
            bookmarks.remove(locationId);
            Toast.makeText(LocationActivity.this, "Bookmark removed.", Toast.LENGTH_SHORT).show();
          } else {
            bookmarks.add(locationId);
            Toast.makeText(LocationActivity.this, "Bookmark added.", Toast.LENGTH_SHORT).show();
          }
        });

        //Setup your review button
        yourReviewButton = findViewById(R.id.register_review_button);
        yourReviewButton.setOnClickListener(v -> {
          SubmitCommentDialog submitCommentDialog = new SubmitCommentDialog();
          submitCommentDialog.show(getSupportFragmentManager(), "whatever");
        });

        /*
        //Setup your image button
        yourImageButton = findViewById(R.id.register_image_button);
        yourImageButton.setOnClickListener(v -> {
          UploadImageDialog uploadImageDialog = new UploadImageDialog();
          uploadImageDialog.show(getSupportFragmentManager(), "whatever");
        });
        */

        //Setup comments
        ListView commentListView = findViewById(R.id.comment_list_view);
        CommentAdapter commentAdapter = new CommentAdapter(LocationActivity.this, 0, userComments);
        commentListView.setAdapter(commentAdapter);

        commentListView.setOnTouchListener(new OnTouchListener() {
          // Setting on Touch Listener for handling the touch inside ScrollView
          @Override
          public boolean onTouch(View v, MotionEvent event) {
            // Disallow the touch request for parent scroll on touch of child view
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
          }
        });

      } else if (errorMessage.equals(getString(R.string.error_unauthorized))) {
        signOutWithAlertDialog(errorMessage);
      } else {
        exitWithAlertDialog(errorMessage);
      }
    }
  }
}
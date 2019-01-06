package edu.cnm.deepdive.abq_film_tour.controller;

import static edu.cnm.deepdive.abq_film_tour.controller.MapsActivity.SHARED_PREF_BOOKMARKS;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
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
import edu.cnm.deepdive.abq_film_tour.R;
import edu.cnm.deepdive.abq_film_tour.model.entity.FilmLocation;
import edu.cnm.deepdive.abq_film_tour.model.entity.Production;
import edu.cnm.deepdive.abq_film_tour.model.entity.UserComment;
import edu.cnm.deepdive.abq_film_tour.FilmTourApplication;
import edu.cnm.deepdive.abq_film_tour.view.CommentAdapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import retrofit2.Call;
import retrofit2.Response;

/**
 * This activity displays more information about a selected location and loads when the user selects
 * the location from the map pin. The activity retrieves the information from the database on
 * loading and the user can navigate to a dialog fragment to submit new content.
 */
public class LocationActivity extends AppCompatActivity {

  //CONSTANTS
  private final String LOCATION_ID_KEY = "location_id_key";
  private static final String ERROR_LOG_TAG_LOCATION_ACTIVITY = "LocationActivity";

  //FIELDS
  private FilmTourApplication filmTourApplication;
  private ImageView locationPosterImage;
  private TextView locationSiteName;
  private TextView locationProductionTitle;
  private TextView locationImdb;
  private TextView locationPlot;
  private FilmLocation location;
  private Production production;
  private List<UserComment> userComments;
  private String token;
  private ImageButton bookmarkButton;
  private Button commentButton;
  private SharedPreferences sharedPref;
  private Set<String> bookmarks;
  private ListView commentListView;
  private ImageView locationImage;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    //Sets up the activity and required fields
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_location);
    commentListView = findViewById(R.id.comment_list_view);
    View headerView = getLayoutInflater().inflate(R.layout.activity_location_header, null);
    commentListView.addHeaderView(headerView);
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

    //Sets up layout content
    setupViews();

    //Queries the database
    UUID locationUUID = UUID.fromString(locationID);
    new LocationTask().execute(locationUUID);
  }

  /**
   * This method filters out user comments that do not have the approved flag by populating a new
   * list with only the approved comments.
   * It could easily be done with userComments.removeIf(s -> !s.isApproved()); but this method would
   * limit the application to require API 24, excluding about 40% of the marketplace.
   * @param unfilteredComments the unfiltered userComments List.
   * @return a list of filtered comments, only with the approved flag set to true.
   */
  private List<UserComment> filterComments(List<UserComment> unfilteredComments) {
    List<UserComment> filteredComments = new ArrayList<>();
    for (UserComment userComment : unfilteredComments) {
      if (userComment.isApproved()) {
        filteredComments.add(userComment);
      }
    }
    return filteredComments;
  }

  /**
   * Gets film location.
   *
   * @return the film location
   */
  public FilmLocation getLocation() {
    return location;
  }

  /**
   * Gets production.
   *
   * @return the production
   */
  Production getProduction() {
    return production;
  }

  private void loadPosterImage() {
    //TODO This is the "lazy method" to add an authorization header, there's a cleaner method using Modules.
    GlideUrl urlWithLazyHeader = new GlideUrl(String
        .format(getString(R.string.poster_url_format), getString(R.string.base_url),
            production.getId()),
        new LazyHeaders.Builder()
            .addHeader("Authorization", token)
            .build());
    Glide.with(LocationActivity.this)
        .load(urlWithLazyHeader)
        .into(locationPosterImage);
    //TODO Default image in case there's no poster?
  }

  @Override
  protected void onStop() {
    super.onStop();
    //Saves bookmarks to shared preferences when activity is no longer visible.
    saveBookmarksToSharedPref(bookmarks);
  }

  private void saveBookmarksToSharedPref(Set<String> bookmarks) {
    SharedPreferences.Editor editor = sharedPref.edit();
    editor.putStringSet(SHARED_PREF_BOOKMARKS, bookmarks);
    editor.apply();
  }


  /**
   * Sets film location.
   *
   * @param location the film location
   */
  public void setLocation(FilmLocation location) {
    this.location = location;
  }


  /**
   * Sets production.
   *
   * @param production the production
   */
  public void setProduction(Production production) {
    this.production = production;
  }

  private void setupButtons() {
    bookmarkButton = findViewById(R.id.bookmark_button);
    if (bookmarks.contains(location.getId().toString())) {
      bookmarkButton.setSelected(true);
    }
    commentButton = findViewById(R.id.submit_comment_button);
  }

  private void setupBookmarkListener() {
    //Setup bookmark button
    bookmarkButton.setOnClickListener(v -> {
      String locationId = location.getId().toString();
      if (bookmarks.contains(locationId)) {
        bookmarks.remove(locationId);
        Toast.makeText(LocationActivity.this, R.string.bookmark_removed_notification,
            Toast.LENGTH_SHORT).show();
        bookmarkButton.setSelected(false);
        bookmarkButton.refreshDrawableState();
      } else {
        bookmarks.add(locationId);
        Toast.makeText(LocationActivity.this, R.string.bookmark_added_notification,
            Toast.LENGTH_SHORT).show();
        bookmarkButton.setSelected(true);
        bookmarkButton.refreshDrawableState();
      }
    });
  }

  private void setupCommentButtonListener() {
    //Setup comment button
    commentButton.setOnClickListener(v -> {
      SubmitCommentDialog submitCommentDialog = new SubmitCommentDialog();
      submitCommentDialog.show(getSupportFragmentManager(), "");
    });
  }

  private void setupComments() {
    commentListView = findViewById(R.id.comment_list_view);
    userComments = filterComments(userComments);
    CommentAdapter commentAdapter = new CommentAdapter(LocationActivity.this, R.layout.cardview_item_comment, userComments);
    commentListView.setAdapter(commentAdapter);
  }

  private void setupImdbLink() {
    //Setup IMDB link
    locationImdb.setText(R.string.imdb_link);
    locationImdb.setOnClickListener(v -> {
      Uri locationImdb = Uri.parse(getString(R.string.imdb_url) + production.getImdbID());
      Intent intent = new Intent(Intent.ACTION_VIEW, locationImdb);
      startActivity(intent);
    });
  }

  private void setupTextViews() {
    //Assigns data to text views after it has been retrieved from the database..
    production = location.getProduction();
    String locationText = location.getSiteName();
    locationSiteName.setText(locationText);
    String productionTitle = production.getTitle();
    locationProductionTitle.setText(productionTitle);
    String productionPlot = production.getPlot();
    locationPlot.setText(productionPlot);
  }

  private void setupViews() {
    locationImage = findViewById(R.id.image_view_header);
    locationPosterImage = findViewById(R.id.image_view_poster);
    locationProductionTitle = findViewById(R.id.production_title_view);
    locationSiteName = findViewById(R.id.location_sitename_view);
    locationImdb = findViewById(R.id.imdb_link_view);
    locationPlot = findViewById(R.id.plot_view);
    commentListView = findViewById(R.id.comment_list_view);
  }

  /**
   * Asynchronyous task that retrieves the location and comments for the location from the database.
   * Returns a boolean. If the query was successful populates the location and comments. If not
   * successful handles the error message.
   */
  public class LocationTask extends AsyncTask<UUID, Void, Boolean> {

    private String errorMessage = getString(R.string.error_default);

    @Override
    protected Boolean doInBackground(UUID... UUIDs) {
      boolean successfulQuery = false;
      try {
        Call<FilmLocation> locationCall = filmTourApplication.getService()
            .getFilmLocation(token, UUIDs[0]);
        Call<List<UserComment>> commentCall = filmTourApplication.getService()
            .getComments(token, UUIDs[0]);
        Response<FilmLocation> locationCallResponse = locationCall.execute();
        Response<List<UserComment>> commentCallResponse = commentCall.execute();
        if (locationCallResponse.isSuccessful() && commentCallResponse.isSuccessful()) {
          location = locationCallResponse.body();
          userComments = commentCallResponse.body();
          successfulQuery = true;
        } else {
          Log.d(ERROR_LOG_TAG_LOCATION_ACTIVITY, String.valueOf(locationCallResponse.code()));
          Log.d(ERROR_LOG_TAG_LOCATION_ACTIVITY, String.valueOf(commentCallResponse.code()));
          errorMessage = filmTourApplication.getErrorMessageFromHttpResponse(locationCallResponse);
          //I can't think of a situation where both of these calls would return different response codes.
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
        LocationActivity.this.setTitle(location.getSiteName());
        setupTextViews();
        setupImdbLink();
        loadPosterImage();
        setupButtons();
        setupBookmarkListener();
        setupCommentButtonListener();
        setupComments();
        /* A method like this will set up an image button when the image feature is implemented.
        imageButton = findViewById(R.id.register_image_button);
        imageButton.setOnClickListener(v -> {
          UploadImageDialog uploadImageDialog = new UploadImageDialog();
          uploadImageDialog.show(getSupportFragmentManager(), "whatever");
        });*/
      } else {
        filmTourApplication.handleErrorMessage(LocationActivity.this, errorMessage);
      }
    }
  }
}
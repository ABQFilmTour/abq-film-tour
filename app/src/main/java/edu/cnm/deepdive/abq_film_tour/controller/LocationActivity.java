package edu.cnm.deepdive.abq_film_tour.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import edu.cnm.deepdive.abq_film_tour.R;
import edu.cnm.deepdive.abq_film_tour.model.entity.FilmLocation;
import edu.cnm.deepdive.abq_film_tour.model.entity.Production;
import edu.cnm.deepdive.abq_film_tour.model.entity.UserComment;
import edu.cnm.deepdive.abq_film_tour.service.FilmTourApplication;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class LocationActivity extends AppCompatActivity {

  private ImageView locationImage;
  private TextView locationProductionTitle;
  private TextView locationTitle;
  private TextView locationImdb;
  private TextView locationPlot;
  private TextView locationComments;

  private FilmLocation location;
  private Production production;
  private List<UserComment> userComments;

  private final String LOCATION_ID_KEY = "location_id_key";

  private FilmTourApplication filmTourApplication;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_location);
    filmTourApplication = (FilmTourApplication) getApplication();

    Bundle extras = getIntent().getExtras();
    assert extras != null;
    String locationID = extras.getString(LOCATION_ID_KEY);

    locationImage = findViewById(R.id.imageViewHeader);
    locationTitle = findViewById(R.id.location_title_view);
    locationProductionTitle = findViewById(R.id.production_title_view);
    locationImdb = findViewById(R.id.imdb_link_view);
    locationPlot = findViewById(R.id.plot_view);
    UUID locationUUID = UUID.fromString(locationID);
    new LocationTask().execute(locationUUID);
  }


  public class LocationTask extends AsyncTask<UUID, Void, Void> {

    @Override
    protected Void doInBackground(UUID... UUIDs) {
      try {

        location = filmTourApplication.getService().getFilmLocation(UUIDs[0]).execute().body();
        userComments = filmTourApplication.getService().getComments(UUIDs[0]).execute().body();
      } catch (IOException e) {
        System.out.println("IO exception thrown in LocationTask of LocationActivity.");
      }
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
      String pathId = location.getId().toString();
      production = location.getProduction();
      String locationText = location.getSiteName();
      locationTitle.setText(locationText);
      String productionTitle = production.getTitle();
      locationProductionTitle.setText(productionTitle);
      String productionPlot = production.getPlot();
      locationPlot.setText(productionPlot);

      ListView commentListView = findViewById(R.id.comments_list_view);
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

      locationImdb.setText(R.string.imdb_link);
      locationImdb.setOnClickListener(v -> {
        System.out.println(production.getTitle());
        System.out.println(production.getImdbID());
        Uri locationImdb = Uri.parse("https://www.imdb.com/title/" + production.getImdbID());
        Intent intent = new Intent(Intent.ACTION_VIEW, locationImdb);
        startActivity(intent);
      });
    }
  }
}

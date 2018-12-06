package edu.cnm.deepdive.abq_film_tour.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import edu.cnm.deepdive.abq_film_tour.R;
import edu.cnm.deepdive.abq_film_tour.model.entity.FilmLocation;
import edu.cnm.deepdive.abq_film_tour.model.entity.Production;
import edu.cnm.deepdive.abq_film_tour.model.entity.UserComment;
import edu.cnm.deepdive.abq_film_tour.service.FilmTourApplication;
import java.io.IOException;

public class LocationActivity extends AppCompatActivity {

  private ImageView locationImage;
  private TextView locationProductionTitle;
  private TextView locationTitle;
  private TextView locationImdb;
  private TextView locationPlot;
  private TextView locationComments;

  public static FilmLocation location;
  public static Production production;
  public static UserComment comment;


  FilmTourApplication filmTourApplication;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_location);
    filmTourApplication = (FilmTourApplication) getApplication();

    comment = MapsActivity.exampleComment; //TODO fix

    Bundle extras = getIntent().getExtras();
    String locationID = extras.getString("locationID");

    locationImage = findViewById(R.id.image_view);
    locationTitle = findViewById(R.id.location_title_view);
    locationProductionTitle = findViewById(R.id.production_title_view);
    locationImdb = findViewById(R.id.imdb_link_view);
    locationComments = findViewById(R.id.comments_view);
    locationPlot = findViewById(R.id.plot_view);

    new LocationTask().execute(locationID);
  }


  public class LocationTask extends AsyncTask<String, Void, Void> {


    @Override
    protected Void doInBackground(String... strings) {
      try {
        location = filmTourApplication.getService().getFilmLocation(strings[0]).execute().body();
      } catch (IOException e) {
        System.out.println("oh no!!!!!");
      }
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
      production = location.getProduction();
      String locationText = location.getSiteName();
      locationTitle.setText(locationText);
      String productionTitle = production.getTitle();
      locationProductionTitle.setText(productionTitle);
      String productionPlot = production.getPlot();
      locationPlot.setText(productionPlot);
      String locationComment = comment.getContent();
      locationComments.setText(locationComment);
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

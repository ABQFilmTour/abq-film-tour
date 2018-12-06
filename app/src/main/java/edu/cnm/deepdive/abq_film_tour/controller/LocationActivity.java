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



  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_location);

    location = MapsActivity.exampleLocation;
    production = MapsActivity.exampleProduction;
    comment = MapsActivity.exampleComment;

    Bundle extras = getIntent().getExtras();
    String locationID = extras.getString("locationID");
    this.setTitle(locationID);

    locationImage = findViewById(R.id.image_view);
    locationTitle = findViewById(R.id.location_title_view);
    locationProductionTitle = findViewById(R.id.production_title_view);
    locationImdb = findViewById(R.id.imdb_link_view);
    locationComments = findViewById(R.id.comments_view);
    locationPlot = findViewById(R.id.plot_view);

    new LocationTask().execute();

    locationImdb.setOnClickListener(new View.OnClickListener() {
      String imdbId = production.getImdbID();
      @Override
      public void onClick(View v) {
       Uri locationImdb = Uri.parse("https://www.imdb.com/title/" + imdbId);
        Intent intent = new Intent(Intent.ACTION_VIEW, locationImdb);
        startActivity(intent);
      }
    });
  }


  public class LocationTask extends AsyncTask<Void, Void, String>{


    @Override
    protected void onPostExecute(String locationId) {
      super.onPostExecute(locationId);

        String locationText = location.getSiteName();
        locationTitle.setText(locationText);
        String productionTitle = production.getTitle();
        locationProductionTitle.setText(productionTitle);
        String productionPlot = production.getPlot();
        locationPlot.setText(productionPlot);
        String locationComment = comment.getContent();
        locationComments.setText(locationComment);
        locationImdb.setText(R.string.imdb_link);

    }

    @Override
    protected String doInBackground(Void... voids) {
      return null;
    }
  }


}

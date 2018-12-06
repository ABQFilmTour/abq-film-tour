package edu.cnm.deepdive.abq_film_tour.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
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

    locationImage = findViewById(R.id.image_view);
    locationTitle = findViewById(R.id.location_title_view);
    locationProductionTitle = findViewById(R.id.production_title_view);
    locationImdb = findViewById(R.id.imdb_link_view);
    locationComments = findViewById(R.id.comments_view);
    locationPlot = findViewById(R.id.plot_view);


    updateLocationTitle();
    updateProductionTitle();
    setLocationPlot();
    setLocationComments();
    setLocationImdb();

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
  public void updateLocationTitle(){
    String locationText = location.getSiteName();
    locationTitle.setText(locationText);
  }

  public void updateProductionTitle(){
    String productionTitle = production.getTitle();
    locationProductionTitle.setText(productionTitle);
  }

 public void setLocationPlot(){
    String productionPlot = production.getPlot();
    locationPlot.setText(productionPlot);
 }

 public void setLocationComments(){
      String locationComment = comment.getContent();
      locationComments.setText(locationComment);
 }

 public void setLocationImdb(){
      locationImdb.setText("IMDB Link");

 }


}

package edu.cnm.deepdive.abq_film_tour.controller;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import edu.cnm.deepdive.abq_film_tour.R;

public class LocationActivity extends AppCompatActivity {

  private ImageView locationImage;
  private TextView locationProductionTitle;
  private TextView locationTitle;
  private WebView locationOmdb;
  private TextView locationPlot;
  private TextView locationComments;


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_location);

//    locationImage = findViewById(R.id.image_view);
    locationTitle = findViewById(R.id.location_title_view);
    locationProductionTitle = findViewById(R.id.production_title_view);
//    locationOmdb = findViewById(R.id.omdb_link_view);
    locationComments = findViewById(R.id.comments_view);
    locationPlot = findViewById(R.id.plot_view);


    updateLocationTitle();
    updateProductionTitle();
    setLocationPlot();
    setLocationComments();

//    locationOmdb.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        //TODO link to imdb page when clicked
//      }
//    });
  }
  public void updateLocationTitle(){
    String location = MapsActivity.exampleLocation.getSiteName();
    locationTitle.setText(location);
  }

  public void updateProductionTitle(){
    String location = MapsActivity.exampleProduction.getTitle();
    locationProductionTitle.setText(location);
  }

 public void setLocationPlot(){
    String location = MapsActivity.exampleProduction.getPlot();
    locationPlot.setText(location);
 }

 public void setLocationComments(){
      String location = MapsActivity.exampleComment.getContent();
      locationComments.setText(location);
 }



}

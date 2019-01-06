package edu.cnm.deepdive.abq_film_tour.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import edu.cnm.deepdive.abq_film_tour.R;
import edu.cnm.deepdive.abq_film_tour.model.entity.FilmLocation;

/**
 * Custom adapter for snippets.
 */
public class CustomSnippetAdapter implements GoogleMap.InfoWindowAdapter {

  private final View snippet;
  private Context context;

  /**
   * Instantiates a new Custom snippet adapter.
   *
   * @param context the context
   */
  public CustomSnippetAdapter(Context context) {
    snippet = LayoutInflater.from(context).inflate(R.layout.custom_info_snippet, null);
  }

//TODO Becca make this work
//
//  public double howFarAway();{
//    double venueLat = Double.valueOf(location.getLatCoordinate());
//    double venueLng = Double.valueOf(location.getLongCoordinate());
//    double delta = MapsActivity.calculateDistanceInKilometer(userLatLng.latitude,
//        userLatLng.longitude,
//        venueLat, venueLng);
//    return delta;
//
//  }


  private void renderSnippetText(Marker marker, View view){
    String title = marker.getTitle();
    TextView snippetTitle = view.findViewById(R.id.snippet_title);
    FilmLocation location = (FilmLocation) marker.getTag();
    double venueLat = Double.valueOf(location.getLatCoordinate());
    double venueLng = Double.valueOf(location.getLongCoordinate());
//    double delta = MapsActivity.calculateDistanceInKilometer(userLatLng.latitude,
//        userLatLng.longitude,
//        venueLat, venueLng);



    if (!title.equals("")){
      snippetTitle.setText(title);
    }
    String snippet = marker.getTitle();
    TextView snippetDescription = view.findViewById(R.id.snippet_description);

    if (!snippet.equals("")) {
      snippetDescription.setText(location.getProduction().getTitle());
    }
  }


  @Override
  public View getInfoContents(Marker marker) {
    renderSnippetText(marker, snippet);
    return snippet;
  }

  @Override
  public View getInfoWindow(Marker marker) {
    renderSnippetText(marker, snippet);
    return snippet;
  }

}
package edu.cnm.deepdive.abq_film_tour.controller;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import edu.cnm.deepdive.abq_film_tour.R;
import edu.cnm.deepdive.abq_film_tour.model.entity.FilmLocation;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

  private GoogleMap map;
  private static final float ZOOM_LEVEL = 15;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.options, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    boolean handled = true;
    // TODO rig hand menu option for testing/future potential
    switch (item.getItemId()) {
      default:
        handled = super.onOptionsItemSelected(item);
        break;
      case R.id.menu_all_near_me:
        // TODO reposition camera to user location
        break;
      case R.id.menu_television:
        // TODO open dialog to select a series
        break;
      case R.id.menu_film:
        // TODO open dialog to select a film
        break;
      case R.id.menu_submit:
        // TODO open dialog to submit a location
        break;
    }
    return handled;
  }


  /**
   * Manipulates the map once available. This callback is triggered when the map is ready to be
   * used. This is where we can add markers or lines, add listeners or move the camera. In this
   * case, we just add a marker near Sydney, Australia. If Google Play services is not installed on
   * the device, the user will be prompted to install it inside the SupportMapFragment. This method
   * will only be triggered once the user has installed Google Play services and returned to the
   * app.
   */
  @Override
  public void onMapReady(GoogleMap googleMap) {
    map = googleMap;

    // Build FilmLocation entity for Hot Dog Place
    FilmLocation hotDogPlace = new FilmLocation("The Dog House", 35.0879, -106.6614);
    hotDogPlace.setOriginalDetails("Original details here \uD83C\uDF2D");

    // Add a marker in hot dogs and move the camera
    LatLng dogHouseCoordinates = new LatLng(hotDogPlace.getLongCoordinate(), hotDogPlace.getLatCoordinate());

    map.addMarker(new MarkerOptions()
        .position(dogHouseCoordinates)
        .title(hotDogPlace.getSiteName())
        .snippet(hotDogPlace.getOriginalDetails()));
    map.moveCamera(CameraUpdateFactory.newLatLng(dogHouseCoordinates));
    map.moveCamera(CameraUpdateFactory.zoomTo(ZOOM_LEVEL));

  }
}

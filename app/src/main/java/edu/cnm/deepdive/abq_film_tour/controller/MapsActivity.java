package edu.cnm.deepdive.abq_film_tour.controller;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import edu.cnm.deepdive.abq_film_tour.R;
import edu.cnm.deepdive.abq_film_tour.model.entity.FilmLocation;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Maps activity.
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

  private static final String TITLE_LIST_KEY="titlesList";
  private static final String TITLE_SELECTION_KEY="titleSelection";
  private static final float ZOOM_LEVEL = 15;

  private ArrayList<String> movieTitles;
  private ArrayList<String> tvTitles;
  private GoogleMap map;
  private SelectionDialog selectionDialog;
  private FusedLocationProviderClient fusedLocationProviderClient;
  private Bundle arguments;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
    createDummyLocations();
  }

  private void createDummyLocations() {
    movieTitles = new ArrayList<>();
    tvTitles = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      movieTitles.add("Movie Title " + i);
      tvTitles.add("TV Title " + i);
    }
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
    switch (item.getItemId()) {
      default:
        handled = super.onOptionsItemSelected(item);
        break;
      case R.id.menu_all_near_me:
        //TODO change camera view to user location
        /* code throws an exception, may be due to permissions
        Location userLocation = fusedLocationProviderClient.getLastLocation().getResult();
        LatLng userLocationCoordinates =
            new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLng(userLocationCoordinates));
        */
        Toast.makeText(this, "Near me", Toast.LENGTH_SHORT).show();
        break;
      case R.id.menu_television:
        selectionDialog = new SelectionDialog();
        arguments = new Bundle();
        arguments.putStringArrayList(TITLE_LIST_KEY, tvTitles);
        selectionDialog.setArguments(arguments);
        selectionDialog.show(getSupportFragmentManager(), "dialog");
        break;
      case R.id.menu_film:
        selectionDialog = new SelectionDialog();
        arguments = new Bundle();
        arguments.putStringArrayList(TITLE_LIST_KEY, movieTitles);
        selectionDialog.setArguments(arguments);
        selectionDialog.show(getSupportFragmentManager(), "dialog");
        break;
      case R.id.menu_submit:
        // TODO open dialog to submit a location
        Toast.makeText(this, "Submission dialog", Toast.LENGTH_SHORT).show();
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

    Marker marker = map.addMarker(new MarkerOptions()
        .position(dogHouseCoordinates)
        .title(hotDogPlace.getSiteName())
        .snippet(hotDogPlace.getOriginalDetails()));
    marker.setTag(hotDogPlace);
    map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
      @Override
      public void onInfoWindowClick(Marker marker) {
        FilmLocation markerLocation = ((FilmLocation) marker.getTag());
        Bundle extras = new Bundle();
        extras.putString("exampleTag", markerLocation.getSiteName());
        Intent intent = new Intent(MapsActivity.this, LocationActivity.class);
        startActivity(intent);
      }
    });
    map.moveCamera(CameraUpdateFactory.newLatLng(dogHouseCoordinates));
    map.moveCamera(CameraUpdateFactory.zoomTo(ZOOM_LEVEL));

  }

}
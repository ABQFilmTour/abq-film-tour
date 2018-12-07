package edu.cnm.deepdive.abq_film_tour.controller;

import static edu.cnm.deepdive.abq_film_tour.controller.SelectionDialog.SELECTED_OPTIONS_MENU_ITEM_KEY;
import static edu.cnm.deepdive.abq_film_tour.controller.SelectionDialog.TITLE_LIST_KEY;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
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
import edu.cnm.deepdive.abq_film_tour.model.entity.Production;
import edu.cnm.deepdive.abq_film_tour.model.entity.User;
import edu.cnm.deepdive.abq_film_tour.model.entity.UserComment;
import edu.cnm.deepdive.abq_film_tour.service.FilmTourApplication;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is the primary activity for the application. It implements Google Map functionality and
 * displays map markers generated from the backend server.
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

  //CONSTANTS
  //TODO resolve weird constant imports
  private static final String TYPE_SERIES = "series";
  private static final String TYPE_MOVIE = "movie";
  private static final String LOCATION_ID_KEY = "location_id_key";
  private static final float ZOOM_LEVEL = 10; //TODO Zoom level? Start coordinates?
  //First result on google when I searched "city of albuquerque coordinates"
  private static final String START_LONG = "-106.6055534";
  private static final String START_LAT = "35.0853336";

  //FIELDS
  private FilmTourApplication filmTourApplication;
  private ArrayList<String> movieTitles;
  private ArrayList<String> tvTitles;
  private List<FilmLocation> locations;
  private List<Production> productions;
  private GoogleMap map;
  private SelectionDialog selectionDialog;
  private SubmitDialog submitDialog;
  private FusedLocationProviderClient fusedLocationProviderClient;
  private Bundle arguments;

  // TODO Remove these fields when the data is more stable.
  private static FilmLocation startLocation;
  static UserComment exampleComment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    filmTourApplication = (FilmTourApplication) getApplication();
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);
    new GetProductionsTask().execute();

    //CREATE START LOCATION TO POSITION CAMERA TO
    startLocation = new FilmLocation();
    startLocation.setLatCoordinate(START_LAT);
    startLocation.setLongCoordinate(START_LONG);
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);

    ActionBar actionBar = getSupportActionBar();
    actionBar.setLogo(R.mipmap.ic_filmtour_round);
    actionBar.setDisplayUseLogoEnabled(true);
    actionBar.setDisplayShowHomeEnabled(true);
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
  }

  /**
   * This method populates the "movie titles" and "series titles" lists to be displayed from the
   * SelectionDialog fragment.
   */
  private void populateTitlesList() {
    movieTitles = new ArrayList<>();
    tvTitles = new ArrayList<>();
    for (Production production : productions) {
      if (production.getType() == null) {
        //TODO Record if null data is in the database
        continue; // skip null data
      }
      else if (production.getType().equals(TYPE_SERIES)) {
        tvTitles.add(production.getTitle());
      }
      else if (production.getType().equals(TYPE_MOVIE)) {
        movieTitles.add(production.getTitle());
      }
    }
    Collections.sort(tvTitles);
    Collections.sort(movieTitles);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.options, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    selectionDialog = new SelectionDialog();
    boolean handled = true;
    switch (item.getItemId()) {
      default:
        handled = super.onOptionsItemSelected(item);
        break;
      case R.id.menu_all_near_me:
        arguments.putString(SELECTED_OPTIONS_MENU_ITEM_KEY, "NEAR ME");
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
        arguments.putString(SELECTED_OPTIONS_MENU_ITEM_KEY, "TV SHOW");
        arguments.putStringArrayList(TITLE_LIST_KEY, tvTitles);
        selectionDialog.setArguments(arguments);
        selectionDialog.show(getSupportFragmentManager(), "dialog");
        break;
      case R.id.menu_film:
        selectionDialog = new SelectionDialog();
        arguments = new Bundle();
        arguments.putString(SELECTED_OPTIONS_MENU_ITEM_KEY, "FILMS");
        arguments.putStringArrayList(TITLE_LIST_KEY, movieTitles);
        selectionDialog.setArguments(arguments);
        selectionDialog.show(getSupportFragmentManager(), "dialog");
        break;
/*
      case R.id.menu_submit:
        submitDialog = new SubmitDialog();
        submitDialog.show(getSupportFragmentManager(), "dialog");
        break; */
      case R.id.sign_out:
        signOut();
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
    new PopulateMapPinsTask().execute();
    LatLng startCoordinates = new LatLng(Double.valueOf(startLocation.getLatCoordinate()),
        Double.valueOf(startLocation.getLongCoordinate()));
    map.moveCamera(CameraUpdateFactory.newLatLng(startCoordinates));
    map.moveCamera(CameraUpdateFactory.zoomTo(ZOOM_LEVEL));
  }

  private void signOut() {
    FilmTourApplication app = FilmTourApplication.getInstance();
    app.getClient().signOut().addOnCompleteListener(this, (task) -> {
      app.setAccount(null);
      Intent intent = new Intent(this, LoginActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);
    });
  }

  public void populateMapFromTitle(String title) {
    map.clear();
    for (FilmLocation location : locations) {
      if (location.getProduction().getTitle() != null && location.getProduction().getTitle().equals(title)) {
        LatLng coordinates = new LatLng(Double.valueOf(location.getLongCoordinate()),
            Double.valueOf(location.getLatCoordinate()));
        Marker marker = map.addMarker(new MarkerOptions()
            .position(coordinates)
            .title(location.getSiteName())
            .snippet(
                location.getProduction().getTitle())); //TODO Snipper should be something else?
        marker.setTag(location);
        map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
          @Override
          public void onInfoWindowClick(Marker marker) {
            FilmLocation taggedLocation = (FilmLocation) marker.getTag();
            Intent intent = new Intent(MapsActivity.this, LocationActivity.class);
            intent.putExtra(LOCATION_ID_KEY, taggedLocation.getId().toString());
            startActivity(intent);
          }
        });
      }
    }
  }

  /**
   * Asynchronous task that retrieves the productions from the server.
   */
  private class GetProductionsTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
      try {
        productions = filmTourApplication.getService().getProductions().execute().body();
      } catch (IOException e) {
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
      populateTitlesList();
    }
  }

  /**
   * Asynchronous task that retrieves the film locations from the server.
   */
  private class PopulateMapPinsTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
      try {
        locations = filmTourApplication.getService().getLocations().execute().body();
      } catch (IOException e) {
        //TODO Handle or don't.
      }
      return null;

    }

    @Override
    protected void onPostExecute(Void aVoid) {
      for (FilmLocation location : locations) {
        LatLng coordinates = new LatLng(Double.valueOf(location.getLongCoordinate()),
            Double.valueOf(location.getLatCoordinate()));
        Marker marker = map.addMarker(new MarkerOptions()
            .position(coordinates)
            .title(location.getSiteName())
            .snippet(
                location.getProduction().getTitle())); //TODO Snipper should be something else?
        marker.setTag(location);
        map.setOnInfoWindowClickListener(marker1 -> {
          FilmLocation taggedLocation = (FilmLocation) marker1.getTag();
          Intent intent = new Intent(MapsActivity.this, LocationActivity.class);
          intent.putExtra(LOCATION_ID_KEY, taggedLocation.getId().toString());
          startActivity(intent);
        });
      }
    }
  }
}
package edu.cnm.deepdive.abq_film_tour.controller;

import android.Manifest.permission;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import edu.cnm.deepdive.abq_film_tour.R;
import edu.cnm.deepdive.abq_film_tour.model.entity.FilmLocation;
import edu.cnm.deepdive.abq_film_tour.model.entity.Production;
import edu.cnm.deepdive.abq_film_tour.service.FilmTourApplication;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the primary activity for the application. It implements Google Map functionality and
 * displays map markers generated from the backend server.
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

  //CONSTANTS
  /**
   * String type that refers to Television Production types.
   */
  private static final String TYPE_SERIES = "series";
  /**
   * String type that refers to Film Production types.
   */
  private static final String TYPE_MOVIE = "movie";
  /**
   * Shared preferences tag to reference the last saved title.
   */
  private static final String SHARED_PREF_LAST_TITLE = "last_title";
  /**
   * Initial zoom level for the map camera, should display a birds eye view of the ABQ area.
   */
  private static final float ZOOM_LEVEL_INITIAL = 9;
  /**
   * Zoom level for the camera when "Near Me" is selected.
   */
  private static final float ZOOM_LEVEL_NEAR_ME = 17;
  /**
   * Bearing level for hte camera when "Near Me" is selected, this skews the map direction so should
   * be avoided.
   */
  private static final float BEARING_LEVEL_NEAR_ME = 0;
  /**
   * Tilt level for the camera when "Near Me" is selected, looks really cool.
   */
  private static final float TILT_LEVEL_NEAR_ME = 40;
  /**
   * BECCA ???
   */
  private static final double AVERAGE_RADIUS_OF_EARTH_KM = 6371;
  /**
   * Latitude coordinates for Albuquerque according to Google, it's about 3rd and Central.
   */
  private static final double BURQUE_LAT = 35.0844;
  /**
   * Longitude coordinates for Albuquerque according to Google, it's about 3rd and Central.
   */
  private static final double BURQUE_LONG = -106.6504;
  /**
   * Bounds of the Albuquerque area to determine if the user is close enough to use the app.
   */
  private static final int BURQUE_LIMITS = 50;
  /**
   * Range from the user location "Near me" should populate map markers.
   */
  private static final double KM_RANGE_FROM_USER = 1.5;
  /**
   * Extras key to pass a list of production titles into a selection dialog.
   */
  private static final String TITLE_LIST_KEY = "titlesList";
  /**
   * Extras tag to pass a location UUID String into a LocationActivity.
   */
  private static final String LOCATION_ID_KEY = "location_id_key";
  /**
   * Extras key to pass in a selection menu item.
   */
  private static final String SELECTED_OPTIONS_MENU_ITEM_KEY = "selectedOptionMenuItem";
  /**
   * BECCA ???
   */
  private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 11;


  //FIELDS
  /**
   * Object to reference the parent application.
   */
  private FilmTourApplication filmTourApplication;
  /**
   * A list of movie titles to display in a ListView.
   */
  private ArrayList<String> movieTitles;
  /**
   * A list of television titles to display in a ListView.
   */
  private ArrayList<String> tvTitles;
  /**
   * A list of the FilmLocation objects retrieved from the backend.
   */
  private List<FilmLocation> locations;
  /**
   * A list of the Production objects retrieved from the backend.
   */
  private List<Production> productions;
  /**
   * The GoogleMap object for the activity.
   */
  private GoogleMap map;
  /**
   * LocationManager object to reference the user location.
   */
  private LocationManager locationManager;
  /**
   * SharedPreferences object
   */
  private SharedPreferences sharedPref;

  /**
   * BECCA Document code.
   */
  private LocationListener locationListenerGPS = new LocationListener() {

    @Override
    public void onLocationChanged(android.location.Location location) {
      double latitude = location.getLatitude();
      double longitude = location.getLongitude();
      String msg = "New Latitude: " + latitude + "New Longitude: " + longitude;
      //Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
      // Do nothing.
    }

    @Override
    public void onProviderEnabled(String provider) {
      // Do nothing.
    }

    @Override
    public void onProviderDisabled(String provider) {
      // Do nothing.
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) throws SecurityException {
    filmTourApplication = (FilmTourApplication) getApplication();
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);
    new GetProductionsTask().execute();
    sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map);
    assert mapFragment != null;
    mapFragment.getMapAsync(this);

    ActionBar actionBar = getSupportActionBar();
    assert actionBar != null;
    actionBar.setLogo(R.drawable.toolbar_icon);
    actionBar.setDisplayUseLogoEnabled(true);
    actionBar.setDisplayShowHomeEnabled(true);

    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    getLocationPermission();
    isLocationEnabled();
  }

  @Override
  protected void onPostResume() {
    super.onPostResume();
    isLocationEnabled();
  }

  /**
   * BECCA Document code
   */
  private int calculateDistanceInKilometer(double userLat, double userLng,
      double venueLat, double venueLng) {
    double latDistance = Math.toRadians(userLat - venueLat);
    double lngDistance = Math.toRadians(userLng - venueLng);
    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
        + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(venueLat))
        * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return (int) (Math.round(AVERAGE_RADIUS_OF_EARTH_KM * c));
  }

  /**
   * This method checks if a title is in the local SharedPreferences. If there is, it starts with
   * the data for that production, if not, it prompts the user to select a title.
   */
  private void checkForPastTitle() {
    String savedTitle = sharedPref.getString(SHARED_PREF_LAST_TITLE, null);
    if (savedTitle != null) {
      populateMapFromTitle(savedTitle);
    } else {
      Toast.makeText(MapsActivity.this, R.string.startup_select_title, Toast.LENGTH_LONG).show();
      setTitle(getString(R.string.application_title));
    }
  }

  /**
   * SAM Document code
   */
  private static Bitmap createCustomMarker(Context context, @DrawableRes int resource) {

    View marker = ((LayoutInflater) Objects.requireNonNull(context.getSystemService(
        Context.LAYOUT_INFLATER_SERVICE))).inflate(R.layout.custom_marker_layout, null);

    DisplayMetrics displayMetrics = new DisplayMetrics();
    ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
    marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
    marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
    Bitmap bitmap = Bitmap.createBitmap(
        marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    marker.draw(canvas);

    return bitmap;
  }

  /**
   * This method creates a map marker based on a given location, along with the snippet and
   * onClickListener for the marker. The FilmLocation entity is attached as a tag for reference.
   *
   * @param location the FilmLocation to be associated with this marker, and where coordinates are
   * retrieved from.
   */
  private void createMapMarker(FilmLocation location) {
    LatLng coordinates = new LatLng(Double.valueOf(location.getLatCoordinate()),
        Double.valueOf(location.getLongCoordinate()));
    Marker marker = map.addMarker(new MarkerOptions()
        .position(coordinates)
        .icon(BitmapDescriptorFactory.fromBitmap(
            createCustomMarker(MapsActivity.this, R.drawable.map_pin)))
        .title(location.getSiteName())
        .snippet(
            location.getProduction().getTitle()));
    map.setInfoWindowAdapter(new CustomSnippetAdapter(MapsActivity.this));
    marker.setTag(location);
    map.setOnInfoWindowClickListener(marker1 -> {
      FilmLocation taggedLocation = (FilmLocation) marker1.getTag();
      Intent intent = new Intent(MapsActivity.this, LocationActivity.class);
      assert taggedLocation != null;
      intent.putExtra(LOCATION_ID_KEY, taggedLocation.getId().toString());
      startActivity(intent);
    });
  }



  /**
   * BECCA Document code
   */
  private boolean inBurque(LatLng userLatLng) {
    int delta = calculateDistanceInKilometer(userLatLng.latitude, userLatLng.longitude,
        BURQUE_LAT, BURQUE_LONG);
    return (delta < BURQUE_LIMITS);
  }

  /**
   * BECCA Document code.
   */
  private void isLocationEnabled() {
    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
      AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.AlertDialog);
      alertDialog.setTitle("Enable Location");
      alertDialog
          .setMessage(R.string.enable_location_settings);
      alertDialog.setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
          Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
          startActivity(intent);
        }
      });
      alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
          dialog.cancel();
        }
      });
      AlertDialog alert = alertDialog.create();
      alert.show();
    }
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
        // Log info and skip loop, could "continue;" but currently unnecessary. Unfamiliar with
        // logs so I'm not sure where this is recorded.
        Logger logger = Logger.getLogger(MapsActivity.class.getName());
        logger.log(Level.WARNING, String.format(getString(R.string.log_null_production_type),
            production.getId()));
      } else if (production.getType().equals(TYPE_SERIES)) {
        tvTitles.add(production.getTitle());
      } else if (production.getType().equals(TYPE_MOVIE)) {
        movieTitles.add(production.getTitle());
      }
    }
    Collections.sort(tvTitles);
    Collections.sort(movieTitles);
  }

  /**
   * BECCA Document code.
   */
  private void nearMe(LatLng userLatLng) {
    setTitle(getString(R.string.application_title));
    CameraPosition cameraPosition = new CameraPosition.Builder()
        .target(userLatLng)      // Sets the center of the map to location user
        .zoom(ZOOM_LEVEL_NEAR_ME)                   // Sets the zoom
        .bearing(
            BEARING_LEVEL_NEAR_ME)                // Sets the orientation of the camera to east
        .tilt(
            TILT_LEVEL_NEAR_ME)                   // Sets the tilt of the camera to 30 degrees
        .build();                   // Creates a CameraPosition from the builder
    populateMapFromLocation(userLatLng);
    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
  }



  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.options, menu);
    return true;
  }

  /**
   * Manipulates the map once available. This callback is triggered when the map is ready to be
   * used. For this application, the map positions to approximately central Albuquerque.
   */
  @Override
  public void onMapReady(GoogleMap googleMap) {
    map = googleMap;
    LatLng startCoordinates = new LatLng(BURQUE_LAT, BURQUE_LONG);
    map.moveCamera(CameraUpdateFactory.newLatLng(startCoordinates));
    map.moveCamera(CameraUpdateFactory.zoomTo(ZOOM_LEVEL_INITIAL));
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    SelectionDialog selectionDialog = new SelectionDialog();
    boolean handled = true;
    switch (item.getItemId()) {
      default:
        handled = super.onOptionsItemSelected(item);
        break;
      case R.id.menu_all_near_me:
        getDeviceLocation();
        break;
      case R.id.menu_television:
        selectionDialog = new SelectionDialog();
        Bundle arguments = new Bundle();
        arguments.putString(SELECTED_OPTIONS_MENU_ITEM_KEY,
            getString(R.string.selected_options_series_title));
        arguments.putStringArrayList(TITLE_LIST_KEY, tvTitles);
        selectionDialog.setArguments(arguments);
        selectionDialog.show(getSupportFragmentManager(), "dialog");
        break;
      case R.id.menu_film:
        selectionDialog = new SelectionDialog();
        arguments = new Bundle();
        arguments.putString(SELECTED_OPTIONS_MENU_ITEM_KEY,
            getString(R.string.selected_options_films_title));
        arguments.putStringArrayList(TITLE_LIST_KEY, movieTitles);
        selectionDialog.setArguments(arguments);
        selectionDialog.show(getSupportFragmentManager(), "dialog");
        break;
      case R.id.menu_submit:
        SubmitDialog submitDialog = new SubmitDialog();
        submitDialog.show(getSupportFragmentManager(), "dialog");
        break;
      case R.id.sign_out:
        signOut();
        break;
    }
    return handled;
  }

  /**
   * BECCA Document code
   */
  private void populateMapFromLocation(LatLng userLatLng) {
    map.clear();
    for (FilmLocation location : locations) {
      double venueLat = Double.valueOf(location.getLatCoordinate());
      double venueLng = Double.valueOf(location.getLongCoordinate());
      int delta = calculateDistanceInKilometer(userLatLng.latitude, userLatLng.longitude,
          venueLat, venueLng);
      if (delta < KM_RANGE_FROM_USER) {
        createMapMarker(location);
      }
    }
  }

  /**
   * This method calls the createMapMarker method on every FilmLocation in the locations field for
   * the MapsActivity that is not null (to filer out bunk data) and matches a given String title.
   *
   * @param title a production title as a raw string.
   */
  void populateMapFromTitle(String title) {
    map.clear();
    for (FilmLocation location : locations) {
      this.setTitle(title);
      saveSharedPreferences(title);
      if (location.getProduction().getTitle() != null && location.getProduction().getTitle()
          .equals(title)) {
        createMapMarker(location);
      }
    }
  }

  /**
   * This method updates the local SharedPreferences with a given production title.
   *
   * @param title a production title as a raw string..
   */
  private void saveSharedPreferences(String title) {
    SharedPreferences.Editor editor = sharedPref.edit();
    editor.putString(SHARED_PREF_LAST_TITLE, title);
    editor.apply();
  }

  /**
   * This method signs the Google account out of the application and returns the user to the login
   * activity.
   */
  private void signOut() {
    FilmTourApplication app = FilmTourApplication.getInstance();
    app.getClient().signOut().addOnCompleteListener(this, (task) -> {
      app.setAccount(null);
      Intent intent = new Intent(this, LoginActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);
    });
  }

  /**
   * BECCA Document code
   */
  private void getLocationPermission() {
    /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
    ActivityCompat.requestPermissions(this,
        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
            permission.ACCESS_COARSE_LOCATION},
        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

  }

  /**
   * BECCA Document code
   */
  @Override
  public void onRequestPermissionsResult(int requestCode,
      @NonNull String permissions[], @NonNull int[] grantResults) throws SecurityException {
    switch (requestCode) {
      case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
              2000,
              10,
              locationListenerGPS);
        } else {
          // permission denied, boo!
          Toast.makeText(this, R.string.cannot_enable_location,
              Toast.LENGTH_LONG).show();
        }
      }
    }
  }

  /**
   * BECCA Document code
   */
  private void getDeviceLocation() throws SecurityException {
    LocationManager locationManager = (LocationManager) getSystemService(
        Context.LOCATION_SERVICE);
    Criteria criteria = new Criteria();
    criteria.setAccuracy(Criteria.ACCURACY_COARSE);
    assert locationManager != null;
    Location location = locationManager
        .getLastKnownLocation(locationManager.getBestProvider(criteria, true));
    if (location != null) {
      LatLng userLatLng = new LatLng(location.getLatitude(),
          location.getLongitude());
      if (!inBurque(userLatLng)) {
        Toast.makeText(this, R.string.not_in_burque,
            Toast.LENGTH_SHORT).show();
      } else {
        nearMe(userLatLng);
      }
    } else {
      Toast.makeText(this, R.string.null_location, Toast.LENGTH_LONG).show();
    }
  }

  /**
   * Asynchronous task that retrieves the film locations from the server.
   */
  private class GetLocationsTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
      try {
        //TODO Return locations rather than modify activity field.
        locations = filmTourApplication.getService().getLocations().execute().body();
      } catch (IOException e) {
        //TODO Handle or don't.
      }
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      checkForPastTitle();
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
        //TODO Return locations rather than modify activity field.
        productions = filmTourApplication.getService().getProductions().execute().body();
      } catch (IOException e) {
        //TODO Handle or don't.
      }
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
      populateTitlesList();
      new GetLocationsTask().execute();
    }
  }
}
package edu.cnm.deepdive.abq_film_tour.controller;

import android.Manifest.permission;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
import edu.cnm.deepdive.abq_film_tour.FilmTourApplication;
import edu.cnm.deepdive.abq_film_tour.view.CustomSnippetAdapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import retrofit2.Call;
import retrofit2.Response;

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
  static final String SHARED_PREF_LAST_TITLE = "last_title";
  /**
   * Shared preferences tag to reference a set of bookmarked location IDs.
   */
  static final String SHARED_PREF_BOOKMARKS = "bookmarks";
  /**
   * Initial zoom level for the map camera, should display a birds eye view of the ABQ area.
   */
  private static final float ZOOM_LEVEL_INITIAL = 11;
  /**
   * Zoom level for the camera when "Near Me" is selected.
   */
  private static final float ZOOM_LEVEL_NEAR_ME = 17;
  /**
   * Initial bearing level for the map camera, sets the top of the map north
   */
  private static final float BEARING_LEVEL_INITIAL = 0;
  /**
   * Bearing level for the camera when "Near Me" is selected, changing this skews the map direction
   * and should be avoided.
   */
  private static final float BEARING_LEVEL_NEAR_ME = 0;
  /**
   * Initial tilt level for the map camera.
   */
  private static final float TILT_LEVEL_INITIAL = 0;
  /**
   * Tilt level for the camera when "Near Me" is selected.
   */
  private static final float TILT_LEVEL_NEAR_ME = 40;

  /**
   * Constant of the average radius of the earth in km, used to find the distance between two
   * coordinates.
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
   * Bounds of the Albuquerque area in km to determine if the user is close enough to use the app.
   */
  private static final int BURQUE_LIMITS = 50;
  /**
   * Range in km from the user location "Near me" should populate map markers.
   */
  private static final double KM_RANGE_FROM_USER = 1.5;
  /**
   * Extras key to pass a list of production titles into a selection dialog.
   */
  static final String TITLE_LIST_KEY = "titlesList";
  /**
   * Extras tag to pass a location UUID String into a LocationActivity.
   */
  static final String LOCATION_ID_KEY = "location_id_key";
  /**
   * Extras key to pass in a selection menu item.
   */
  static final String SELECTED_OPTIONS_MENU_ITEM_KEY = "selectedOptionMenuItem";
  /**
   * Constant for the onRequestPermissionsResult callback.
   */
  private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 11;
  /**
   * Log tag for this activity.
   */
  private static final String ERROR_LOG_TAG_MAPS_ACTIVITY = "MapsActivity";
  /**
   * Reference key for the user's latitude.
   */
  static final String USER_LOCATION_LAT_KEY = "userLocationLat";
  /**
   * Reference key for the user's longitude.
   */
  static final String USER_LOCATION_LONG_KEY = "userLocationLong";
  /**
   * Time in milliseconds to refresh location check.
   */
  private static final int REFRESH_LOCATION_MILLISECONDS = 2000;
  /**
   * Distance to refresh location check.
   */
  private static final int REFRESH_MIN_DISTANCE = 10;
  /**
   * Width of the custom map markers.
   */
  private static final int MAP_MARKER_WIDTH = 52;
  /**
   * URL to the Usage Instructions page on the project homepage.
   */
  private static final String HELP_URL = "https://abqfilmtour.github.io/docs/UsageInstructions.html";

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
   * Progress bar, becomes visible if there is lag when map pins are being displayed.
   */
  private ProgressBar progressSpinner;
  /**
   * Authentication token
   */
  private String token;
  /**
   * String set of bookmarked location IDs.
   */
  private Set<String> bookmarks;

  /**
   * Location Listener object to find when user location changes
   */
  private LocationListener locationListenerGPS = new LocationListener() {

    @Override
    public void onLocationChanged(android.location.Location location) {
      double latitude = location.getLatitude();
      double longitude = location.getLongitude();
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
    token = getString(R.string.oauth2_header, filmTourApplication.getAccount().getIdToken());
    //TODO refresh token to ensure it isn't stale
    setContentView(R.layout.activity_maps);
    progressSpinner = findViewById(R.id.progress_spinner);
    progressSpinner.setVisibility(View.VISIBLE);
    new GetProductionsTask().execute();
    sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    bookmarks = sharedPref.getStringSet(SHARED_PREF_BOOKMARKS, new HashSet<>());
    filmTourApplication.getAccount().getId(); //Pointless code?
    FilmTourApplication.getInstance().getAccount().getId(); //Pointless code?
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
    bookmarks = sharedPref.getStringSet(SHARED_PREF_BOOKMARKS, new HashSet<>());
  }

  @Override
  protected void onResume() {
    super.onResume();
    //Assigns bookmarks to set stored in SharedPref, creates a new HashSet if not available
    bookmarks = sharedPref.getStringSet(SHARED_PREF_BOOKMARKS, new HashSet<>());
  }


  /**
   * Given a location, this method animates the map camera and zooms in or out and populates map
   * pins of nearby filming locations.
   */
  private void animateCamera(LatLng targetCoordinates, float zoomLevelInitial,
      float bearingLevelInitial,
      float tiltLevelInitial) {
    CameraPosition cameraPosition = new CameraPosition.Builder()
        .target(targetCoordinates) // Sets the center of the map to center of Albuquerque
        .zoom(zoomLevelInitial) // Sets the zoom
        .bearing(bearingLevelInitial) // Sets the orientation of the camera
        .tilt(tiltLevelInitial) // Sets the tilt of the camera
        .build(); // Creates a CameraPosition from the builder
    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
  }

  /**
   * Calculates the distance as the crow flies in km between two coordinates given latitude and
   * longitude. This method computes the central angle between the two coordinates using the
   * Haversine formula then multiplies the angle by the average circumference of the Earth to solve
   * for the arclength or great-circle distance between the two points. Note: this method is an
   * approximation that assumes the Earth is a perfect sphere.
   **/
  private static double calculateDistanceInKilometer(double userLat, double userLng,
      double venueLat, double venueLng) {
    //converts degrees to radians
    double latDistance = Math.toRadians(userLat - venueLat);
    double lngDistance = Math.toRadians(userLng - venueLng);
    // calculates a = hav(Theta) where Theta = central angle
    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
        + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(venueLat))
        * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
    //takes inverse haversine to solve for central angle
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    //multiplies by r to solve for d
    return (AVERAGE_RADIUS_OF_EARTH_KM * c);
  }

  /**
   * This method checks if a title is in the local SharedPreferences. If there is, it starts with
   * the data for that production, if not, it prompts the user to select a title. If the user
   * selected their "Create Your Own Tour" last, it loads the bookmarked data.
   */
  private void checkForPastTitle() {
    String savedTitle = sharedPref.getString(SHARED_PREF_LAST_TITLE, null);
    if (savedTitle != null) {
      if (savedTitle.equals(SHARED_PREF_BOOKMARKS)) {
        //Hopefully, nobody ever films a movie or series in Albuquerque titled "Bookmarks".
        populateMapFromBookmarks();
      } else {
        populateMapFromTitle(savedTitle);
      }
    } else {
      Toast.makeText(MapsActivity.this, R.string.startup_select_title, Toast.LENGTH_LONG).show();
      setTitle(getString(R.string.title_application));
    }
  }

  /**
   * Takes a custom drawable file and converts it to Bitmap. The BitmapDescriptorFactory.fromBitmap()
   * method will use converted bitmap to create the custom marker for us.
   */
  private static Bitmap createCustomMarker(Context context) {
    View marker = ((LayoutInflater) Objects.requireNonNull(context.getSystemService(
        Context.LAYOUT_INFLATER_SERVICE))).inflate(R.layout.custom_marker_layout, null);
    DisplayMetrics displayMetrics = new DisplayMetrics();
    ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    marker.setLayoutParams(
        new ViewGroup.LayoutParams(MAP_MARKER_WIDTH, ViewGroup.LayoutParams.WRAP_CONTENT));
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
    if (location.getSiteName().equals("null")) {
      //Skip locations with a "null" site name.
      return;
    }
    LatLng coordinates = new LatLng(Double.valueOf(location.getLatCoordinate()),
        Double.valueOf(location.getLongCoordinate()));
    Marker marker = map.addMarker(new MarkerOptions()
        .position(coordinates)
        .icon(BitmapDescriptorFactory.fromBitmap(
            createCustomMarker(MapsActivity.this)))
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
   * Gets the last known location of the device using the best provider possible. If the user is
   * outside of Albuquerque city limits it will toast the user and not update the map to user's
   * location.
   */
  private LatLng getDeviceLocation() throws SecurityException {
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
        return userLatLng;
      }
    } else {
      Toast.makeText(this, R.string.null_location, Toast.LENGTH_LONG).show();
    }
    //If the code makes it here, a location has not been successfully returned.
    //Returns a 0,0 LatLng instead of null to avoid a null pointer.
    return new LatLng(0, 0);
  }

  /**
   * Requests location permission of the user's device. The result of the permission request is
   * handled by a callback, onRequestPermissionsResult.
   */
  private void getLocationPermission() {
    ActivityCompat.requestPermissions(this,
        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
            permission.ACCESS_COARSE_LOCATION},
        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
  }

  /**
   * Retrieves a locally saved Production from the title in shared preferences (assuming it exists).
   * This way a production can be retrieved without having to call the database again.
   *
   * @return a production matching a given title
   */
  Production getProductionFromSavedTitle() {
    String savedTitle = sharedPref.getString(SHARED_PREF_LAST_TITLE, null);
    assert savedTitle != null;
    assert !(savedTitle.equals(SHARED_PREF_BOOKMARKS));
    for (Production production : productions) {
      if (production.getTitle() == null) {
        //Necessary check, there's still some bad data, this will avoid a null pointer from screwing things up for now
        continue;
      }
      if (production.getTitle().equals(savedTitle)) {
        return production;
      }
    }
    return null; // Code should not reach this point unless a title somehow is not available.
  }

  /**
   * Checks to see if user is inside Albuquerque city limits.
   */
  private boolean inBurque(LatLng userLatLng) {
    double delta = calculateDistanceInKilometer(userLatLng.latitude, userLatLng.longitude,
        BURQUE_LAT, BURQUE_LONG);
    return (delta < BURQUE_LIMITS);
  }

  /**
   * Checks to see if device's location is enabled.
   */
  private void isLocationEnabled() {
    //FIXME Alert dialog pops up like three times
    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
      AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.AlertDialog);
      alertDialog.setTitle(R.string.enable_loc);
      alertDialog
          .setMessage(R.string.enable_location_settings);
      alertDialog.setPositiveButton(R.string.loc_settings, (dialog, which) -> {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
      });
      alertDialog.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());
      AlertDialog alert = alertDialog.create();
      alert.show();
    }
  }

  /**
   * Given the user's last known location, this method calls the animateCamera method to move the
   * map camera and zoom into the user's location.
   */
  private void nearMe(LatLng userLatLng) {
    populateMapFromLocation(userLatLng);
    animateCamera(userLatLng, ZOOM_LEVEL_NEAR_ME, BEARING_LEVEL_NEAR_ME, TILT_LEVEL_NEAR_ME);
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
    SelectionDialog selectionDialog;
    boolean handled = true;
    LatLng location;
    Bundle arguments = new Bundle();
    if (this.progressSpinner.getVisibility() == View.VISIBLE) {
      Toast.makeText(this, getString(R.string.please_wait), Toast.LENGTH_SHORT).show();
      return false;
    }
    switch (item.getItemId()) {
      default:
        handled = super.onOptionsItemSelected(item);
        break;
      case R.id.menu_all_locations:
        this.setTitle(getString(R.string.title_all_locations));
        populateMapFromAll();
        break;
      case R.id.menu_all_near_me:
        progressSpinner.setVisibility(View.VISIBLE);
        location = getDeviceLocation();
        if (location.latitude == 0 & location.longitude == 0) {
          // Do nothing. LatLng was invalid and getDeviceLocation should have returned an error message.
        } else {
          setTitle(getString(R.string.title_near_me));
          nearMe(location);
        }
        progressSpinner.setVisibility(View.GONE);
        break;
      case R.id.menu_television:
        selectionDialog = new SelectionDialog();
        arguments.putString(SELECTED_OPTIONS_MENU_ITEM_KEY,
            getString(R.string.selected_options_series_title));
        arguments.putStringArrayList(TITLE_LIST_KEY, tvTitles);
        selectionDialog.setArguments(arguments);
        selectionDialog.show(getSupportFragmentManager(), "dialog");
        break;
      case R.id.menu_film:
        selectionDialog = new SelectionDialog();
        arguments.putString(SELECTED_OPTIONS_MENU_ITEM_KEY,
            getString(R.string.selected_options_films_title));
        arguments.putStringArrayList(TITLE_LIST_KEY, movieTitles);
        selectionDialog.setArguments(arguments);
        selectionDialog.show(getSupportFragmentManager(), "dialog");
        break;
      case R.id.menu_bookmarks:
        progressSpinner.setVisibility(View.VISIBLE);
        if (bookmarks.isEmpty()) {
          Toast.makeText(this, getString(R.string.menu_no_bookmarks), Toast.LENGTH_LONG).show();
        } else {
          this.setTitle(getString(R.string.title_bookmarks));
          zoomOut();
          populateMapFromBookmarks();
        }
        progressSpinner.setVisibility(View.GONE);
        break;
      case R.id.menu_submit:
        String savedTitle = sharedPref.getString(SHARED_PREF_LAST_TITLE, null);
        if (savedTitle == null || savedTitle.equals(SHARED_PREF_BOOKMARKS)) {
          Toast.makeText(this, R.string.menu_no_savedtitle, Toast.LENGTH_LONG).show();
          break;
        }
        location = getDeviceLocation();
        if (location.latitude == 0 & location.longitude == 0) {
          // Do nothing. LatLng was invalid and getDeviceLocation should have returned an error message.
        } else {
          animateCamera(location, ZOOM_LEVEL_NEAR_ME, BEARING_LEVEL_NEAR_ME, TILT_LEVEL_NEAR_ME);
          SubmitLocationDialog submitLocationDialog = new SubmitLocationDialog();
          arguments.putDouble(USER_LOCATION_LAT_KEY, location.latitude);
          arguments.putDouble(USER_LOCATION_LONG_KEY, location.longitude);
          submitLocationDialog.setArguments(arguments);
          submitLocationDialog.show(getSupportFragmentManager(), "dialog");
        }
        break;
      case R.id.help:
        Uri help = Uri.parse(HELP_URL);
        Intent hIntent = new Intent(Intent.ACTION_VIEW, help);
        startActivity(hIntent);
        break;
      case R.id.sign_out:
        filmTourApplication.signOut();
        break;
    }
    return handled;
  }

  /**
   * Handles the result of the permission request from getLocationPermission.
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
              REFRESH_LOCATION_MILLISECONDS,
              REFRESH_MIN_DISTANCE,
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
   * Populates the map with every map pin stored in the database.
   */
  private void populateMapFromAll() {
    map.clear();
    progressSpinner.setVisibility(View.VISIBLE);
    for (FilmLocation location : locations) {
      createMapMarker(location);
    }
    progressSpinner.setVisibility(View.GONE);
  }

  /**
   * Draws map pins for all filming locations that the user has saved in the bookmarks sharedpref
   * String set.
   */
  private void populateMapFromBookmarks() {
    map.clear();
    for (FilmLocation location : locations) {
      if (!location.isApproved()) {
        continue;
      }
      if (bookmarks.contains(location.getId().toString())) {
        createMapMarker(location);
      }
    }
    saveTitleToSharedPreferences(SHARED_PREF_BOOKMARKS);
  }

  /**
   * Draws map pins for all filming locations within a given distance of the user's location.
   */
  private void populateMapFromLocation(LatLng userLatLng) {
    map.clear();
    for (FilmLocation location : locations) {
      if (!location.isApproved()) {
        continue;
      }
      double venueLat = Double.valueOf(location.getLatCoordinate());
      double venueLng = Double.valueOf(location.getLongCoordinate());
      double delta = calculateDistanceInKilometer(userLatLng.latitude, userLatLng.longitude,
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
    this.setTitle(title);
    saveTitleToSharedPreferences(title);
    for (FilmLocation location : locations) {
      if (!location.isApproved()) {
        continue;
      }
      if (location.getProduction().getTitle() != null && location.getProduction().getTitle()
          .equals(title)) {
        createMapMarker(location);
      }
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
        // This occurrence should probably be logged somehow. For now, doing nothing is fine.
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
   * This method updates the local SharedPreferences with a given production title.
   *
   * @param title a production title as a raw string..
   */
  private void saveTitleToSharedPreferences(String title) {
    SharedPreferences.Editor editor = sharedPref.edit();
    editor.putString(SHARED_PREF_LAST_TITLE, title);
    editor.apply();
  }

  /**
   * This method calls the animateCamera method to move the map camera and zooms out to a bird's eye
   * view of Albuquerque.
   */
  private void zoomOut() {
    LatLng startCoordinates = new LatLng(BURQUE_LAT, BURQUE_LONG);
    animateCamera(startCoordinates, ZOOM_LEVEL_INITIAL, BEARING_LEVEL_INITIAL, TILT_LEVEL_INITIAL);
  }

  /**
   * Asynchronous task that retrieves the productions from the server. Returns a boolean if the
   * query was successful, displays an alert dialog and exits the app if not.
   *
   * Checks the version as well. If the application version does not match the version on the server
   * it is handled as an error.
   */
  private class GetProductionsTask extends AsyncTask<Void, Void, Boolean> {

    private String errorMessage = getString(R.string.error_default);

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
      boolean successfulQuery = false;
      Call<String> versionCheckCall = filmTourApplication.getService().getVersion(); //sets up a version call in addition to the production call
      Call<List<Production>> call = filmTourApplication.getService().getProductions(token);
      try {
        Response<List<Production>> response = call.execute();
        Response<String> versionCheckResponse = versionCheckCall.execute();
        //TODO Move this code to application class with generic predicate
        if (response.isSuccessful() && versionCheckResponse.isSuccessful()) { //checks both response calls (only evaluates first call)
          String serverVersion = versionCheckResponse.body(); //body of jscpeterson.com/server
          String localVersion = filmTourApplication.VERSION; //version constant in application class
          if (!localVersion.equals(serverVersion)) { //the big check!!
            //TODO Create window with link to site.
            errorMessage = getString(R.string.error_message_version); //updates the error message
            return successfulQuery; //return to break out of method with successfulQuery = false
          }
          productions = response.body();
          successfulQuery = true;
        } else {
          Log.d(ERROR_LOG_TAG_MAPS_ACTIVITY, String.valueOf(response.code()));
          errorMessage = filmTourApplication.getErrorMessageFromHttpResponse(response);
        }
      } catch (IOException e) {
        Log.d(ERROR_LOG_TAG_MAPS_ACTIVITY, e.getMessage());
        errorMessage = getString(R.string.error_io);
      }
      return successfulQuery;
    }

    @Override
    protected void onPostExecute(Boolean successfulQuery) {
      super.onPostExecute(successfulQuery);
      if (successfulQuery) {
        populateTitlesList();
        new GetLocationsTask().execute(); //we got the productions time to call for the locations
      } else {
        filmTourApplication.handleErrorMessage(MapsActivity.this, errorMessage);
      }
    }
  }

  /**
   * Asynchronous task that retrieves the film locations from the server. Returns a boolean if the
   * query was successful, displays an alert dialog and exits the app if not.
   */
  private class GetLocationsTask extends AsyncTask<Void, Void, Boolean> {

    private String errorMessage = getString(R.string.error_default);

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
      boolean successfulQuery = false;
      try {
        Call<List<FilmLocation>> call = filmTourApplication.getService().getLocations(token);
        Response<List<FilmLocation>> response = call.execute();
        if (response.isSuccessful()) {
          locations = response.body();
          successfulQuery = true;
        } else {
          Log.d(ERROR_LOG_TAG_MAPS_ACTIVITY, String.valueOf(response.code()));
          errorMessage = filmTourApplication.getErrorMessageFromHttpResponse(response);
        }
      } catch (IOException e) {
        Log.d(ERROR_LOG_TAG_MAPS_ACTIVITY, e.getMessage());
        errorMessage = getString(R.string.error_io);
      }
      return successfulQuery;
    }

    @Override
    protected void onPostExecute(Boolean successfulQuery) {
      if (successfulQuery) {
        checkForPastTitle(); //see what we've got in shared pref
        progressSpinner.setVisibility(View.GONE); //all the work is done the spinner can go now
      } else {
        filmTourApplication.handleErrorMessage(MapsActivity.this, errorMessage);
      }
    }
  }
}
package edu.cnm.deepdive.abq_film_tour.controller;

import android.Manifest.permission;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import de.hdodenhof.circleimageview.CircleImageView;
import edu.cnm.deepdive.abq_film_tour.R;
import edu.cnm.deepdive.abq_film_tour.model.entity.FilmLocation;
import edu.cnm.deepdive.abq_film_tour.model.entity.Production;
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
  private static final String TYPE_SERIES = "series";
  private static final String TYPE_MOVIE = "movie";
  private static final String LOCATION_ID_KEY = "location_id_key";
  private static final float ZOOM_LEVEL_INITIAL = 9;
  private static final float ZOOM_LEVEL_NEAR_ME = 17;
  private static final float BEARING_LEVEL_NEAR_ME = 90;
  private static final float TILT_LEVEL_NEAR_ME = 40;

  //First result on google when I searched "city of albuquerque coordinates"
  private static final String START_LONG = "-106.6055534";
  private static final String START_LAT = "35.0853336";
  public static final String TITLE_LIST_KEY = "titlesList";
  public static final String SELECTED_OPTIONS_MENU_ITEM_KEY = "selectedOptionMenuItem";
  private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 11;

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

  private static FilmLocation startLocation; //Should this be a FilmLocation? Only coordinates needed
  LocationManager locationManager;
  Context context;


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
    actionBar.setLogo(R.drawable.toolbar_icon);
    actionBar.setDisplayUseLogoEnabled(true);
    actionBar.setDisplayShowHomeEnabled(true);
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.

    context = this;
    locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    getLocationPermission();
    isLocationEnabled();
    if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {

      return;
    }
    Criteria criteria = new Criteria();
    criteria.setAccuracy(Criteria.ACCURACY_COARSE);
    locationManager.requestLocationUpdates(locationManager.getBestProvider(criteria, true),
        2000,
        1,
        locationListenerGPS);

  }

  LocationListener locationListenerGPS = new LocationListener() {

    @Override
    public void onLocationChanged(android.location.Location location) {
      double latitude = location.getLatitude();
      double longitude = location.getLongitude();
      String msg = "New Latitude: " + latitude + "New Longitude: " + longitude;
      //Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
  };

  @Override
  protected void onPostResume() {
    super.onPostResume();
    isLocationEnabled();
  }

  private void isLocationEnabled() {
    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
      /*AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
      alertDialog.setTitle("Enable Location");
      alertDialog
          .setMessage("Your locations setting is not enabled. Please enabled it in settings menu.");
      alertDialog.setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
          Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
          startActivity(intent);
        }
      });
      alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
          dialog.cancel();
        }
      });
      AlertDialog alert = alertDialog.create();
      alert.show();
    } else {
      AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
      alertDialog.setTitle("Confirm Location");
      alertDialog.setMessage("Select a title to begin!");
      alertDialog.setNegativeButton("Back to interface", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
          dialog.cancel();
        }
      });
      AlertDialog alert = alertDialog.create();
      alert.show();*/
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
        //TODO Record if null data is in the database
        continue; // skip null data
      } else if (production.getType().equals(TYPE_SERIES)) {
        tvTitles.add(production.getTitle());
      } else if (production.getType().equals(TYPE_MOVIE)) {
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
        getDeviceLocation();
        break;
      case R.id.menu_television:
        selectionDialog = new SelectionDialog();
        arguments = new Bundle();
        arguments.putString(SELECTED_OPTIONS_MENU_ITEM_KEY, getString(R.string.selected_options_series_title));
        arguments.putStringArrayList(TITLE_LIST_KEY, tvTitles);
        selectionDialog.setArguments(arguments);
        selectionDialog.show(getSupportFragmentManager(), "dialog");
        break;
      case R.id.menu_film:
        selectionDialog = new SelectionDialog();
        arguments = new Bundle();
        arguments.putString(SELECTED_OPTIONS_MENU_ITEM_KEY, getString(R.string.selected_options_films_title));
        arguments.putStringArrayList(TITLE_LIST_KEY, movieTitles);
        selectionDialog.setArguments(arguments);
        selectionDialog.show(getSupportFragmentManager(), "dialog");
        break;
      case R.id.menu_submit:
        submitDialog = new SubmitDialog();
        submitDialog.show(getSupportFragmentManager(), "dialog");
        break;
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
    map.moveCamera(CameraUpdateFactory.zoomTo(ZOOM_LEVEL_INITIAL));

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
      this.setTitle(title);
      if (location.getProduction().getTitle() != null && location.getProduction().getTitle()
          .equals(title)) {
        LatLng coordinates = new LatLng(Double.valueOf(location.getLongCoordinate()),
            Double.valueOf(location.getLatCoordinate()));
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
          intent.putExtra(LOCATION_ID_KEY, taggedLocation.getId().toString());
          startActivity(intent);
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
      Toast.makeText(MapsActivity.this, R.string.startup_select_title, Toast.LENGTH_LONG).show();
    }
  }

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

  @Override
  public void onRequestPermissionsResult(int requestCode,
      String permissions[], int[] grantResults) {
    switch (requestCode) {
      case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION)
              != PackageManager.PERMISSION_GRANTED
              && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION)
              != PackageManager.PERMISSION_GRANTED) {
            //    do nothing. Permissions will always be granted here.
            return;
          }
          locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
              2000,
              10,
              locationListenerGPS);
        } else {
          // permission denied, boo!
          Toast.makeText(this, "Cannot enable location.",
              Toast.LENGTH_LONG).show();
        }
      }
    }
  }

  public void getDeviceLocation() {
    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {

    } else {
      Criteria criteria = new Criteria();
      criteria.setAccuracy(Criteria.ACCURACY_COARSE);
      Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));
      if (location != null) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
            .target(new LatLng(location.getLatitude(),
                location.getLongitude()))      // Sets the center of the map to location user
            .zoom(ZOOM_LEVEL_NEAR_ME)                   // Sets the zoom
            .bearing(BEARING_LEVEL_NEAR_ME)                // Sets the orientation of the camera to east
            .tilt(TILT_LEVEL_NEAR_ME)                   // Sets the tilt of the camera to 30 degrees
            .build();                   // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
      }
    }
  }

  private static Bitmap createCustomMarker(Context context, @DrawableRes int resource) {

    View marker = ((LayoutInflater) context.getSystemService(
        Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);

    CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
    markerImage.setImageResource(resource);

    DisplayMetrics displayMetrics = new DisplayMetrics();
    ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
    marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
    marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
    marker.buildDrawingCache();
    Bitmap bitmap = Bitmap.createBitmap(
        marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    marker.draw(canvas);

    return bitmap;
  }
}
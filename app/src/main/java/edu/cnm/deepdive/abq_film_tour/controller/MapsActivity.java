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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.abq_film_tour.R;
import edu.cnm.deepdive.abq_film_tour.model.entity.FilmLocation;
import edu.cnm.deepdive.abq_film_tour.model.entity.Production;
import edu.cnm.deepdive.abq_film_tour.model.entity.User;
import edu.cnm.deepdive.abq_film_tour.model.entity.UserComment;
import edu.cnm.deepdive.abq_film_tour.service.FilmTourApplication;
import edu.cnm.deepdive.abq_film_tour.service.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The type Maps activity.
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {


  private static final float ZOOM_LEVEL = 15;

  private ArrayList<String> movieTitles;
  private ArrayList<String> tvTitles;
  private List<FilmLocation> locations;
  private List<Production> productions;
  private GoogleMap map;
  private SelectionDialog selectionDialog;
  private FusedLocationProviderClient fusedLocationProviderClient;
  private Bundle arguments;

  public static FilmLocation exampleLocation;
  public static Production exampleProduction;
  public static User exampleUser;
  public static UserComment exampleComment;

  private FilmTourApplication filmTourApplication;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    filmTourApplication = (FilmTourApplication) getApplication();
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);
    new GetProductionsTask().execute();
    ActionBar actionBar = getSupportActionBar();
    actionBar.setLogo(R.mipmap.ic_filmtour_round);
    actionBar.setDisplayUseLogoEnabled(true);
    actionBar.setDisplayShowHomeEnabled(true);
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
    createExampleData();
  }


  private void createExampleData() {
    exampleUser = new User();
    exampleUser.setGoogleName("Walter White");
    exampleUser.setGoogleId("12345");
    exampleUser.setGmailAddress("walterwhite@gmail.com");

    exampleProduction = new Production();
    exampleProduction.setImdbID("tt0903747");
    exampleProduction.setTitle("Breaking Bad");
    exampleProduction.setType("series");
    exampleProduction.setReleaseYear("2008-2013");
    exampleProduction.setPlot(
        "A high school chemistry teacher diagnosed with inoperable lung cancer turns to manufacturing and selling methamphetamine in order to secure his family's future.");

    exampleLocation = new FilmLocation();
    exampleLocation.setSiteName("The Dog House Drive In");
    exampleLocation.setLongCoordinate("35.0879012653313");
    exampleLocation.setLatCoordinate("-106.661403252821");
    exampleLocation.setProduction(exampleProduction);
    exampleLocation
        .setOriginalDetails("The Dog House - 1216 Central - ITC on Central from 11th to 14th St.");
    exampleLocation.setAddress("1216 Central Avenue NW");
    exampleLocation.setShootDate(1216857600000L);

    exampleComment = new UserComment();
    exampleComment.setContent(
        "Shot on 07/23/2008 at 1216 Central Avenue NW. The Dog House - 1216 Central - ITC on Central from 11th to 14th St.");
  }

  private void createDummyTitles() {
    movieTitles = new ArrayList<>();
    tvTitles = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      movieTitles.add("Movie Title " + i);
      tvTitles.add("TV Title " + i);
    }
  }

  private void createNotDummyTitles() {
    movieTitles = new ArrayList<>();
    tvTitles = new ArrayList<>();
    for (Production production : productions) {
      if (production.getType() == null) {
        System.out.println(production.getTitle() + "IS NULL!!");
        continue;
      }
      else if (production.getType().equals("series")) {
        tvTitles.add(production.getTitle());
      }
      else if (production.getType().equals("movie")) {
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
    String TITLE_SELECTED;
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
    System.out.println("map ready");
    new PopulateMapPinsTask().execute();
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
      createNotDummyTitles();
      System.out.println("productions in");
    }
  }

  private class PopulateMapPinsTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected void onPreExecute() {
      System.out.println("on pre execute");
      super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
      System.out.println("execution");
      try {
        locations = filmTourApplication.getService().getLocations().execute().body();
        System.out.println("hello " + locations.get(0).getSiteName());
      } catch (IOException e) {
        System.out.println("oh no!!");
      }
      return null;

    }

    @Override
    protected void onPostExecute(Void aVoid) {
      System.out.println("on post execute");
      for (FilmLocation location : locations) {
        System.out.println(location.getId());
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
            String test = location.getId().toString();
            Intent intent = new Intent(MapsActivity.this, LocationActivity.class);
            intent.putExtra("locationID", test);
            startActivity(intent);
          }
        });
        LatLng startCoordinates = new LatLng(Double.valueOf(exampleLocation.getLongCoordinate()),
            Double.valueOf(exampleLocation.getLatCoordinate()));
        map.moveCamera(CameraUpdateFactory.newLatLng(startCoordinates));
        map.moveCamera(CameraUpdateFactory.zoomTo(ZOOM_LEVEL));
      }
    }
  }
}
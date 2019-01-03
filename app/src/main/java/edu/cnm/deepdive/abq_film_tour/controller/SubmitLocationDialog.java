package edu.cnm.deepdive.abq_film_tour.controller;

import static edu.cnm.deepdive.abq_film_tour.controller.MapsActivity.USER_LOCATION_LAT_KEY;
import static edu.cnm.deepdive.abq_film_tour.controller.MapsActivity.USER_LOCATION_LONG_KEY;
import static edu.cnm.deepdive.abq_film_tour.controller.MapsActivity.SHARED_PREF_LAST_TITLE;
import static edu.cnm.deepdive.abq_film_tour.controller.MapsActivity.SHARED_PREF_BOOKMARKS;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import edu.cnm.deepdive.abq_film_tour.R;
import edu.cnm.deepdive.abq_film_tour.model.entity.FilmLocation;
import edu.cnm.deepdive.abq_film_tour.model.entity.Production;
import edu.cnm.deepdive.abq_film_tour.FilmTourApplication;
import java.io.IOException;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Dialog fragment for users to upload and submit changes.
 */
public class SubmitLocationDialog extends DialogFragment implements View.OnClickListener{

  private static final int RESULT_LOAD_IMAGE = 1;

  private MapsActivity parentMap;
  private Production production;
  private String token;
  private TextInputLayout siteNameInput;
  private FilmTourApplication filmTourApplication;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    parentMap = (MapsActivity) getActivity();
    assert parentMap != null;
    filmTourApplication = (FilmTourApplication) parentMap.getApplication();
    token = getString(R.string.oauth2_header, filmTourApplication.getAccount().getIdToken());
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(parentMap);
    String savedTitle = sharedPref.getString(SHARED_PREF_LAST_TITLE, null); //This may not even be necessary?
    if (savedTitle == null || savedTitle.equals(SHARED_PREF_BOOKMARKS)) {
      dismiss(); //All incoming cases should be handled, but if for some reason the savedTitle is invalid, kill the fragment.
    }
    production = parentMap.getProductionFromSavedTitle();
    View view = inflater.inflate(R.layout.submit_location_fragment, null, false);
    parentMap = (MapsActivity)getActivity();
    Button submitButton = view.findViewById(R.id.submit);
    submitButton.setText(String.format(getString(R.string.submit_location_button_format), production.getTitle()));
    submitButton.setOnClickListener(this);
    siteNameInput = view.findViewById(R.id.sitename_input);
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    WindowManager.LayoutParams params = Objects.requireNonNull(getDialog().getWindow()).getAttributes();
    params.width = LayoutParams.MATCH_PARENT;
    getDialog().getWindow().setAttributes(params);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.submit:
        SubmitLocationTask task = new SubmitLocationTask(
            Objects.requireNonNull(siteNameInput.getEditText()).getText().toString(),
            getUserLocation(),
            production);
        task.execute();
        break;
    }
  }

  public LatLng getUserLocation() {
    assert getArguments() != null;
    return new LatLng(getArguments().getDouble(USER_LOCATION_LAT_KEY), getArguments().getDouble(USER_LOCATION_LONG_KEY));
  }

  private class SubmitLocationTask extends AsyncTask<Void, Void, Boolean> {

    String siteName;
    LatLng location;
    Production production;
    FilmLocation newFilmLocation;

    SubmitLocationTask(String siteName, LatLng location,
        Production production) {
      this.siteName = siteName;
      this.location = location;
      this.production = production;
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      newFilmLocation = new FilmLocation();
      newFilmLocation.setProduction(production);
      newFilmLocation.setGoogleId(filmTourApplication.getAccount().getId());
      newFilmLocation.setSiteName(this.siteName);
      newFilmLocation.setLatCoordinate(String.valueOf(this.location.latitude));
      newFilmLocation.setLongCoordinate(String.valueOf(this.location.longitude));
      newFilmLocation.setApproved(true); // TODO Remove me!
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
      boolean successfulQuery = false;
      Call<FilmLocation> locationCall = filmTourApplication.getService()
          .postFilmLocation(token, newFilmLocation);
      try {
        Response<FilmLocation> response = locationCall.execute();
        if (response.isSuccessful()) {
          successfulQuery = true;
        }
      } catch (IOException e) {
        //TODO Handle error
      }
      return successfulQuery;
    }

    @Override
    protected void onPostExecute(Boolean successfulQuery) {
      super.onPostExecute(successfulQuery);
      if (successfulQuery) {
        Toast.makeText(parentMap, R.string.submission_successful, Toast.LENGTH_SHORT).show();
        dismiss();
      } else {
        Toast.makeText(parentMap, R.string.submission_failed, Toast.LENGTH_SHORT).show();
        dismiss();
      }
    }
  }
}
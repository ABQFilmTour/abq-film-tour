package edu.cnm.deepdive.abq_film_tour.controller;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import edu.cnm.deepdive.abq_film_tour.FilmTourApplication;
import edu.cnm.deepdive.abq_film_tour.R;
import edu.cnm.deepdive.abq_film_tour.model.entity.FilmLocation;
import edu.cnm.deepdive.abq_film_tour.model.entity.Image;
import edu.cnm.deepdive.abq_film_tour.model.entity.UserComment;
import retrofit2.Call;
import retrofit2.Response;

import static edu.cnm.deepdive.abq_film_tour.controller.LocationActivity.ERROR_LOG_TAG_LOCATION_ACTIVITY;
import static edu.cnm.deepdive.abq_film_tour.controller.MapsActivity.LOCATION_ID_KEY;

public class ImageGalleryActivity extends AppCompatActivity {

    private FilmTourApplication filmTourApplication;
    private String token;
    private FilmLocation location;
    private List<Image> images;

    private TextView sampleTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        filmTourApplication = (FilmTourApplication) getApplication();
        token = getString(R.string.oauth2_header,
                FilmTourApplication.getInstance().getAccount().getIdToken());

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        String locationID = extras.getString(LOCATION_ID_KEY);
        UUID locationUUID = UUID.fromString(locationID);

        setContentView(R.layout.activity_location);

        sampleTextView = findViewById(R.id.sample_text_view);
        sampleTextView.setText("Making network call...");
        new ImageGalleryTask().execute(locationUUID);

    }


    public class ImageGalleryTask extends AsyncTask<UUID, Void, Boolean> {

        private String errorMessage = getString(R.string.error_default);

        @Override
        protected Boolean doInBackground(UUID... UUIDs) {
            boolean successfulQuery = false;
            try {
                Call<FilmLocation> locationCall = filmTourApplication.getService()
                            .getFilmLocation(token, UUIDs[0]);
                Call<List<Image>> imageCall = filmTourApplication.getService()
                            .getImages(token, UUIDs[0]);
                Response<FilmLocation> locationCallResponse = locationCall.execute();
                Response<List<Image>> imageCallResponse = imageCall.execute();
                    if (locationCallResponse.isSuccessful() && imageCallResponse.isSuccessful()) {
                        location = locationCallResponse.body();
                        images = imageCallResponse.body();
                        successfulQuery = true;
                    } else {
                        Log.d(ERROR_LOG_TAG_LOCATION_ACTIVITY, String.valueOf(locationCallResponse.code()));
                        Log.d(ERROR_LOG_TAG_LOCATION_ACTIVITY, String.valueOf(imageCallResponse.code()));
                        errorMessage = filmTourApplication.getErrorMessageFromHttpResponse(locationCallResponse);
                    }
                } catch (IOException e) {
                    Log.d(ERROR_LOG_TAG_LOCATION_ACTIVITY, e.getMessage());
                    errorMessage = getString(R.string.error_io);
                }
                return successfulQuery;
            }

         @Override
         protected void onPostExecute(Boolean successfulQuery) {
            if (successfulQuery) {
                super.onPostExecute(successfulQuery);

                sampleTextView.setText("Successful call!\n" + images.toString());

            } else {
                filmTourApplication.handleErrorMessage(ImageGalleryActivity.this, errorMessage);
            }

        }

    }

}

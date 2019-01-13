package edu.cnm.deepdive.abq_film_tour.controller;


import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import edu.cnm.deepdive.abq_film_tour.FilmTourApplication;
import edu.cnm.deepdive.abq_film_tour.R;
import edu.cnm.deepdive.abq_film_tour.controller.LocationActivity;
import edu.cnm.deepdive.abq_film_tour.model.entity.FilmLocation;
import edu.cnm.deepdive.abq_film_tour.model.entity.Image;
import edu.cnm.deepdive.abq_film_tour.model.entity.Production;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import retrofit2.Call;
import retrofit2.Response;


public class SubmitImageDialog extends DialogFragment implements View.OnClickListener {

  private static final int RESULT_LOAD_IMAGE = 1;
  private static final String ERROR_LOG_TAG_IMAGE_DIALOG= "SubmitImageDialog";

  private ImageView userUploadImage;
  private Button userImageButton, sendImageButton;
  private FilmTourApplication filmTourApplication;
  private LocationActivity parentActivity;
  private FilmLocation location;
  private Production production;
  private Uri savedUri;
  private String token;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    filmTourApplication = (FilmTourApplication) getActivity().getApplication();
    parentActivity = (LocationActivity) getActivity();
    location = parentActivity.getLocation();
    production = parentActivity.getProduction();

    token = getString(R.string.oauth2_header, filmTourApplication.getAccount().getIdToken());
    View view = inflater.inflate(R.layout.submit_image_fragment, null, false);

    location = parentActivity.getLocation();

    userUploadImage = view.findViewById(R.id.user_upload_image);
    userImageButton = view.findViewById(R.id.user_image_button);
    sendImageButton = view.findViewById(R.id.send_image_button);

    userImageButton.setOnClickListener(this);
    sendImageButton.setOnClickListener(this);

    return view;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
      Uri selectedImage = data.getData();
      savedUri = selectedImage;
      userUploadImage.setImageURI(selectedImage);
    }
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
    switch (v.getId()) {
      case R.id.user_image_button:
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
        break;
      case R.id.send_image_button:
        if (savedUri != null) {
          Image newImage = new Image();
          newImage.setFilmLocation(location);
          newImage.setGoogleId(filmTourApplication.getAccount().getId());
          String requestId = MediaManager.get().upload(savedUri)
              .unsigned(getString(R.string.cloudinary_unsigned_preset)) //Does this need to be hidden?
              .option("tags", new String[] {location.getSiteName(), production.getTitle()})
              .callback(new UploadCallback() {
                @Override
                public void onStart(String requestId) {
                  //Do nothing
                }

                @Override
                public void onProgress(String requestId, long bytes, long totalBytes) {
                  //Do nothing
                }

                @Override
                public void onSuccess(String requestId, Map resultData) {
                  String url = resultData.get("url").toString();
                  newImage.setUrl(url);
                  new ImageUploadTask().execute(newImage);
                }

                @Override
                public void onError(String requestId, ErrorInfo error) {
                  Toast.makeText(parentActivity, "Failed to reach cloud service.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onReschedule(String requestId, ErrorInfo error) {
                  //Do nothing
                }
              })
              .dispatch();
          newImage.setApproved(true); //TODO Remove once security is tightened.
        } else {
          Toast.makeText(parentActivity, "Please select an image.", Toast.LENGTH_SHORT).show();
        }
    }
  }

  private class ImageUploadTask extends AsyncTask<Image, Void, Boolean> {

    @Override
    protected Boolean doInBackground(Image... images) {
      boolean successfulQuery = false;
      Call<Image> call = filmTourApplication.getService().postImage(token, images[0], location.getId());
      try {
        Response<Image> response = call.execute();
        if (response.isSuccessful()) {
          successfulQuery = true;
        } else {
          // Do nothing - toast in onPostExecute displays enough information for now.
        }
      } catch (IOException e) {
        // Do nothing - toast in onPostExecute displays enough information for now.
      }
      return successfulQuery;
    }

    @Override
    protected void onPostExecute(Boolean successfulQuery) {
      if (successfulQuery) {
        Toast.makeText(parentActivity, R.string.submission_successful, Toast.LENGTH_SHORT).show();
        dismiss();
      } else {
        Toast.makeText(parentActivity, R.string.submission_failed, Toast.LENGTH_SHORT).show();
        dismiss();
      }
    }
  }
}
package edu.cnm.deepdive.abq_film_tour.controller;

import static android.app.Activity.RESULT_OK;
import static edu.cnm.deepdive.abq_film_tour.controller.MapsActivity.USER_LOCATION_LAT_KEY;
import static edu.cnm.deepdive.abq_film_tour.controller.MapsActivity.USER_LOCATION_LONG_KEY;
import static edu.cnm.deepdive.abq_film_tour.controller.MapsActivity.SHARED_PREF_LAST_TITLE;
import static edu.cnm.deepdive.abq_film_tour.controller.MapsActivity.SHARED_PREF_BOOKMARKS;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.google.android.gms.maps.model.LatLng;
import edu.cnm.deepdive.abq_film_tour.R;
import edu.cnm.deepdive.abq_film_tour.model.entity.FilmLocation;
import edu.cnm.deepdive.abq_film_tour.model.entity.Production;
import edu.cnm.deepdive.abq_film_tour.service.FilmTourApplication;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.io.IOException;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Dialog fragment for users to upload and submit changes.
 */
public class SubmitDialog extends DialogFragment implements View.OnClickListener{
  private static final int RESULT_LOAD_IMAGE = 1;

  /**
   * The Uploaded image.
   */
  ImageView uploadImage;
  /**
   * Button to upload an image.
   */
  Button uploadImagebutton, /**
   * Button to register.
   */
  registerButton;
  /**
   * Field to enter the name of the site location.
   */
  EditText siteName, /**
   * Field to enter a short discription of the image.
   */
  description;

  private MapsActivity parentMap;
  private SharedPreferences sharedPref;
  private Production production;
  private String token;

  private TextInputLayout siteNameInput;
  private TextInputLayout descriptionInput;
  private FilmTourApplication filmTourApplication;


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    parentMap = (MapsActivity) getActivity();
    assert parentMap != null;
    filmTourApplication = (FilmTourApplication) parentMap.getApplication();
    token = getString(R.string.oauth2_header, filmTourApplication.getAccount().getIdToken());
    sharedPref = PreferenceManager.getDefaultSharedPreferences(parentMap);
    String savedTitle = sharedPref.getString(SHARED_PREF_LAST_TITLE, null); //This may not even be necessary?
    if (savedTitle == null || savedTitle.equals(SHARED_PREF_BOOKMARKS)) {
      dismiss(); //All incoming cases should be handled, but if for some reason the savedTitle is invalid, kill the fragment.
    }
    System.out.println(savedTitle);
    production = parentMap.getProductionFromSavedTitle();
    View view = inflater.inflate(R.layout.submit_fragment, null, false);
    parentMap = (MapsActivity)getActivity();

    uploadImage = view.findViewById(R.id.upload_image);
    uploadImagebutton = view.findViewById(R.id.upload_image_btn);
    registerButton = view.findViewById(R.id.register);
    registerButton.setText(String.format("Submit for %s", production.getTitle()));
    uploadImagebutton.setOnClickListener(this);
    registerButton.setOnClickListener(this);
    siteNameInput = view.findViewById(R.id.sitename_input);
    descriptionInput = view.findViewById(R.id.description_input);


    return  view;
  }

  @Override
  public void onResume() {
    super.onResume();

    ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
    params.width = LayoutParams.MATCH_PARENT;
    getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.upload_image_btn:
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
        break;
      case R.id.register:

        Bitmap image = ((BitmapDrawable) uploadImage.getDrawable()).getBitmap();
        Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        //uploads image to cloudinary
        //TODO change to submitted image
        String requestId = MediaManager.get().upload(R.drawable.back_ground)
            .unsigned("wggcxbzh")
            .option("site_name", "siteName")
            .option("tags", "production")
            //TODO set the siteName and production options to the actual values
            .dispatch();
        //TODO set up listener service and callback interface to check for progress of uploads
        Toast.makeText(parentMap, "image uploaded", Toast.LENGTH_LONG).show();
//        Bitmap image = ((BitmapDrawable) uploadImage.getDrawable()).getBitmap();
        FilmLocationSubmitTask task = new FilmLocationSubmitTask(
            siteNameInput.getEditText().getText().toString(),
            descriptionInput.getEditText().getText().toString(),
            getUserLocation(),
            production);
        task.execute();
        break;
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
      Uri selectedImage = data.getData();
      uploadImage.setImageURI(selectedImage);
    }
  }

  public LatLng getUserLocation() {
    assert getArguments() != null;
    return new LatLng(getArguments().getDouble(USER_LOCATION_LAT_KEY), getArguments().getDouble(USER_LOCATION_LONG_KEY));
  }

  private class FilmLocationSubmitTask extends AsyncTask<Void, Void, Boolean> {

    String siteName;
    String description;
    LatLng location;
    Production production;
    FilmLocation newFilmLocation;

    public FilmLocationSubmitTask(String siteName, String description, LatLng location, Production production) {
      this.siteName = siteName;
      this.description = description;
      this.location = location;
      this.production = production;
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      newFilmLocation = new FilmLocation();
      newFilmLocation.setSiteName(this.siteName);
      newFilmLocation.setLatCoordinate(String.valueOf(this.location.latitude));
      newFilmLocation.setLongCoordinate(String.valueOf(this.location.longitude));
      newFilmLocation.setProduction(production);
      newFilmLocation.setGoogleId(filmTourApplication.getAccount().getId());
      //TODO Create user comment with description info
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
      Boolean successfulQuery = false;
      Call<FilmLocation> locationCall = filmTourApplication.getService()
          .postFilmLocation(token, newFilmLocation);
      try {
        Response<FilmLocation> response = locationCall.execute();
        if (response.isSuccessful()) {
          successfulQuery = true;
        }
      } catch (IOException e) {
        //TODO Handle error
        e.printStackTrace();
      }
      return successfulQuery;
    }

    @Override
    protected void onPostExecute(Boolean successfulQuery) {
      super.onPostExecute(successfulQuery);
      if (successfulQuery) {
        Toast.makeText(parentMap, "Post submitted!", Toast.LENGTH_SHORT).show();
        dismiss();
      } else {
        Toast.makeText(parentMap, "Something went wrong", Toast.LENGTH_SHORT).show();
        dismiss();
      }
    }

  }

  private class ImageUploadTask extends AsyncTask<Void, Void, Void>{

    /**
     * The Image that is being uploaded.
     */
    Bitmap image;
    /**
     * The Site location name.
     */
    String siteName;
    /**
     * Description of the image.
     */
    String description;

    /**
     * Instantiates a new Register.
     *
     * @param image the uploaded image
     */
    public ImageUploadTask(Bitmap image){
      this.image = image;
    }

    @Override
    protected Void doInBackground(Void... voids) {

      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      image.compress(CompressFormat.JPEG, 100, byteArrayOutputStream);
      String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(),
          Base64.DEFAULT);

      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
    }
  }

  String requestId = MediaManager.get().upload("image name").callback(
      new com.cloudinary.android.callback.UploadCallback() {
        @Override
        public void onStart(String requestId) {

        }

        @Override
        public void onProgress(String requestId, long bytes, long totalBytes) {
          Double progress = (double) bytes/totalBytes;
          //TODO make spinner visible
        }

        @Override
        public void onSuccess(String requestId, Map resultData) {

        }

        @Override
        public void onError(String requestId, ErrorInfo error) {

          Toast.makeText(parentMap, "unable to load image.", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onReschedule(String requestId, ErrorInfo error) {

        }
      })
      .dispatch();

}

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
import android.provider.MediaStore.Images.Media;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.android.gms.maps.model.LatLng;
import edu.cnm.deepdive.abq_film_tour.R;
import edu.cnm.deepdive.abq_film_tour.model.entity.FilmLocation;
import edu.cnm.deepdive.abq_film_tour.model.entity.Production;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

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



  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    parentMap = (MapsActivity) getActivity();
    sharedPref = PreferenceManager.getDefaultSharedPreferences(parentMap);
    String savedTitle = sharedPref.getString(SHARED_PREF_LAST_TITLE, null); //This may not even be necessary?
    if (savedTitle == null || savedTitle.equals(SHARED_PREF_BOOKMARKS)) {
      dismiss(); //All incoming cases should be handled, but if for some reason the savedTitle is invalid, kill the fragment.
    }
    System.out.println(savedTitle);
    production = parentMap.getProductionFromSavedTitle();
    View view = inflater.inflate(R.layout.submit_fragment, null, false);

    uploadImage = view.findViewById(R.id.upload_image);

    uploadImagebutton = view.findViewById(R.id.upload_image_btn);
    registerButton = view.findViewById(R.id.register);

    registerButton.setText(String.format("Submit for %s", production.getTitle()));

    uploadImagebutton.setOnClickListener(this);
    registerButton.setOnClickListener(this);

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
    FilmLocation newLocation = new FilmLocation();
    switch (v.getId()){
      case R.id.upload_image_btn:
        Intent galleryIntenet = new Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntenet, RESULT_LOAD_IMAGE);
        break;
      case R.id.register:
        Bitmap image = ((BitmapDrawable) uploadImage.getDrawable()).getBitmap();
        //TODO POST a new Location with an associated GoogleId
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

  private class Register extends AsyncTask<Void, Void, Void>{

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
     * @param siteName the site location name
     * @param description the description of the image
     */
    public Register(Bitmap image, String siteName, String description){
      this.image = image;
      this.siteName = siteName;
      this.description = description;

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
}

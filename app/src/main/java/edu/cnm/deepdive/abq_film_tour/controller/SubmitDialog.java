package edu.cnm.deepdive.abq_film_tour.controller;


import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.Toast;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import edu.cnm.deepdive.abq_film_tour.R;
import java.io.ByteArrayOutputStream;
import java.util.Map;

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



  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.submit_fragment, null, false);
    parentMap = (MapsActivity)getActivity();

    uploadImage = view.findViewById(R.id.upload_image);

    uploadImagebutton = view.findViewById(R.id.upload_image_btn);
    registerButton = view.findViewById(R.id.register);

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

    switch (v.getId()){
      case R.id.upload_image_btn:
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
        break;
      case R.id.register:

        //Sam what does this do??
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

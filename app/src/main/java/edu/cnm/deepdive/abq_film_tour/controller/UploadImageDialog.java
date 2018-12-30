package edu.cnm.deepdive.abq_film_tour.controller;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.cloudinary.android.MediaManager;
import edu.cnm.deepdive.abq_film_tour.R;


public class UploadImageDialog extends DialogFragment implements View.OnClickListener {

  private static final int RESULT_LOAD_IMAGE = 1;

  ImageView userUploadImage;

  Button userImageButton, sendImageButton;

  private MapsActivity parentMap;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.submit_image_fragment, null, false);

    userUploadImage = view.findViewById(R.id.user_upload_image);
    userImageButton = view.findViewById(R.id.user_image_button);
    sendImageButton = view.findViewById(R.id.send_image_button);

    userImageButton.setOnClickListener(this);
    sendImageButton.setOnClickListener(this);

    return view;
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
    switch (v.getId()) {
      case R.id.user_image_button:
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
        break;
      case R.id.send_image_button:

        Bitmap image = ((BitmapDrawable) userUploadImage.getDrawable()).getBitmap();
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

}



package edu.cnm.deepdive.abq_film_tour.controller;


import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import edu.cnm.deepdive.abq_film_tour.R;

public class SubmitDialog extends DialogFragment implements View.OnClickListener{
  private static final int RESULT_LOAD_IMAGE = 1;

  ImageView uploadImage;
  Button uploadImagebutton, registerButton;
  EditText siteName, description;




  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.submit_fragment, null, false);
//    dialog.setContentView(view);
//    dialog.setCancelable(true);

    uploadImage = view.findViewById(R.id.upload_image);

    uploadImagebutton = view.findViewById(R.id.upload_image_btn);
    registerButton = view.findViewById(R.id.register);

    uploadImage.setOnClickListener(this);
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
        Intent galleryIntenet = new Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntenet, RESULT_LOAD_IMAGE);
        break;
      case R.id.register:
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
}

package edu.cnm.deepdive.abq_film_tour.controller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.cnm.deepdive.abq_film_tour.R;

public class SubmitCommentDialog extends DialogFragment implements View.OnClickListener {


  @Nullable
  public View onCreate(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    View view = inflater.inflate(R.layout.submit_comment_fragment, null, false);
    return view;
  }

  @Override
  public void onClick(View v) {

  }
}

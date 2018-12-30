package edu.cnm.deepdive.abq_film_tour.controller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import edu.cnm.deepdive.abq_film_tour.R;

public class SubmitCommentDialog extends DialogFragment implements View.OnClickListener {


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.submit_comment_fragment, null, false);

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

  }
}

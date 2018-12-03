package edu.cnm.deepdive.abq_film_tour.controller;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import edu.cnm.deepdive.abq_film_tour.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class SelectionDialog extends DialogFragment {

  private static final String TITLE_LIST_KEY="titlesList";
  private static final String TITLE_SELECTION_KEY="titleSelection";


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    //transparent color for dialog fragment
    final Drawable d = new ColorDrawable(Color.BLACK);
    d.setAlpha(130);
    View view = inflater.inflate(R.layout.fragment_selection, container,false);

    ListView selectionListView = view.findViewById(R.id.selection_list_view);
    Bundle arguments = this.getArguments();
    assert arguments != null;
    ArrayList titles = (ArrayList) arguments.get(TITLE_LIST_KEY);
    ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
        android.R.layout.simple_list_item_1, titles);
    selectionListView.setAdapter(adapter);
    //sets dialog fragment to transparent color
    getDialog().getWindow().setBackgroundDrawable(d);
    return view;
  }


//  @Override
//  public void onResume() {
//    ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
//    params.width = LayoutParams.MATCH_PARENT;
//    params.height = LayoutParams.WRAP_CONTENT;
//    //here I tried changing params
//    getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams)params);
//
//    another way also doesn't work:
//
//    int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
//    int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
//    getDialog().getWindow().setLayout(width, height);
//    super.onResume();
//  }
}


package edu.cnm.deepdive.abq_film_tour.controller;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
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
import javax.annotation.Nonnull;


public class SelectionDialog extends DialogFragment {

  public static final String TITLE_LIST_KEY="titlesList";
  public static final String SELECTED_OPTIONS_MENU_ITEM_KEY = "selectedOptionMenuItem";


  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
  super.onCreateDialog(savedInstanceState);
    AlertDialog.Builder builder = new Builder(getActivity());
//  builder.setTitle("MOVIE");

    View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_selection, null,false);
    Toolbar toolbar = view.findViewById(R.id.my_toolbar);
    toolbar.setTitle(getArguments().getString(SELECTED_OPTIONS_MENU_ITEM_KEY));
    ListView selectionListView = view.findViewById(R.id.selection_list_view);

    Bundle arguments = this.getArguments();
    assert arguments != null;
    ArrayList titles = (ArrayList) arguments.get(TITLE_LIST_KEY);

    ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
        android.R.layout.simple_list_item_1, titles);
    selectionListView.setAdapter(adapter);
    selectionListView.setVisibility(View.VISIBLE);


  builder.setView(view);
  return builder.create();
  }

  @Override
  public void onResume() {
    //this sets the dialog dimensions to the whole screen
    ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
    params.width = LayoutParams.MATCH_PARENT;
    params.height = LayoutParams.MATCH_PARENT;


    final Drawable d = new ColorDrawable(Color.BLACK);
    d.setAlpha(50);
    getDialog().getWindow().setBackgroundDrawable(d);

    getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams)params);
    super.onResume();
  }
}


package edu.cnm.deepdive.abq_film_tour.controller;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.widget.Toast;
import edu.cnm.deepdive.abq_film_tour.R;
import java.util.ArrayList;
import java.util.Objects;


public class SelectionDialog extends DialogFragment {

  public static final String TITLE_LIST_KEY="titlesList";
  public static final String SELECTED_OPTIONS_MENU_ITEM_KEY = "selectedOptionMenuItem";


  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
  super.onCreateDialog(savedInstanceState);
    AlertDialog.Builder builder = new Builder(getActivity());


    View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_selection, null,false);
    Toolbar toolbar = view.findViewById(R.id.my_toolbar);
    toolbar.setTitle(getArguments().getString(SELECTED_OPTIONS_MENU_ITEM_KEY));
    ListView selectionListView = view.findViewById(R.id.selection_list_view);

    Bundle arguments = this.getArguments();
    assert arguments != null;
    ArrayList<String> titles = (ArrayList) arguments.get(TITLE_LIST_KEY);


    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()),
        android.R.layout.simple_list_item_1, titles);
    selectionListView.setAdapter(adapter);
    selectionListView.setVisibility(View.VISIBLE);
    selectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println(titles.get(position));
        Toast.makeText(getContext(), titles.get(position), Toast.LENGTH_LONG).show();
      }
    });



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


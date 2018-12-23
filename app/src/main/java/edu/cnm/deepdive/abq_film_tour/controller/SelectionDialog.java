package edu.cnm.deepdive.abq_film_tour.controller;

import static edu.cnm.deepdive.abq_film_tour.controller.MapsActivity.TITLE_LIST_KEY;
import static edu.cnm.deepdive.abq_film_tour.controller.MapsActivity.SELECTED_OPTIONS_MENU_ITEM_KEY;

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
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import edu.cnm.deepdive.abq_film_tour.R;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This is the selection dialog that appears when a user selects "Film" or "Television Series" from
 * the Options menu. It should not interact with the database at all, the dialog only retrieves a
 * list of title Strings and passes back the selected title.
 */
public class SelectionDialog extends DialogFragment {

  /**
   * This is the parent activity to pass information back to.
   */
  private MapsActivity parentMap;

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    super.onCreateDialog(savedInstanceState);
    AlertDialog.Builder builder = new Builder(getActivity());
    parentMap = (MapsActivity) getActivity();
    View view = Objects.requireNonNull(getActivity())
        .getLayoutInflater().inflate(R.layout.fragment_selection, null, false);
    Toolbar toolbar = view.findViewById(R.id.my_toolbar);
    assert getArguments() != null;
    toolbar.setTitle(getArguments().getString(SELECTED_OPTIONS_MENU_ITEM_KEY));
    ListView selectionListView = view.findViewById(R.id.selection_list_view);
    Bundle arguments = this.getArguments();
    assert arguments != null;
    ArrayList titles = (ArrayList) arguments.get(TITLE_LIST_KEY);
    assert titles != null;
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()),
        android.R.layout.simple_list_item_1, titles);
    selectionListView.setAdapter(adapter);
    selectionListView.setVisibility(View.VISIBLE);
    selectionListView.setOnItemClickListener((parent, view1, position, id) -> {
      //Passes selected title back to parent activity
      System.out.println(titles.get(position));
      String selectedTitle = (String) titles.get(position);
      parentMap.populateMapFromTitle(selectedTitle);
      dismiss();
    });
    builder.setView(view);
    return builder.create();
  }

  @Override
  public void onResume() {
    //this sets the dialog dimensions to the whole screen
    LayoutParams params = Objects.requireNonNull(getDialog().getWindow()).getAttributes();
    params.width = LayoutParams.MATCH_PARENT;
    params.height = LayoutParams.MATCH_PARENT;
    final Drawable d = new ColorDrawable(Color.BLACK);
    d.setAlpha(50); //FIXME what is this magic value?
    getDialog().getWindow().setBackgroundDrawable(d);
    getDialog().getWindow().setAttributes(params);
    super.onResume();
  }

}
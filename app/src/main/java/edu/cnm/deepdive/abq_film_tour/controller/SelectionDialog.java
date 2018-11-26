package edu.cnm.deepdive.abq_film_tour.controller;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

  private RecyclerView tvRecyclerView;
  private TextView titleTextView;
  private List<String> locations;

  public SelectionDialog() {
    // Required empty public constructor
  }



  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_selection, null);
    ListView selectionListView = view.findViewById(R.id.selection_list_view);
    Bundle arguments = this.getArguments();
    assert arguments != null;
    ArrayList titles = (ArrayList) arguments.get(TITLE_LIST_KEY);
    ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
        android.R.layout.simple_list_item_1, titles);
    selectionListView.setAdapter(adapter);
    return view;
  }
}


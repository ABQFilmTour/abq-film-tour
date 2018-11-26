package edu.cnm.deepdive.abq_film_tour.controller;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class LocationActivity extends AppCompatActivity {

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState,
      @Nullable PersistableBundle persistentState) {
    this.setTitle(this.getIntent().getExtras().getString("exampleTag"));
    super.onCreate(savedInstanceState, persistentState);
  }
}

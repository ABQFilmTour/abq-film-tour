package edu.cnm.deepdive.abq_film_tour.controller;

import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Toast;
import edu.cnm.deepdive.abq_film_tour.R;
import edu.cnm.deepdive.abq_film_tour.model.entity.FilmLocation;
import edu.cnm.deepdive.abq_film_tour.model.entity.Production;
import edu.cnm.deepdive.abq_film_tour.model.entity.UserComment;
import edu.cnm.deepdive.abq_film_tour.service.FilmTourApplication;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import retrofit2.Call;
import retrofit2.Response;

public class SubmitCommentDialog extends DialogFragment {

  private FilmTourApplication filmTourApplication;
  private LocationActivity parentActivity;
  private FilmLocation location;
  private Production production;
  private String token;
  EditText commentEditText;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    filmTourApplication = (FilmTourApplication) getActivity().getApplication();
    parentActivity = (LocationActivity) getActivity();
    location = parentActivity.getLocation();
    production = parentActivity.getProduction();
    token = getString(R.string.oauth2_header,
        FilmTourApplication.getInstance().getAccount().getIdToken());

    View view = inflater.inflate(R.layout.submit_comment_fragment, null, false);
    commentEditText = view.findViewById(R.id.register_comment_description);
    Button userCommentButton = view.findViewById(R.id.user_comment_button);

    userCommentButton.setOnClickListener( v -> {
      String textEntered = commentEditText.getText().toString();
      new PostCommentTask().execute(textEntered);
    });

    return view;
  }

  @Override
  public void onResume() {
    super.onResume();

    ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
    params.width = LayoutParams.MATCH_PARENT;
    getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
  }

  public class PostCommentTask extends AsyncTask<String, Void, Boolean> {

    UserComment newComment;

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      newComment = new UserComment();
      newComment.setApproved(true); //Remove me!
      newComment.setFilmLocation(location);
      newComment.setGoogleId(filmTourApplication.getAccount().getId());
      String theText = SubmitCommentDialog.this.commentEditText.getText().toString();
      System.out.println(theText);
      newComment.setText(SubmitCommentDialog.this.commentEditText.getText().toString());
      SimpleDateFormat requiredFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    }

    @Override
    protected Boolean doInBackground(String... strings) {
      Boolean successfulQuery = false;

      Call<UserComment> call = filmTourApplication.getService().postUserComment(token, newComment, location.getId());
      try {
        Response<UserComment> response = call.execute();
        if (response.isSuccessful()) {
          successfulQuery = true;
        } else {
          System.out.println(response);
        }
      } catch (IOException e) {
        //TODO Handle errors
      }
      return successfulQuery;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
      super.onPostExecute(aBoolean);
      if (aBoolean) {
        Toast.makeText(parentActivity, "Success!", Toast.LENGTH_LONG).show();
        dismiss();
      } else {
        Toast.makeText(parentActivity, "oh no", Toast.LENGTH_LONG).show();
      }
    }

  }

}
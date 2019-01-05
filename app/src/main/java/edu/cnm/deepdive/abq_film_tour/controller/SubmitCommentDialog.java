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
import edu.cnm.deepdive.abq_film_tour.FilmTourApplication;
import java.io.IOException;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Dialog fragment for users to upload and submit comments.
 */
public class SubmitCommentDialog extends DialogFragment {

  private FilmTourApplication filmTourApplication;
  private LocationActivity parentActivity;
  private FilmLocation location;
  private Production production;
  private String token;
  private EditText commentEditText;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    filmTourApplication = (FilmTourApplication) Objects.requireNonNull(getActivity()).getApplication();
    parentActivity = (LocationActivity) getActivity();
    location = parentActivity.getLocation();
    production = parentActivity.getProduction();
    token = getString(R.string.oauth2_header,
        FilmTourApplication.getInstance().getAccount().getIdToken());
    View view = inflater.inflate(R.layout.submit_comment_fragment, null, false);
    commentEditText = view.findViewById(R.id.user_comment_edit);
    Button userCommentButton = view.findViewById(R.id.user_comment_button);
    userCommentButton.setOnClickListener( v -> {
      String textEntered = commentEditText.getText().toString();
      new SubmitCommentTask().execute(textEntered);
    });
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    WindowManager.LayoutParams params = Objects.requireNonNull(getDialog().getWindow()).getAttributes();
    params.width = LayoutParams.MATCH_PARENT;
    getDialog().getWindow().setAttributes(params);
  }

  /**
   * Asynchronous task that posts a user submitted comment to the database. Returns a boolean and toasts
   * the user if the submission was successful or unsuccessful.
   */
  public class SubmitCommentTask extends AsyncTask<String, Void, Boolean> {

    /**
     * A new submitted user comment.
     */
    UserComment newComment;

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      newComment = new UserComment();
      newComment.setFilmLocation(location);
      newComment.setGoogleId(filmTourApplication.getAccount().getId());
      newComment.setText(SubmitCommentDialog.this.commentEditText.getText().toString());
      newComment.setApproved(true); // TODO Remove me when security is tighter.
    }

    @Override
    protected Boolean doInBackground(String... strings) {
      boolean successfulQuery = false;
      Call<UserComment> call = filmTourApplication.getService().postUserComment(token, newComment, location.getId());
      try {
        //TODO fix duplicated code
        Response<UserComment> response = call.execute();
        if (response.isSuccessful()) {
          successfulQuery = true;
        } else {
          // Do nothing - toast in onPostExecute displays enough information for now.
        }
      } catch (IOException e) {
        // Do nothing - toast in onPostExecute displays enough information for now.
      }
      return successfulQuery;
    }

    @Override
    protected void onPostExecute(Boolean successfulQuery) {
      super.onPostExecute(successfulQuery);
      if (successfulQuery) {
        Toast.makeText(parentActivity, R.string.submission_successful, Toast.LENGTH_SHORT).show();
        dismiss();
      } else {
        Toast.makeText(parentActivity, R.string.submission_failed, Toast.LENGTH_SHORT).show();
        dismiss();
      }
    }
  }
}
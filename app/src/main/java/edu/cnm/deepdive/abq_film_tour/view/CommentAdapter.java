package edu.cnm.deepdive.abq_film_tour.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import edu.cnm.deepdive.abq_film_tour.R;
import edu.cnm.deepdive.abq_film_tour.model.entity.UserComment;
import java.util.List;

/**
 * This is a custom adapter for user submitted comments.
 */
public class CommentAdapter extends ArrayAdapter<UserComment> {

  /**
   * Instantiates a new Comment adapter.
   *
   * @param context the context
   * @param resource the resource
   */
  public CommentAdapter(@NonNull Context context, int resource) {
    super(context, resource);
  }

  /**
   * Instantiates a new Comment adapter.
   *
   * @param context the context
   * @param resource the resource
   * @param objects the objects
   */
  public CommentAdapter(@NonNull Context context, int resource,
      @NonNull List<UserComment> objects) {
    super(context, resource, objects);
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    TextView adaptedView = new TextView(getContext());
    StringBuilder text = new StringBuilder();
    text.append(getItem(position).getUser().getGoogleName());
    text.append(" says ");
    text.append(getItem(position).getText());
    adaptedView.setText(text);
    adaptedView.setBackgroundResource(R.drawable.round_border_black);
    return adaptedView;
  }
}
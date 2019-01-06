package edu.cnm.deepdive.abq_film_tour.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import edu.cnm.deepdive.abq_film_tour.FilmTourApplication;
import edu.cnm.deepdive.abq_film_tour.R;
import edu.cnm.deepdive.abq_film_tour.model.entity.UserComment;
import java.util.List;
import java.util.Objects;

/**
 * This is a custom adapter for user submitted comments.
 */
public class CommentAdapter extends ArrayAdapter<UserComment> {

  FilmTourApplication filmTourApplication;

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
    View v = convertView;
    if (v == null) {
      LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      assert inflater != null;
      v = inflater.inflate(R.layout.cardview_item_comment, null);
    }

    ImageView imageView = v.findViewById(R.id.user_image_view);
    Glide.with(v)
        .load(Objects.requireNonNull(getItem(position)).getUserPictureUrl())
        .into(imageView);

    TextView userNameView = v.findViewById(R.id.user_name);
    userNameView.setText(Objects.requireNonNull(getItem(position)).getUserName());

    TextView textView = v.findViewById(R.id.comment_text_view);
    StringBuilder text = new StringBuilder();
    text.append(Objects.requireNonNull(getItem(position)).getText());
    textView.setText(text);
    return v;
  }
}
package edu.cnm.deepdive.abq_film_tour.imageShelve;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import edu.cnm.deepdive.abq_film_tour.R;
import java.util.List;

public class RecyclerImageViewAdapter extends RecyclerView.Adapter<RecyclerImageViewAdapter.MyViewHolder> {


  private Context context;
  private List<UserImage> data;

  public RecyclerImageViewAdapter(Context context, List<UserImage> data) {
    this.context = context;
    this.data = data;
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    View view;
    LayoutInflater inflater = LayoutInflater.from(context);
    view = inflater.inflate(R.layout.cardview_item_image, parent, false);
    return new MyViewHolder(view);
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {

    holder.textView_user_name.setText(data.get(position).getUserName());
    holder.image_thumbnail.setImageResource(data.get(position).getThumbnail());
    holder.cardView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {

        Intent intent = new Intent(context, UserImageActivity.class);

        //passes data to the user image class
        intent.putExtra("UserName", data.get(position).getUserName());
        intent.putExtra("Descrpition", data.get(position).getDescription());
        intent.putExtra("Thumbnail", data.get(position).getThumbnail());
        //starts the activity
        context.startActivity(intent);

      }
    });

  }

  @Override
  public int getItemCount() {
    return data.size();
  }

  public static class MyViewHolder extends RecyclerView.ViewHolder{

    TextView textView_user_name;
    ImageView image_thumbnail;
    CardView cardView;

    public MyViewHolder(View itemView) {
      super(itemView);

      textView_user_name = (TextView) itemView.findViewById(R.id.user_image_name_id);
      image_thumbnail = (ImageView) itemView.findViewById(R.id.user_image_id);
      cardView = (CardView) itemView.findViewById(R.id.cardview_image_id);
    }
  }
}

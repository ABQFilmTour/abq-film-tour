package edu.cnm.deepdive.abq_film_tour.imageShelve;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import edu.cnm.deepdive.abq_film_tour.R;
import java.util.ArrayList;
import java.util.List;

public class ImageGallery extends AppCompatActivity {


  List<UserImage> listUserImage;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_image_gallery);

    listUserImage = new ArrayList<>();
    listUserImage.add(new UserImage("Breaking Bad", "Description", R.drawable.bb_user_image_1));
    listUserImage.add(new UserImage("Breaking Bad", "Description", R.drawable.bb_user_image_2));
    listUserImage.add(new UserImage("Breaking Bad", "Description", R.drawable.bb_user_image_3));
    listUserImage.add(new UserImage("Breaking Bad", "Description", R.drawable.bb_user_image_4));
    listUserImage.add(new UserImage("Breaking Bad", "Description", R.drawable.bb_user_image_5));
    listUserImage.add(new UserImage("Breaking Bad", "Description", R.drawable.bb_user_image_1));
    listUserImage.add(new UserImage("Breaking Bad", "Description", R.drawable.bb_user_image_2));
    listUserImage.add(new UserImage("Breaking Bad", "Description", R.drawable.bb_user_image_3));
    listUserImage.add(new UserImage("Breaking Bad", "Description", R.drawable.bb_user_image_4));
    listUserImage.add(new UserImage("Breaking Bad", "Description", R.drawable.bb_user_image_5));

    RecyclerView myRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_user_image_id);
    RecyclerImageViewAdapter myImageViewAdapter = new RecyclerImageViewAdapter(this, listUserImage);
    myRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    myRecyclerView.setAdapter(myImageViewAdapter);
  }
}

package edu.cnm.deepdive.abq_film_tour;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import edu.cnm.deepdive.abq_film_tour.controller.MapsActivity;

public class SplashActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    if (getSupportActionBar() != null) {
      getSupportActionBar().hide();
    }
   Thread thread = new Thread(){
     public void run(){
       try{
         sleep(4000);
       }catch (Exception e){
         e.printStackTrace();
       }finally {
         Intent intent = new Intent(SplashActivity.this,MapsActivity.class);
         startActivity(intent);
       }
     }
   };
   thread.start();
  }

  @Override
  protected void onPause() {
    super.onPause();
    finish();
  }
}

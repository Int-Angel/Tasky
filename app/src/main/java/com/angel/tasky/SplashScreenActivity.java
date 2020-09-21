package com.angel.tasky;

import android.content.Intent;
import android.os.TestLooperManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {


    private final int splash_screen_time=1000; //in milliseconds
    Thread splashTread;
    RelativeLayout splash_back;
    ImageView imageView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splash_back = findViewById(R.id.splash_back);
        imageView = findViewById(R.id.splash_image);
        textView = findViewById(R.id.splash_text);
        final Animation bye = AnimationUtils.loadAnimation(this,R.anim.vanish);
        final Animation bye2 = AnimationUtils.loadAnimation(this,R.anim.vanish);
        final Animation up = AnimationUtils.loadAnimation(this,R.anim.movew_up_little);


        splashTread = new Thread(){
          @Override
          public void run(){
              try{
                  synchronized (this){
                      wait(splash_screen_time);
                  }
              }catch (InterruptedException e){
              }finally {

                  imageView.startAnimation(bye);


              }
          }
        };
        splashTread.start();

        bye.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.setVisibility(View.GONE);
                textView.startAnimation(up);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        up.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                textView.setVisibility(View.GONE);
                Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
